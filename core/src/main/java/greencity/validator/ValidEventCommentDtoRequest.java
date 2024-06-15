package greencity.validator;

import greencity.annotations.ValidEventCommentRequest;
import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import greencity.dto.eventcomment.UpdateEventCommentDtoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEventCommentDtoRequest implements ConstraintValidator<ValidEventCommentRequest, Object> {

    private boolean isAddEventCommentDtoRequest(Object obj) {
        return obj instanceof AddEventCommentDtoRequest;
    }

    private boolean isUpdateEventCommentDtoRequest(Object obj) {
        return obj instanceof UpdateEventCommentDtoRequest;
    }

    @Override
    public boolean isValid(Object request, ConstraintValidatorContext constraintValidatorContext) {
        if (isAddEventCommentDtoRequest(request)) {
            AddEventCommentDtoRequest addRequest = (AddEventCommentDtoRequest) request;
            // Implement validation logic for AddEventCommentDtoRequest
            // For example:
            // return addRequest.getText() != null && !addRequest.getText().isEmpty();
            return true; // Placeholder, replace with actual validation
        } else if (isUpdateEventCommentDtoRequest(request)) {
            UpdateEventCommentDtoRequest updateRequest = (UpdateEventCommentDtoRequest) request;
            // Implement validation logic for UpdateEventCommentDtoRequest
            // For example:
            // return updateRequest.getText() != null && !updateRequest.getText().isEmpty();
            return true; // Placeholder, replace with actual validation
        }

        // If request type is neither AddEventCommentDtoRequest nor UpdateEventCommentDtoRequest
        return false;
    }
}
