package greencity.validator;

import greencity.annotations.ValidEventDateLocationsDtoRequestList;
import greencity.dto.event.EventDateLocationDtoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventDateLocationDtoRequestListValidator implements ConstraintValidator<ValidEventDateLocationsDtoRequestList, List<EventDateLocationDtoRequest>> {

    @Override
    public void initialize(ValidEventDateLocationsDtoRequestList constraintAnnotation) {
        //nothing to initialize
    }

    @Override
    public boolean isValid(List<EventDateLocationDtoRequest> dates, ConstraintValidatorContext context) {
        if (dates == null) {
            return true;
        }

        Set<EventDateLocationDtoRequest> uniqueDates = new HashSet<>(dates);

        if (uniqueDates.size() != dates.size()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("List of EventDateLocations have duplicates").addConstraintViolation();
            return false;
        }

        for (EventDateLocationDtoRequest dto : dates) {
            if (!isStartBeforeEnd(dto)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Start date and time must be before end date and time").addConstraintViolation();
                return false;
            }
        }

        return true;
    }

    private boolean isStartBeforeEnd(EventDateLocationDtoRequest dto) {
        if (dto == null) return true;

        LocalDateTime starTime = dto.getStartTime();
        LocalDateTime endTime = dto.getEndTime();

        if (starTime == null || endTime == null) return true;

        return starTime.isBefore(endTime);
    }
}
