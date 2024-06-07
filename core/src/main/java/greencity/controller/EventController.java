package greencity.controller;

import greencity.dto.event.EventDtoCreateResponse;
import greencity.dto.event.EventDtoRequest;
import greencity.service.EventService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {

    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<EventDtoCreateResponse> createEvent(@Valid @RequestBody EventDtoRequest dto,
                                                              @AuthenticationPrincipal String principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(dto, principal));
    }

}
