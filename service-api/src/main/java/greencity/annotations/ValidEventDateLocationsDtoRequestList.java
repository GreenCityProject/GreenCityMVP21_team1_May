package greencity.annotations;

import greencity.validator.EventDateLocationDtoRequestListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EventDateLocationDtoRequestListValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventDateLocationsDtoRequestList {
    String message() default "List of event date and are not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}