package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.eventorganizerranting.RantingCreateDtoRequest;
import greencity.dto.eventorganizerranting.RantingDto;
import greencity.dto.user.UserVO;
import greencity.service.EventOrganizerRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Creates a new rating for an event organizer.
     *
     * @param eventId                 the ID of the event
     * @param rantingCreateDtoRequest the DTO containing rating information
     * @param userVO                  the current user information
     * @return the created rating DTO wrapped in a ResponseEntity
     */
    @Operation(summary = "Create a new rating for an event organizer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RantingDto.class))),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
        @ApiResponse(responseCode = "409", description = HttpStatuses.CONFLICT),
    })
    @PostMapping
    public ResponseEntity<RantingDto> create(@PathVariable Long eventId,
                                             @RequestBody @Validated RantingCreateDtoRequest rantingCreateDtoRequest,
                                             @Parameter(hidden = true) @CurrentUser UserVO userVO) {
        return ResponseEntity.status(CREATED)
            .body(eventOrganizerRatingService.create(eventId, rantingCreateDtoRequest, userVO));
    }

    /**
     * Finds the average rating for an event organizer.
     *
     * @param eventId the ID of the event
     * @param userVO  the current user information
     * @return the average rating wrapped in a ResponseEntity
     */
    @Operation(summary = "Get the average rating for an event organizer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Double.class))}),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @GetMapping
    public ResponseEntity<Double> find(@PathVariable Long eventId,
                                       @Parameter(hidden = true) @CurrentUser UserVO userVO) {
        return ResponseEntity.ok().body(eventOrganizerRatingService.find(eventId, userVO));
    }
}
