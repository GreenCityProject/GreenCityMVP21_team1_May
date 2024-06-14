package greencity.validator;

import greencity.annotations.ValidEventCommentRequest;
import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEventCommentDtoRequest implements ConstraintValidator<ValidEventCommentRequest, AddEventCommentDtoRequest> {
    @Override
    public boolean isValid(AddEventCommentDtoRequest request, ConstraintValidatorContext constraintValidatorContext) {
        // todo
        return true;
    }
}
