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
@Table(name = "addresses")
@EqualsAndHashCode
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_date_location_id", nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "event_date_location_id")
    private EventDateLocation eventDateLocation;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String countryEn;

    private String countryUa;

    private String regionEn;

    private String regionUa;

    private String cityEn;

    private String cityUa;

    private String streetEn;

    private String streetUa;

    private String houseNumber;

    private String formattedAddressEn;

    private String formattedAddressUa;

}
