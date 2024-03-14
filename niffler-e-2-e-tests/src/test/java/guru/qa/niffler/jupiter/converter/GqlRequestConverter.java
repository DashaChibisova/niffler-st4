package guru.qa.niffler.jupiter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.GqlRequestFile;
import guru.qa.niffler.jupiter.annotation.GqlRequestFileConvertor;
import guru.qa.niffler.jupiter.extension.GqlRequestResolver;
import guru.qa.niffler.model.gql.GqlRequest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.io.InputStream;

public class GqlRequestConverter implements ArgumentConverter {

  private static final ObjectMapper om = new ObjectMapper();
  private final ClassLoader cl = GqlRequestResolver.class.getClassLoader();

  @Override
  public Object convert(Object source, ParameterContext parameterContext) throws ArgumentConversionException {
    System.out.println("ggggggggggggggggggggggg");
    if (source == null) {
      throw new ArgumentConversionException("Cannot convert null source object");
    }

    if (!source.getClass().equals(String.class)) {
      throw new ArgumentConversionException(
              "Cannot convert source object because it's not a string"
      );
    }
    if(!(source instanceof String) || !parameterContext.isAnnotated(GqlRequestFileConvertor.class)) {
      throw new RuntimeException("Cannot convert to GqlRequest");
    }
    try (InputStream is = cl.getResourceAsStream((String) source)) {
      return om.readValue(is.readAllBytes(), GqlRequest.class);
    } catch (IOException e) {
      throw new ArgumentConversionException(e.getMessage());
    }
  }
}
