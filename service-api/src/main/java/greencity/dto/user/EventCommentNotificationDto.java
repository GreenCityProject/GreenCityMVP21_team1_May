package greencity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCommentNotificationDto {
    private String authorEmail;
    private String authorName;
    private String eventTitle;
    private String commentAuthor;
    private String commentShortText;
    private LocalDateTime commentDate;
    private String commentLink;
}
