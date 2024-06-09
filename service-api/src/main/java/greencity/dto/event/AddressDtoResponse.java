package greencity.dto.event;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AddressDtoResponse {
    private Long id;
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
}
