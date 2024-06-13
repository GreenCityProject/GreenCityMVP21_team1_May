package greencity.entity;

import greencity.annotations.ValidStartBeforeEndDates;
import jakarta.persistence.*;
import lombok.*;

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
@ValidStartBeforeEndDates
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

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "online_link", nullable = true)
    private String onlineLink;

}
