package greencity.mapping;

import greencity.dto.user.UserVO;
import greencity.entity.OwnSecurity;
import greencity.entity.User;
import greencity.entity.VerifyEmail;
import greencity.enums.EmailNotification;
import greencity.enums.Role;
import greencity.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserVOMapperTest {

    @InjectMocks
    private UserVOMapper mapper;

    @Test
    @DisplayName("Test convert from User to UserVO")
    void convert() {
        User user = User.builder()
                .id(1L)
                .email("namesurname1995@gmail.com")
                .name("user")
                .role(Role.ROLE_USER)
                .userCredo("save the world")
                .firstName("name")
                .emailNotification(EmailNotification.MONTHLY)
                .userStatus(UserStatus.ACTIVATED)
                .rating(13.4)
                .verifyEmail(VerifyEmail.builder()
                        .id(32L)
                        .user(User.builder()
                                .id(1L)
                                .name("user")
                                .build())
                        .expiryDate(LocalDateTime.of(2021, 7, 7, 7, 7))
                        .token("toooookkkeeeeen42324532542")
                        .build())
                .refreshTokenKey("refreshtoooookkkeeeeen42324532542")
                .ownSecurity(OwnSecurity.builder()
                        .id(1L)
                        .password("password")
                        .user(User.builder()
                                .id(1L)
                                .email("namesurname1995@gmail.com")
                                .build())
                        .build())
                .dateOfRegistration(LocalDateTime.of(2020, 6, 6, 13, 47))
                .profilePicturePath("path/to/profile/picture")
                .city("Lviv")
                .showShoppingList(true)
                .showEcoPlace(true)
                .showLocation(true)
                .build();;

        UserVO result = mapper.convert(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole(), result.getRole());
        assertEquals(user.getUserCredo(), result.getUserCredo());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getEmailNotification(), result.getEmailNotification());
        assertEquals(user.getUserStatus(), result.getUserStatus());
        assertEquals(user.getRating(), result.getRating());

        if (user.getVerifyEmail() != null) {
            assertNotNull(result.getVerifyEmail());
            assertEquals(user.getVerifyEmail().getId(), result.getVerifyEmail().getId());
            assertEquals(user.getVerifyEmail().getUser().getId(), result.getVerifyEmail().getUser().getId());
            assertEquals(user.getVerifyEmail().getUser().getName(), result.getVerifyEmail().getUser().getName());
            assertEquals(user.getVerifyEmail().getExpiryDate(), result.getVerifyEmail().getExpiryDate());
            assertEquals(user.getVerifyEmail().getToken(), result.getVerifyEmail().getToken());
        } else {
            assertNull(result.getVerifyEmail());
        }

        assertEquals(user.getRefreshTokenKey(), result.getRefreshTokenKey());

        if (user.getOwnSecurity() != null) {
            assertNotNull(result.getOwnSecurity());
            assertEquals(user.getOwnSecurity().getId(), result.getOwnSecurity().getId());
            assertEquals(user.getOwnSecurity().getPassword(), result.getOwnSecurity().getPassword());
            assertEquals(user.getOwnSecurity().getUser().getId(), result.getOwnSecurity().getUser().getId());
            assertEquals(user.getOwnSecurity().getUser().getEmail(), result.getOwnSecurity().getUser().getEmail());
        } else {
            assertNull(result.getOwnSecurity());
        }

        assertEquals(user.getDateOfRegistration(), result.getDateOfRegistration());
        assertEquals(user.getProfilePicturePath(), result.getProfilePicturePath());
        assertEquals(user.getCity(), result.getCity());
        assertEquals(user.getShowShoppingList(), result.getShowShoppingList());
        assertEquals(user.getShowEcoPlace(), result.getShowEcoPlace());
        assertEquals(user.getShowLocation(), result.getShowLocation());
    }
}
