package greencity.dto.user;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class EventCommentAuthorDto {
    private Long id;
    private String name;
    private String email;
}
