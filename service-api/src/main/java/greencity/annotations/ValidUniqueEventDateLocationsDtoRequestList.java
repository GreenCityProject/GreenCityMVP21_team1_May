package greencity.annotations;

import greencity.validator.UniqueEventDateLocationDtoRequestListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEventDateLocationDtoRequestListValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUniqueEventDateLocationsDtoRequestList {
    String message() default "List contains duplicate event date locations";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}