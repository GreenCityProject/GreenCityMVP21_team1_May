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
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @NotNull(message = "Missed event title")
    @Size(min = 20, max = 70, message = "Event title must be from 20 to 70 symbols")
    @Column(name = "title")
    private String title; // EXCLUDE SERVICE SYMBOLS ??? YES!!!

    @Size(min = 1, max = 7, message = "Must add from 1 to 7 sets of date, time and location parameters")
    @Column(name = "event_date_location_id")
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventDateLocation> dates = new ArrayList<>();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @NotNull(message = "Event description must be less than 63 206 symbols")
    @Size(min = 20, max = 63206, message = "Missed event date, time or location")
    @Column(name = "description")
    private String description; //less than 63 206 symbols. EXCLUDE SERVICE SYMBOLS ??? YES!!!

    @Column(name = "is_open")
    private Boolean isOpen = true;

    @Column(name = "title_image")
    private String titleImage = "default image"; //up to 10MB and have JPG or PNG format.

    @Column(name = "additional_images_id")
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalImage> additionalImages = new ArrayList<>(); //up to 10MB and have JPG or PNG format.

}
