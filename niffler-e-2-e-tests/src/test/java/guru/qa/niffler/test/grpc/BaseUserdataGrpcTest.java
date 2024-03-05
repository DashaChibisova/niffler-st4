package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.NifflerUserdataServiceGrpc;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import guru.qa.niffler.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

@GrpcTest
public abstract class BaseUserdataGrpcTest {

  protected static final Config CFG = Config.getInstance();

  protected static Channel channel;

  protected static NifflerUserdataServiceGrpc.NifflerUserdataServiceBlockingStub blockingStubUser;


  static {
    channel = ManagedChannelBuilder.forAddress(CFG.userdataGrpcHost(), CFG.userdataGrpcPort())
            .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
            .usePlaintext()
            .build();

    blockingStubUser = NifflerUserdataServiceGrpc.newBlockingStub(channel);
  }
}
