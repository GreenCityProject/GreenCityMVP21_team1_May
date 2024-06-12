package greencity.controller;

import greencity.annotations.MultipartValidation;
import greencity.constant.HttpStatuses;
import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventCreateDtoResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
