package guru.qa.niffler.service;


import guru.qa.grpc.niffler.grpc.*;
import guru.qa.niffler.data.FriendsEntity;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;


@GrpcService
public class GrpcUserdataService extends NifflerUserdataServiceGrpc.NifflerUserdataServiceImplBase {
    private final UserRepository userRepository;

    @Autowired
    public GrpcUserdataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public void getCurrentUser(UserName request, StreamObserver<User> responseObserver) {

        UserEntity user = getRequiredUser(request.getUsername());

        User response = User.newBuilder()
                .setUsername(user.getUsername())
                .setFirstname(convertToEmpty(user.getFirstname()).toString())
                .setSurname(convertToEmpty(user.getSurname()).toString())
                .setCurrency(CurrencyValues.valueOf(convertToEmpty(user.getCurrency().name()).toString()))
                .setPhoto(convertToEmpty(user.getPhoto()).toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void postUpdateUserInfo(User request,
                                   StreamObserver<User> responseObserver) {
        UserEntity user = getRequiredUser(request.getUsername());

        user.setUsername(request.getUsername());
        user.setFirstname(convertToEmpty(request.getFirstname()).toString());
        user.setSurname(convertToEmpty(request.getSurname()).toString());
        user.setCurrency(guru.qa.niffler.data.CurrencyValues.valueOf(request.getCurrency().name()));
        user.setPhoto(user.getPhoto());

        userRepository.save(user);

        User response = User.newBuilder()
                .setUsername(user.getUsername())
                .setFirstname(convertToEmpty(user.getFirstname()).toString())
                .setSurname(convertToEmpty(user.getSurname()).toString())
                .setCurrency(CurrencyValues.valueOf(convertToEmpty(user.getCurrency().name()).toString()))
                .setPhoto(convertToEmpty(user.getPhoto()).toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void getAllUsers(UserName request,
                            StreamObserver<UsersResponce> responseObserver) {
        Set<UserJson> result = new HashSet<>();

        for (UserEntity user : userRepository.findByUsernameNot(request.getUsername())) {
            List<FriendsEntity> sendInvites = user.getFriends();
            List<FriendsEntity> receivedInvites = user.getInvites();

            if (!sendInvites.isEmpty() || !receivedInvites.isEmpty()) {
                Optional<FriendsEntity> inviteToMe = sendInvites.stream()
                        .filter(i -> i.getFriend().getUsername().equals(request.getUsername()))
                        .findFirst();

                Optional<FriendsEntity> inviteFromMe = receivedInvites.stream()
                        .filter(i -> i.getUser().getUsername().equals(request.getUsername()))
                        .findFirst();

                if (inviteToMe.isPresent()) {
                    FriendsEntity invite = inviteToMe.get();
                    result.add(UserJson.fromEntity(user, invite.isPending()
                            ? guru.qa.niffler.model.FriendState.INVITE_RECEIVED
                            : guru.qa.niffler.model.FriendState.FRIEND));
                }
                if (inviteFromMe.isPresent()) {
                    FriendsEntity invite = inviteFromMe.get();
                    result.add(UserJson.fromEntity(user, invite.isPending()
                            ? guru.qa.niffler.model.FriendState.INVITE_SENT
                            : FriendState.FRIEND));
                }
            } else {
                result.add(UserJson.fromEntity(user));
            }
        }

        UsersResponce usersResponce = UsersResponce.newBuilder()
                .addAllUser(result.stream().map(e -> User.newBuilder()
                                .setUsername(e.username())
                                .setFirstname(convertToEmpty(e.firstname()).toString())
                                .setSurname(convertToEmpty(e.surname()).toString())
                                .setCurrency(CurrencyValues.valueOf(convertToEmpty(e.currency().name()).toString()))
                                .setPhoto(convertToEmpty(e.photo()).toString())
                                .build())
                        .toList())
                .build();

        responseObserver.onNext(usersResponce);
        responseObserver.onCompleted();
    }

    private Object convertToEmpty(Object param) {
        return Objects.requireNonNullElse(param, "");
    }

    @Nonnull
    UserEntity getRequiredUser(@Nonnull String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        return user;
    }

    @Override
    public void getFriends(FrendRequest request,
                           StreamObserver<UsersResponce> responseObserver) {

        List<FriendsEntity> friends = getRequiredUser(request.getUsername()).getFriends();

        UsersResponce usersResponce = UsersResponce.newBuilder()
                .addAllUser(friends.stream().filter(fe -> request.getIncludePending() || !fe.isPending())
                        .map(e -> User.newBuilder()
                                .setUsername(e.getFriend().getUsername())
                                .setFirstname(convertToEmpty(e.getFriend().getFirstname()).toString())
                                .setSurname(convertToEmpty(e.getFriend().getSurname()).toString())
                                .setCurrency(CurrencyValues.valueOf(convertToEmpty(e.getFriend().getCurrency().name()).toString()))
                                .setPhoto(convertToEmpty(e.getFriend().getPhoto()).toString())
                                .setFriendState(e.isPending()
                                        ? guru.qa.grpc.niffler.grpc.FriendState.INVITE_SENT
                                        : guru.qa.grpc.niffler.grpc.FriendState.FRIEND)
                                .build())
                        .toList())
                .build();

        responseObserver.onNext(usersResponce);
        responseObserver.onCompleted();
    }

    @Override
    public void getInvitations(UserName request,
                               StreamObserver<UsersResponce> responseObserver) {

        List<FriendsEntity> invites = getRequiredUser(request.getUsername()).getInvites();

        UsersResponce usersResponce = UsersResponce.newBuilder()
                .addAllUser(invites.stream().filter(FriendsEntity::isPending).map(e -> User.newBuilder()
                                .setUsername(e.getUser().getUsername())
                                .setFirstname(convertToEmpty(e.getUser().getFirstname()).toString())
                                .setSurname(convertToEmpty(e.getUser().getSurname()).toString())
                                .setCurrency(CurrencyValues.valueOf(convertToEmpty(e.getUser().getCurrency().name()).toString()))
                                .setPhoto(convertToEmpty(e.getFriend().getPhoto()).toString())
                                .setFriendState(guru.qa.grpc.niffler.grpc.FriendState.INVITE_RECEIVED)
                                .build())
                        .toList())
                .build();

        responseObserver.onNext(usersResponce);
        responseObserver.onCompleted();

    }

    @Override
    public void postAcceptInvitation(InvitationRequest request,
                                     StreamObserver<UsersResponce> responseObserver) {

        UserEntity currentUser = getRequiredUser(request.getUsername());
        UserEntity inviteUser = getRequiredUser(request.getInvitation().getUsername());


        FriendsEntity invite = currentUser.getInvites()
                .stream()
                .filter(fe -> fe.getUser().getUsername().equals(inviteUser.getUsername()))
                .findFirst()
                .orElseThrow();

        invite.setPending(false);
        currentUser.addFriends(false, inviteUser);
        userRepository.save(currentUser);

        UsersResponce usersResponce = UsersResponce.newBuilder()
                .addAllUser(currentUser.getFriends().stream().map(e -> User.newBuilder()
                                .setUsername(e.getFriend().getUsername())
                                .setFirstname(convertToEmpty(e.getFriend().getFirstname()).toString())
                                .setSurname(convertToEmpty(e.getFriend().getSurname()).toString())
                                .setCurrency(CurrencyValues.valueOf(convertToEmpty(e.getFriend().getCurrency().name()).toString()))
                                .setPhoto(convertToEmpty(e.getFriend().getPhoto()).toString())
                                .setFriendState(e.isPending()
                                        ? guru.qa.grpc.niffler.grpc.FriendState.INVITE_SENT
                                        : guru.qa.grpc.niffler.grpc.FriendState.FRIEND)
                                .build())
                        .toList())
                .build();

        responseObserver.onNext(usersResponce);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void postDeclineInvitation(InvitationRequest request,
                                      StreamObserver<UsersResponce> responseObserver) {

        UserEntity currentUser = getRequiredUser(request.getUsername());
        UserEntity friendToDecline = getRequiredUser(request.getInvitation().getUsername());

        currentUser.removeInvites(friendToDecline);
        friendToDecline.removeFriends(currentUser);

        userRepository.save(currentUser);
        userRepository.save(friendToDecline);

        UsersResponce usersResponce = UsersResponce.newBuilder()
                .addAllUser(currentUser.getInvites().stream().filter(FriendsEntity::isPending)
                        .map(e -> User.newBuilder()
                                .setUsername(e.getFriend().getUsername())
                                .setFirstname(convertToEmpty(e.getFriend().getFirstname()).toString())
                                .setSurname(convertToEmpty(e.getFriend().getSurname()).toString())
                                .setCurrency(CurrencyValues.valueOf(convertToEmpty(e.getFriend().getCurrency().name()).toString()))
                                .setPhoto(convertToEmpty(e.getFriend().getPhoto()).toString())
                                .setFriendState(guru.qa.grpc.niffler.grpc.FriendState.INVITE_RECEIVED)
                               .build())
                        .toList())
                .build();

        responseObserver.onNext(usersResponce);
        responseObserver.onCompleted();
    }

    @Override
    public void postAddFriend(InvitationRequest request,
                              StreamObserver<User> responseObserver) {
        UserEntity currentUser = getRequiredUser(request.getUsername());
        UserEntity friendEntity = getRequiredUser(request.getInvitation().getUsername());

        currentUser.addFriends(true, friendEntity);
        userRepository.save(currentUser);

        User response = User.newBuilder()
                .setUsername(friendEntity.getUsername())
                .setFirstname(convertToEmpty(friendEntity.getFirstname()).toString())
                .setSurname(convertToEmpty(friendEntity.getSurname()).toString())
                .setCurrency(CurrencyValues.valueOf(convertToEmpty(friendEntity.getCurrency().name()).toString()))
                .setPhoto(convertToEmpty(friendEntity.getPhoto()).toString())
                .setFriendState(guru.qa.grpc.niffler.grpc.FriendState.INVITE_SENT)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void deleteRemoveFriend(RemoveFriendRequest request,
                                   StreamObserver<UsersResponce> responseObserver) {

        UserEntity currentUser = getRequiredUser(request.getUsername());
        UserEntity friendToRemove = getRequiredUser(request.getFriendUsername());

        currentUser.removeFriends(friendToRemove);
        currentUser.removeInvites(friendToRemove);
        friendToRemove.removeFriends(currentUser);
        friendToRemove.removeInvites(currentUser);

        userRepository.save(currentUser);
        userRepository.save(friendToRemove);

        UsersResponce usersResponce = UsersResponce.newBuilder()
                .addAllUser(currentUser.getFriends().stream()
                        .map(e -> User.newBuilder()
                                .setUsername(e.getFriend().getUsername())
                                .setFirstname(convertToEmpty(e.getFriend().getFirstname()).toString())
                                .setSurname(convertToEmpty(e.getFriend().getSurname()).toString())
                                .setCurrency(CurrencyValues.valueOf(convertToEmpty(e.getFriend().getCurrency().name()).toString()))
                                .setPhoto(convertToEmpty(e.getFriend().getPhoto()).toString())
                                .setFriendState(e.isPending()
                                        ? guru.qa.grpc.niffler.grpc.FriendState.INVITE_SENT
                                        : guru.qa.grpc.niffler.grpc.FriendState.FRIEND)
                                .build())
                        .toList())
                .build();

        responseObserver.onNext(usersResponce);
        responseObserver.onCompleted();
    }
}
