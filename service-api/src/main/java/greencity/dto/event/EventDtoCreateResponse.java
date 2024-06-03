package greencity.dto.event;

import greencity.dto.user.UserForEventDtoResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class EventDtoCreateResponse {
    private Long id;
    private LocalDateTime timestamp;
    private String title;
    private List<EventDateLocationDtoResponse> dates;
    private UserForEventDtoResponse organizer;
    private String description;
    private Boolean isOpen;
    private String titleImage;
    private List<AdditionalImageForEventDtoResponse> additionalImages;
}
/**
 * {
 *  "id": 2951,
 *  "title": "ЗаголовокЗаголовок",
 *  "organizer": {
 *      "id": 1859,
 *      "name": "Hotih",
 *      "organizerRating": null,
 *      "email": "hotihi4256@hutov.com"
 *      },
 *  "creationDate": "2024-06-02",
 *  "description": "<p>ЗаголовокЗаголовокЗаголовокЗаголовокЗаголовок</p>",
 *  "dates": [
 *      {
 *      "startDate": "2024-06-02T14:00:10Z",
 *      "finishDate": "2024-06-02T20:59:10Z",
 *      "onlineLink": null,
 *      "id": null,
 *      "event": null,
 *      "coordinates": {
 *          "latitude": 50.36803339999999,
 *          "longitude": 30.540602,
 *          "streetEn": "Kytaivska Street",
 *          "streetUa": "вулиця Китаївська",
 *          "houseNumber": "32",
 *          "cityEn": "Kyiv",
 *          "cityUa": "Київ",
 *          "regionEn": "Kyiv",
 *          "regionUa": "місто Київ",
 *          "countryEn": "Ukraine",
 *          "countryUa": "Україна",
 *          "formattedAddressEn": "Kytaivska St, 32, Kyiv, Ukraine, 02000",
 *          "formattedAddressUa": "вулиця Китаївська, 32, Київ, Україна, 02000"
 *          }
 *      }
 *      ],
 *  "tags": [
 *      {
 *      "id": 13,
 *      "nameUa": "Екологічний",
 *      "nameEn": "Environmental"
 *      }
 *      ],
 *  "titleImage": "https://csb10032000a548f571.blob.core.windows.net/allfiles/6f3083e6-07b6-4019-9ac6-b2c7bfd9f007illustration-earth.png",
 *  "additionalImages": [
 *      "https://csb10032000a548f571.blob.core.windows.net/allfiles/54137420-ae88-4302-b795-702d8ade2e7cillustration-money.png",
 *      "https://csb10032000a548f571.blob.core.windows.net/allfiles/9ff9c7bb-689a-4b1a-b27e-0be5bf932520illustration-people.png"
 *      ],
 *  "isRelevant": true,
 *  "likes": 0,
 *  "countComments": 0,
 *  "eventRate": 0.0,
 *  "open": true,
 *  "isSubscribed": false,
 *  "isFavorite": false,
 *  "isOrganizedByFriend": false
 *  }
 */