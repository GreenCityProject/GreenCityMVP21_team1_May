package greencity.controller;

import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.service.EventService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
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
}
