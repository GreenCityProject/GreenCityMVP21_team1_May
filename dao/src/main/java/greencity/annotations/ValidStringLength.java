package greencity.annotations;

import greencity.validator.StringLengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StringLengthValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStringLength {
    String message() default "Description length is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 20;

    int max() default 63206;

    boolean excludeHtml() default true;
}
