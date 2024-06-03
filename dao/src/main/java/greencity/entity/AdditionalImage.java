package greencity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "additional_images")
@EqualsAndHashCode
@ToString
public class AdditionalImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotNull
    @Column(name = "data", nullable = false)
    private String data;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}
