package greencity.dto.event;

import greencity.annotations.ValidStringLength;
import greencity.annotations.ValidUniqueEventDateLocationsDtoRequestList;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static greencity.constant.EventConstants.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EventCreateDtoRequest {

    @NotNull(message = "Title must not be empty")
    @ValidStringLength(min = MIN_TITLE_SIZE, max = MAX_TITLE_SIZE, excludeHtml = true, message = "Title should not be empty and should be up to 70 characters")
    private String title;

    @Valid
    @Size(min = MIN_DATES_COUNT, max = MAX_DATES_COUNT, message = "Must add from 1 to 7 sets of date, time and location parameters")
    @ValidUniqueEventDateLocationsDtoRequestList(message = "List of dates contains duplicates")
    private List<EventDateLocationDtoRequest> dates = new ArrayList<>();

    @NotNull(message = "Description must not be empty")
    @ValidStringLength(min = MIN_DESCRIPTION_SIZE, max = MAX_DESCRIPTION_SIZE, excludeHtml = true, message = "Description length should be from 20 to 63206 characters")
    private String description;

    private Boolean isOpen=true;
}