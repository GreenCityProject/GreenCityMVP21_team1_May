package greencity.validator;

import greencity.annotations.ValidStartBeforeEndDates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class StartBeforeEndDatesValidator implements ConstraintValidator<ValidStartBeforeEndDates, Object> {

    @Override
    public void initialize(ValidStartBeforeEndDates constraintAnnotation) {
        //nothing to initialize
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true;
        }

        LocalDateTime starTime = null;
        LocalDateTime endTime = null;

        try {
            starTime = (LocalDateTime) obj.getClass().getMethod("getStartTime").invoke(obj);
            endTime = (LocalDateTime) obj.getClass().getMethod("getEndTime").invoke(obj);
        } catch (Exception e) {
            // todo: Handle exception
        }

        if (starTime == null || endTime == null) {
            return true;
        }

        return starTime.isBefore(endTime);
    }
}
