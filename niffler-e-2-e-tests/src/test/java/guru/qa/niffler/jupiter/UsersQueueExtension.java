package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.User.UserType.*;
import static guru.qa.niffler.jupiter.User.UserType.RECIEVED;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  private static Map<User.UserType, Queue<UserJson>> users = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> commonQueue = new ConcurrentLinkedQueue<>();
    friendsQueue.add(user("dima", "12345", WITH_FRIENDS));
    friendsQueue.add(user("duck", "12345", WITH_FRIENDS));
    commonQueue.add(user("bee", "12345", COMMON));
    commonQueue.add(user("barsik", "12345", COMMON));
    users.put(WITH_FRIENDS, friendsQueue);
    users.put(COMMON, commonQueue);

    Queue<UserJson> invitationQueue = new ConcurrentLinkedQueue<>();
    invitationQueue.add(user("lion", "12345", INVITATION_SEND));
    invitationQueue.add(user("goose", "12345", INVITATION_SEND));
    users.put(INVITATION_SEND, invitationQueue);

    Queue<UserJson> recievedQueue = new ConcurrentLinkedQueue<>();
    recievedQueue.add(user("parrot", "12345", RECIEVED));
    recievedQueue.add(user("lion", "12345", RECIEVED));
    users.put(RECIEVED, recievedQueue);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Method methods = context.getRequiredTestMethod();
    Method[] declaredMethods = context.getRequiredTestClass().getDeclaredMethods();

    List<Method> listMethodsBeforeEach = new ArrayList<>();
    for (Method method : declaredMethods) {
      if (method.isAnnotationPresent(BeforeEach.class)) {
        listMethodsBeforeEach.add(method);
      }
    }

    Map<User.UserType, UserJson> testCandidates = new HashMap<>();

    for (Method methodBeforeEach : listMethodsBeforeEach) {
        reserveTestCandidate(testCandidates,methodBeforeEach );
      }
      reserveTestCandidate(testCandidates, methods);

      context.getStore(NAMESPACE).put(context.getUniqueId(), testCandidates);
  }

  private void reserveTestCandidate( Map<User.UserType, UserJson> testCandidates, Method methods ) {
    for (Parameter parameter : methods.getParameters()) {
      User annotation = parameter.getAnnotation(User.class);
      if (annotation != null && parameter.getType().isAssignableFrom(UserJson.class) && !testCandidates.containsKey(annotation.value())) {
        UserJson testCandidate = null;
        Queue<UserJson> queue = users.get(annotation.value());
        while (testCandidate == null) {
          testCandidate = queue.poll();
        }
        testCandidates.put(annotation.value(), testCandidate);
      }
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Map <User.UserType, UserJson> usersTest = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
    for (Map.Entry<User.UserType, UserJson> entry : usersTest.entrySet()) {
      users.get(entry.getKey()).add(entry.getValue());
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter()
        .getType()
        .isAssignableFrom(UserJson.class) &&
        parameterContext.getParameter().isAnnotationPresent(User.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    User.UserType userType = parameterContext.getParameter().getAnnotation(User.class).value();
    Map <User.UserType, UserJson> usersTest = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
    return usersTest.get(userType);
  }

  private static UserJson user(String username, String password, User.UserType userType) {
    return new UserJson(
        null,
        username,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        new TestData(
            password,
            userType
        )
    );
  }
}
