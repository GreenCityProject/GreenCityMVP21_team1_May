package greencity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "events")
@EqualsAndHashCode
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "timestamp"/*, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP"*/)
    private LocalDateTime timestamp;

    @NotNull(message = "Missed event title")
    @Size(min = 20, max = 70, message = "Event title must be up to 70 symbols")
    @Column(name = "title")
    private String title; // EXCLUDE SERVICE SYMBOLS ???

    @Size(min = 1, message = "Missed event date, time or location")
    @Column(name = "event_date_location_id")
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EventDateLocation> dates = new ArrayList<>();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @NotNull(message = "Event description must be less than 63 206 symbols")
    @Size(min = 20, max = 63206, message = "Missed event date, time or location")
    @Column(name = "description")
    private String description; //less than 63 206 symbols. EXCLUDE SERVICE SYMBOLS ???

    @Column(name = "is_open")
    private Boolean isOpen = true;

    @Column(name = "title_image")
    private String titleImage = "default image"; //up to 10MB and have JPG or PNG format.

    @Column(name = "additional_images_id")
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalImage> additionalImages = new ArrayList<>(); //up to 10MB and have JPG or PNG format.

}
/**
 * {    "title":"ас події, мету збору тощо. Ви можете повернутися т",
 * "description":"<p><span style=\"background-color: rgb(247, 249, 250); color: rgb(68, 78, 85);\">ас події, мету збору тощо. Ви можете повернутися тас події, мету збору тощо. Ви можете повернутися тас події, мету збору тощо. Ви можете повернутися т</span></p>",
 * "open":true,
 * "datesLocations":
 * [{
 * "startDate":"2024-06-02T14:00:40+03:00",
 * "finishDate":"2024-06-02T23:59:40+03:00",
 * "coordinates":{"latitude":50.3741511,"longitude":30.37375590000001}
 * }],
 * "tags":["Social"]
 * }
 **/