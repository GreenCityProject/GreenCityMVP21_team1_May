package greencity.entity;

import jakarta.persistence.*;
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
@EqualsAndHashCode(exclude = {"id", "timestamp", "organizer", "isOpen", "titleImage", "additionalImages"})
@ToString(exclude = {"id", "timestamp", "organizer", "titleImage", "additionalImages"})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "title")
    private String title;

    @Column(name = "event_date_location_id")
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventDateLocation> dates = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @Column(name = "description")
    private String description;

    @Column(name = "is_open")
    private Boolean isOpen = true;

    @Column(name = "title_image")
    private String titleImage = "default image";

    @Column(name = "additional_images_id")
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalImage> additionalImages = new ArrayList<>();

    public void setDates(@Size(min = 1, max = 7, message = "Must add from 1 to 7 sets of date, time and location parameters") List<EventDateLocation> dates) {
        if (this.dates != null) {
            this.dates.clear();
            this.dates.addAll(dates);
        } else {
            this.dates = dates;
        }
    }

    public void setAdditionalImages(List<AdditionalImage> additionalImages) {
        if (this.additionalImages != null) {
            this.additionalImages.clear();
            this.additionalImages.addAll(additionalImages);
        } else {
            this.additionalImages = additionalImages;
        }
    }
}
