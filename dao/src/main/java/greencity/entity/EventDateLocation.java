package greencity.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "event_date_locations")
@EqualsAndHashCode(exclude = {"id", "event"})
@ToString(exclude = {"id", "event"})
public class EventDateLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    @Future(message = "Start date and time must be in the future")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull
    @Future(message = "End date and time must be in the future")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "online_link", nullable = true)
    @URL(message = "Invalid URL for event link")
    private String onlineLink;

    @PrePersist
    public void throwIfStartTimeIsBeforeEnd() {
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }
}
