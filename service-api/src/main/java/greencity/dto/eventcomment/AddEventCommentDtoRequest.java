package greencity.dto.eventcomment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AddEventCommentDtoRequest {
    @NotNull(message = "The text of comment can not be empty")
    @Size(min = 1, max = 8000, message = "The text of comment must be between 1 and 8000 symbols")
    private String text;

    private Long repliedTo;
}
