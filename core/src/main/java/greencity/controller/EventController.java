package greencity.controller;

import greencity.annotations.MultipartValidation;
import greencity.constant.HttpStatuses;
import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import static greencity.constant.EventConstants.*;

@Validated
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
     * Method for deleting an event.
     *
     * @author Max Bohonko.
     */
    @Operation(summary = "Delete event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED))),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN,
                    content = @Content(examples = @ExampleObject(HttpStatuses.FORBIDDEN))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Object> delete(@PathVariable Long eventId, @Parameter(hidden = true) Principal principal) {
        eventService.delete(eventId, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
