package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.*;

public class DbUserExtension implements ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DbUserExtension.class);

    private final UserRepository userRepository = new UserRepositoryJdbc();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        List<Method> methods = new ArrayList<>();
        methods.add(context.getRequiredTestMethod());
        methods.addAll(Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .toList());

        Map<String, Object> users = new HashMap<>();

        for (Method method : methods) {
            DbUser dbUserData = method.getAnnotation(DbUser.class);
            Faker faker = new Faker();

            if (dbUserData != null && !users.containsKey("userAuth")) {
                String userName = dbUserData.username().isEmpty() ? faker.name().username() : dbUserData.username();
                String userPassword = dbUserData.username().isEmpty() ? faker.internet().password() : dbUserData.password();

                UserAuthEntity userAuth = new UserAuthEntity();
                userAuth.setUsername(userName);
                userAuth.setPassword(userPassword);
                userAuth.setEnabled(true);
                userAuth.setAccountNonExpired(true);
                userAuth.setAccountNonLocked(true);
                userAuth.setCredentialsNonExpired(true);
                userAuth.setAuthorities(Arrays.stream(Authority.values())
                        .map(e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setAuthority(e);
                            return ae;
                        }).toList()
                );

                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(userName);
                userEntity.setCurrency(CurrencyValues.RUB);
                userRepository.createInAuth(userAuth);
                userRepository.createInUserdata(userEntity);

                users.put("userAuth", userAuth);
                users.put("userEntity", userEntity);
            }
        }
        context.getStore(NAMESPACE)
                .put(context.getUniqueId(), users);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public UserAuthEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserAuthEntity) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class).get("userAuth");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Map user = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        UserAuthEntity userAuthEntity = (UserAuthEntity) user.get("userAuth");
        userRepository.deleteInAuthById(userAuthEntity.getId());

        UserEntity userEntity = (UserEntity) user.get("userEntity");
        userRepository.deleteInUserDataById(userEntity.getId());
    }
}
