package greencity.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import greencity.validator.OrganizerRatingValidator;

@Constraint(validatedBy = OrganizerRatingValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrganizerRatingValidation {

    String message() default "You can only enter 1, 2 or 3.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
