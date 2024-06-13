package greencity.dto.event;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AddressDtoRequest {

    @NotNull(message = "latitude must not be NULL")
    @DecimalMax(value = "90", message = "Latitude must be from -90 to 90 degrees.")
    @DecimalMin(value = "-90", message = "Latitude must be from -90 to 90 degrees.")
    private BigDecimal latitude;

    @NotNull(message = "longitude must not be NULL")
    @DecimalMax(value = "180", message = "Longitude must be from -180 to 180 degrees.")
    @DecimalMin(value = "-180", message = "Longitude must be from -180 to 180 degrees.")
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
