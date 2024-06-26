package greencity.validator;

import greencity.annotations.OrganizerRatingValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class OrganizerRatingValidator implements ConstraintValidator<OrganizerRatingValidation, Integer> {

    private final List<Integer> validRating = List.of(1, 2, 3);

    @Override
    public boolean isValid(Integer rating, ConstraintValidatorContext constraintValidatorContext) {
        return validRating.contains(rating);
    }
}
