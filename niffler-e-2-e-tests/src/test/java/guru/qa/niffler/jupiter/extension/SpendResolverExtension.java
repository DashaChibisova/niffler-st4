package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.RestSpendExtension;
import guru.qa.niffler.model.spend.SpendJson;
import org.junit.jupiter.api.extension.*;

public class SpendResolverExtension implements ParameterResolver {

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter()
        .getType()
        .isAssignableFrom(SpendJson.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(RestSpendExtension.NAMESPACE)
        .get(extensionContext.getUniqueId(), SpendJson.class);
  }
}
