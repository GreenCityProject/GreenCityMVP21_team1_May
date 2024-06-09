package greencity.entity;

import greencity.annotations.ValidStringLength;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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
@EqualsAndHashCode(exclude = {"id", "timestamp", "organizer", "titleImage", "additionalImages"})
@ToString(exclude = {"id", "timestamp", "organizer", "titleImage", "additionalImages"})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @NotNull(message = "Missed event title")
    @ValidStringLength(min = 20, max = 70, excludeHtml = true, message = "Event title must be from 20 to 70 symbols")
    @Column(name = "title")
    private String title;

    @Valid
    @Size(min = 1, max = 7, message = "Must add from 1 to 7 sets of date, time and location parameters")
    @Column(name = "event_date_location_id")
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventDateLocation> dates = new ArrayList<>();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @NotNull(message = "Event description must be less than 63 206 symbols")
    @ValidStringLength(min = 20, max = 63206, excludeHtml = true, message = "Event description must be from 20 to 63 206 symbols")
    @Column(name = "description")
    private String description;

    @Column(name = "is_open")
    private Boolean isOpen = true;

    @NotNull
    @Column(name = "title_image")
    private String titleImage = "default image";

    @Valid
    @Column(name = "additional_images_id")
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalImage> additionalImages = new ArrayList<>();
}
