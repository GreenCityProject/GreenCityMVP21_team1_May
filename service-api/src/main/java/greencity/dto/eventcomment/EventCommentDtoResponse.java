package greencity.dto.eventcomment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventShortInfoDto;
import greencity.dto.user.EventCommentAuthorDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class EventCommentDtoResponse {
    @NotNull
    @Min(1)
    private Long id;

    @NotEmpty
    private String text;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @NotNull
    private EventCommentAuthorDto author;

    @NotNull
    private EventShortInfoDto event;

    @JsonIdentityReference(alwaysAsId = true)
    private EventCommentDtoResponse repliedTo;
}
