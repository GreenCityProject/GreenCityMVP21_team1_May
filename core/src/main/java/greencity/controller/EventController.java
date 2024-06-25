package greencity.controller;

import greencity.annotations.MultipartValidation;
import greencity.constant.HttpStatuses;
import greencity.annotations.CurrentUser;
import greencity.dto.event.EventAttenderDto;
import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventUpdateDtoRequest;
import greencity.dto.user.UserVO;
import greencity.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static greencity.constant.EventConstants.*;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {

    private EventService eventService;


    /**
     * Method creates Event by given {@link EventCreateDtoRequest}.
     *
     * @param dto       of {@link EventCreateDtoRequest}.
     * @param images    is an array of {@link MultipartFile} with event images. First one will be chosen as title.
     * @param principal is automatically inserted vie SecurityContextHolder.
     * @return {@link EventCreateDtoResponse}.
     */
    @Operation(summary = "Create new Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED,
                    content = @Content(schema = @Schema(implementation = EventCreateDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "409", description = HttpStatuses.CONFLICT)
    })
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EventCreateDtoResponse> create(
            @Valid @RequestPart("dto") EventCreateDtoRequest dto,
            @Parameter(description = "Array of event images")
            @MultipartValidation(maxFileSize = ACCEPTABLE_IMAGE_SIZE_IN_BYTES, fileTypes = FILE_TYPES_STR)
            @Size(max = MAX_IMAGES_COUNT)
            @RequestPart("images") MultipartFile[] images,
            @AuthenticationPrincipal String principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(dto, images, principal));
    }


    /**
     * Method for getting all events.
     *
     * @return list of {@link EventCreateDtoResponse}
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Get all events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED)
    })
    @GetMapping("")
    public ResponseEntity<List<EventCreateDtoResponse>> getAllEvents(){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAll());
    }

    /**
     * Method for getting event by id.
     *
     * @return object of {@link EventCreateDtoResponse}
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Get event by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventCreateDtoResponse> getEventById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findEventById(id));
    }


    /**
     * Method for updating event by id.
     *
     * @return object of {@link EventCreateDtoResponse}
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Update event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PutMapping(value="/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EventCreateDtoResponse> updateEvent(
            @Parameter(required = true) @Valid @RequestPart("dto") EventUpdateDtoRequest eventUpdate,
            @Parameter(description = "Images for events") @Size(max = 5) @RequestPart("images") MultipartFile[] images,
            @Parameter(hidden = true) @CurrentUser UserVO user
    ){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.update(eventUpdate, images, user));
    }

    /**
     * Method for adding an attender to the event.
     *
     * @author Maksym Petukhov.
     */
    @Operation(summary = "Add an attender to the event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("/attenders/{eventId}")
    public void addAttender(@PathVariable Long eventId, @Parameter(hidden = true) @CurrentUser UserVO user) {
        eventService.addAttender(eventId, user);
    }

    /**
     * Method for deleting an attender from the event.
     *
     * @param eventId id of event
     * @param user    {@link UserVO} of authenticated user
     */
    @Operation(summary = "Delete an attender from the event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("/attenders/{eventId}")
    public void deleteAttender(@PathVariable Long eventId, @Parameter(hidden = true) @CurrentUser UserVO user) {
        eventService.deleteAttender(eventId, user);
    }

    /**
     * Method for getting all event attenders by event id.
     *
     * @param eventId id of event
     * @return list of {@link UserVO}
     */
    @Operation(summary = "Get all event attenders by event id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/attenders/{eventId}")
    public ResponseEntity<List<EventAttenderDto>> getAllEventAttenders(@PathVariable Long eventId,
                                                                       @Parameter(hidden = true) @CurrentUser UserVO user){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEventAttenders(eventId, user));
    }
}