package greencity.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AddressDtoRequest {

    @NotNull(message = "latitude must not be NULL")
    private BigDecimal latitude;

    @NotNull(message = "longitude must not be NULL")

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
