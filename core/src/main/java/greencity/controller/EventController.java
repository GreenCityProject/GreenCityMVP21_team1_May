package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventUpdateDtoRequest;
import greencity.dto.user.UserVO;
import greencity.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {

    private EventService eventService;

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EventCreateDtoResponse> createEvent(
            @Valid @RequestPart("dto") EventCreateDtoRequest dto,
            @Parameter(description = "Array of event images") @Size(max = 5) @RequestPart("images") MultipartFile[] images,
            @AuthenticationPrincipal String principal) {
        imagesValidationOrElseTrow(List.of(images));
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(dto, images, principal));
    }

    private void imagesValidationOrElseTrow(List<MultipartFile> images) {
        if (images.isEmpty()) return;
        List<String> validFileTypes = Arrays.asList("jpeg", "png", "jpg");
        long acceptableSizeInBytes = 10000000L;

        for (MultipartFile image : images) {
            String originalFilename = Objects.requireNonNull(image.getOriginalFilename(), "Original file name is NULL by some reason.");
            String fileType = originalFilename.toLowerCase().substring(originalFilename.lastIndexOf(".") + 1);
            if (fileType.isBlank()) return;

            if (!validFileTypes.contains(fileType)) {
                throw new ValidationException(String.format("Unsupported format: '%s'. Supported formats: {%s}",
                        fileType,
                        validFileTypes));
            }

            if (Long.compare(image.getSize(), acceptableSizeInBytes) == 1) {
                throw new ValidationException("Incorrect image size. Maximum allowed size is 10 MB");
            }
        }
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

}
