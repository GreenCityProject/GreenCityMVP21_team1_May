package greencity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "addresses")
@EqualsAndHashCode(exclude = {"id", "eventDateLocation"})
@ToString(exclude = {"id", "eventDateLocation"})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "address")
    private EventDateLocation eventDateLocation;

    @NotNull
    private BigDecimal latitude;

    @NotNull
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
}
