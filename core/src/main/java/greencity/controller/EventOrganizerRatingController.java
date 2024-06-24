package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.dto.eventorganizerranting.RantingCreateDtoRequest;
import greencity.dto.eventorganizerranting.RantingDto;
import greencity.dto.user.UserVO;
import greencity.service.EventOrganizerRatingService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event/{eventId}/organizer-rating")
@RequiredArgsConstructor
public class EventOrganizerRatingController {

    private final EventOrganizerRatingService eventOrganizerRatingService;

    @PostMapping
    public ResponseEntity<RantingDto> create(@PathVariable Long eventId,
                                             @RequestBody @Validated RantingCreateDtoRequest rantingCreateDtoRequest,
                                             @Parameter(hidden = true) @CurrentUser UserVO userVO) {
        return ResponseEntity.status(CREATED).body(eventOrganizerRatingService.create(eventId, rantingCreateDtoRequest, userVO));
    }

    @GetMapping
    public ResponseEntity<Double> find(@PathVariable Long eventId,
                                                    @Parameter(hidden = true) @CurrentUser UserVO userVO) {
        return ResponseEntity.ok().body(eventOrganizerRatingService.find(eventId, userVO));
    }

}
