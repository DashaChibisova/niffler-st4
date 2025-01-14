package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.DatabaseSpendExtension;

import guru.qa.niffler.model.currency.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//@ExtendWith({RestSpendExtension.class, SpendResolverExtension.class})
@ExtendWith({DatabaseSpendExtension.class})

public @interface GenerateSpend {

    String username();

    String description();

    double amount();

    CurrencyValues currency();
}
