package greencity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "addresses")
@EqualsAndHashCode(exclude = {"id", "eventDateLocation", "countryEn", "countryUa", "regionEn", "regionUa", "cityEn",
        "cityUa", "streetEn", "streetUa", "houseNumber", "formattedAddressEn", "formattedAddressUa"})
@ToString(exclude = {"id", "eventDateLocation"})
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "address")
    private EventDateLocation eventDateLocation;

    private BigDecimal latitude;
    private BigDecimal longitude;
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

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude != null ? latitude.setScale(6, RoundingMode.HALF_UP) : null;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude != null ? longitude.setScale(6, RoundingMode.HALF_UP) : null;
    }

}
