package greencity.controller;

import greencity.constant.HttpStatuses;
import greencity.dto.notifications.NotificationDto;
import greencity.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Get three last notifications for user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/newest/{userId}")
    public ResponseEntity<List<NotificationDto>> getThreeLastNotificationsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(notificationService.getThreeLastUnreadNotificationsForUser(userId));
    }

    @Operation(summary = "Get all notifications for user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDto>> getAllNotificationsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(notificationService.findAllForUser(userId));
    }

    @Operation(summary = "Get all unread notifications for user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<NotificationDto>> getUnreadNotificationsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(notificationService.getUnreadNotificationsForUser(userId));
    }

    @Operation(summary = "Get all unread notifications by section for user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/unread/{userId}/{section}")
    public ResponseEntity<List<NotificationDto>> getUnreadNotificationsBySection(@PathVariable Long userId, @PathVariable String section) {
        return ResponseEntity.ok().body(notificationService.getUnreadBySection(userId, section));
    }

    @Operation(summary = "Get amount of unread notifications for user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/unread/amount/{userId}")
    public ResponseEntity<Long> getAmountOfUnreadNotificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok().body(notificationService.getAmountOfUnreadNotificationsByUserId(userId));
    }
}
