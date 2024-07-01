package greencity.controller;

import greencity.annotations.MultipartValidation;
import greencity.annotations.ValidStringLength;
import greencity.constant.HttpStatuses;
import greencity.annotations.CurrentUser;
import greencity.dto.PageableAdvancedDto;
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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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
     * Method for searching for the event by key words.
     *
     * @return object of {@link EventCreateDtoResponse}
     * @author Mashkin Andriy
     */
    @Operation(summary = "Search the event by key words")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/search")
    public ResponseEntity<PageableAdvancedDto<EventCreateDtoResponse>> getEventByQuery(
            @Pattern(regexp = "^[a-zA-Zа-яА-ЯіІїЇєЄґҐ .'-]{1,30}$",
                    message = "Query allows only 1-30 symbols: ENG and UKR alphabetic characters, a dot, a space, apostrophe and hyphen")
            @PathParam("query") String query,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findEventByQuery(query, pageable));
    }
}
