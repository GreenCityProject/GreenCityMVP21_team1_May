package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.annotations.ValidEventCommentRequest;
import greencity.constant.HttpStatuses;
import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import greencity.dto.eventcomment.EventCommentDtoResponse;
import greencity.dto.eventcomment.UpdateEventCommentDtoRequest;
import greencity.dto.user.UserVO;
import greencity.service.EventCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class EventCommentController {
    private final EventCommentService eventCommentService;

    /**
     * Method for creating event comment.
     *
     * @param eventId id of event to add comment to.
     * @param request dto for EventComment entity.
     * @return dto {@link EventCommentDtoResponse}
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Add comment to event")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED,
                    content = @Content(schema = @Schema(implementation = EventCommentDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("/{eventId}/comments/create")
    public ResponseEntity<EventCommentDtoResponse> save(@PathVariable Long eventId,
                                                        @Valid @ValidEventCommentRequest @RequestBody AddEventCommentDtoRequest request,
                                                        @Parameter(hidden = true) @CurrentUser UserVO user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventCommentService.save(eventId, request, user));
    }


    /**
     * Method for getting all event comments.
     *
     * @param eventId id of event to get comments .
     * @return list of dtos {@link EventCommentDtoResponse}
     *
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Get comments of event")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = EventCommentDtoResponse.class))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{eventId}/comments")
    public ResponseEntity<List<EventCommentDtoResponse>> getAllCommentsByEventId(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventCommentService.getAllByEventId(eventId));
    }

    /**
     * Method for getting event comment by id.
     *
     * @param commentId id of comment .
     * @return list of dtos {@link EventCommentDtoResponse}
     *
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Get comments of event")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = EventCommentDtoResponse.class))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<EventCommentDtoResponse> getCommentById(@PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventCommentService.findCommentById(commentId));
    }


    /**
     * Method for getting amount of comments for event.
     *
     * @param eventId id of event to count comments .
     * @return Integer number of comments
     *
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Get amount of comments for event")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = EventCommentDtoResponse.class))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{eventId}/comments/count")
    public ResponseEntity<Integer> getCommentsCountByEventId(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventCommentService.getCommentsCountByEventId(eventId));
    }

    /**
     * Method for editing event comment.
     *
     * @param eventId id of event of comment to edit.
     * @param request dto for EventComment entity.
     * @return dto {@link EventCommentDtoResponse}
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Edit comment to event")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = EventCommentDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PutMapping("/{eventId}/comments/update")
    public ResponseEntity<EventCommentDtoResponse> update(@PathVariable Long eventId,
                                                        @Valid @ValidEventCommentRequest @RequestBody UpdateEventCommentDtoRequest request,
                                                        @Parameter(hidden = true) @CurrentUser UserVO user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventCommentService.update(eventId, request, user));
    }
}
