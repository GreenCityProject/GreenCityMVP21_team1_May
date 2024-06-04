package greencity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "event_date_locations")
@EqualsAndHashCode
@ToString
public class EventDateLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    @Future
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;//Date must be equal to end Date. if add day - set to 9:00 on front-end

    @NotNull
    @Future
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;//Date must be equal to start Date. if add day - set to 21:00 on front-end

    @Column(name = "online_link", nullable = true)
    @Pattern(
            regexp = "^(https?://)" // Протокол (http або https)
                    + "(([\\w.-]*)+\\.[a-z]{2,6})" // Доменне ім'я
                    + "(:\\d+)?(/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?$", // Порт і шлях
            message = "Invalid URL for event ink"
    )
    private String onlineLink;
}
