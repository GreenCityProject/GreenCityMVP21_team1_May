package greencity.validator;

import greencity.annotations.ValidUniqueEventDateLocationsDtoRequestList;
import greencity.dto.event.EventDateLocationDtoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueEventDateLocationDtoRequestListValidator implements ConstraintValidator<ValidUniqueEventDateLocationsDtoRequestList, List<EventDateLocationDtoRequest>> {

    @Override
    public void initialize(ValidUniqueEventDateLocationsDtoRequestList constraintAnnotation) {
        //nothing to initialize
    }

    @Override
    public boolean isValid(List<EventDateLocationDtoRequest> dates, ConstraintValidatorContext context) {
        if (dates == null) {
            return true;
        }

        Set<EventDateLocationDtoRequest> uniqueDates = new HashSet<>(dates);

        return uniqueDates.size() == dates.size();
    }
}
