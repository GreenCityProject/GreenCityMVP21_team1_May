package greencity.dto.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EventDtoRequest {

    @NotEmpty
    private String title;

    @Size(min = 1)
    private List<EventDateLocationDtoResponse> dates = new ArrayList<>();

//    private UserForEventDtoResponse organizer;

    @NotEmpty
    private String description;

    @NotNull
    private Boolean isOpen;


    @NotEmpty
    private String titleImage;


    private List<AdditionalImageForEventDtoResponse> additionalImages = new ArrayList<>();


}
/**
 * {
 * "title": "ЗаголовокЗаголовок",
 * "description": "<p>ЗаголовокЗаголовокЗаголовокЗаголовокЗаголовок</p>",
 * "dates": [
 * {
 * "startDate": "2024-06-02T14:00:10Z",
 * "finishDate": "2024-06-02T20:59:10Z",
 * "onlineLink": null,
 * "coordinates": {
 * "latitude": 50.36803339999999,
 * "longitude": 30.540602,
 * "streetEn": "Kytaivska Street",
 * "streetUa": "вулиця Китаївська",
 * "houseNumber": "32",
 * "cityEn": "Kyiv",
 * "cityUa": "Київ",
 * "regionEn": "Kyiv",
 * "regionUa": "місто Київ",
 * "countryEn": "Ukraine",
 * "countryUa": "Україна",
 * "formattedAddressEn": "Kytaivska St, 32, Kyiv, Ukraine, 02000",
 * "formattedAddressUa": "вулиця Китаївська, 32, Київ, Україна, 02000"
 * }
 * }
 * ],
 * "titleImage": "https://csb10032000a548f571.blob.core.windows.net/allfiles/6f3083e6-07b6-4019-9ac6-b2c7bfd9f007illustration-earth.png",
 * "additionalImages": [
 * "im1",
 * "im2"
 * ],
 * "open": true,
 * }
 */