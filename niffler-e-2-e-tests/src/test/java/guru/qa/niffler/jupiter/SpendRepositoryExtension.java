package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.repository.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class SpendRepositoryExtension implements TestInstancePostProcessor {
  @Override
  public void postProcessTestInstance(Object o, ExtensionContext extensionContext) throws Exception {
    for (Field field : o.getClass().getDeclaredFields()) {
      if (field.getType().isAssignableFrom(SpendRepository.class)) {
        field.setAccessible(true);
        field.set(o, new SpendRepositoryJdbc());
      }
    }
  }
}
