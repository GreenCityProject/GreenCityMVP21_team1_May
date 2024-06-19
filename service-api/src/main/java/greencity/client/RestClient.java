package greencity.client;

import com.google.gson.Gson;
import greencity.constant.RestTemplateLinks;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.econews.EcoNewsForSendEmailDto;
import greencity.dto.user.*;
import greencity.enums.EmailNotification;
import greencity.enums.Role;
import greencity.message.SendHabitNotification;
import greencity.message.SendReportEmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RestClient {
    private final RestTemplate restTemplate;
    @Setter
    @Value("${greencityuser.server.address}")
    private String greenCityUserServerAddress;

    /**
     * Method for getting all users by their {@link EmailNotification}.
     *
     * @param emailNotification enum with {@link EmailNotification} value.
     * @return {@link List} of {@link UserVO}.
     * @author Taras Kavkalo
     */
    public List<UserVO> findAllByEmailNotification(EmailNotification emailNotification) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<List<UserVO>> exchange = restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_ALL_BY_EMAIL_NOTIFICATION
            + RestTemplateLinks.EMAIL_NOTIFICATION + emailNotification,
            HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
            });
        return exchange.getBody();
    }

    /**
     * Method that find all users cities.
     *
     * @return {@link List} of cities.
     * @author Taras Kavkalo
     */
    public List<String> findAllUsersCities() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<List<String>> exchange = restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.FIND_ALL_USERS_CITIES,
            HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
            });
        return exchange.getBody();
    }

    /**
     * Method that find all registration months.
     *
     * @return {@link Map} with months.
     * @author Taras Kavkalo
     */
    public Map<Integer, Long> findAllRegistrationMonthsMap() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Map<Integer, Long>> exchange = restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.FIND_ALL_REGISTRATION_MONTHS_MAP,
            HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
            });
        return exchange.getBody();
    }

    /**
     * Method find user by principal.
     *
     * @param email of {@link UserVO}
     * @author Orest Mamchuk
     */
    public UserVO findByEmail(String email) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_BY_EMAIL).queryParam("email", email);
        return restTemplate.exchange(url.toUriString(), HttpMethod.GET,
            entity, UserVO.class).getBody();
    }

    /**
     * Method that allow you to find {@link UserVO} by Id.
     *
     * @param id a value of {@link Long}
     * @return {@link UserVO}
     * @author Orest Mamchuk
     */
    public UserVO findById(Long id) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        return restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_BY_ID + RestTemplateLinks.ID + id, HttpMethod.GET, entity, UserVO.class)
            .getBody();
    }

    /**
     * Find {@link UserVO} for management by page .
     *
     * @param pageable a value with pageable configuration.
     * @return a dto of {@link PageableAdvancedDto}.
     * @author Orest Mamchuk
     */
    public PageableAdvancedDto<UserManagementDto> findUserForManagementByPage(Pageable pageable) {
        Sort sort = pageable.getSort();
        StringBuilder orderUrl = new StringBuilder();
        if (!sort.isEmpty()) {
            for (Sort.Order order : sort) {
                orderUrl.append(orderUrl).append(order.getProperty()).append(",").append(order.getDirection());
            }
        }
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        return restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_USER_FOR_MANAGEMENT + RestTemplateLinks.PAGE + pageable
                .getPageNumber()
            + RestTemplateLinks.SIZE + pageable
                .getPageSize()
            + RestTemplateLinks.SORT + orderUrl,
            HttpMethod.GET, entity,
            new ParameterizedTypeReference<PageableAdvancedDto<UserManagementDto>>() {
            }).getBody();
    }

    /**
     * Method for getting UserVO by search query.
     *
     * @param pageable {@link Pageable}.
     * @param query    query to search
     * @return {@link PageableAdvancedDto} of {@link UserManagementDto} instances.
     * @author Orest Mamchuk
     */
    public PageableAdvancedDto<UserManagementDto> searchBy(Pageable pageable, String query) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(greenCityUserServerAddress
            + RestTemplateLinks.SEARCH_BY)
            .queryParam("page", pageable.getPageNumber())
            .queryParam("size", pageable.getPageSize())
            .queryParam("query", query);
        return restTemplate.exchange(url.toUriString(), HttpMethod.GET, entity,
            new ParameterizedTypeReference<PageableAdvancedDto<UserManagementDto>>() {
            }).getBody();
    }

    /**
     * Method for getting UserVO by search query.
     *
     * @param userDto dto with updated fields.
     * @author Orest Mamchuk
     */
    public void updateUser(UserManagementDto userDto) {
        UserManagementUpdateDto updateDto = managementDtoToUpdateDto(userDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserManagementUpdateDto> entity = new HttpEntity<>(updateDto, headers);
        restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER + "/" + userDto.getId(), HttpMethod.PUT, entity, Object.class);
    }

    private UserManagementUpdateDto managementDtoToUpdateDto(UserManagementDto userDto) {
        return UserManagementUpdateDto.builder()
            .name(userDto.getName())
            .email(userDto.getEmail())
            .userCredo(userDto.getUserCredo())
            .role(userDto.getRole())
            .userStatus(userDto.getUserStatus())
            .build();
    }

    /**
     * Method for sending change role request.
     *
     * @param id   of user whose role is being changed
     * @param role new role
     */
    public void updateRole(Long id, Role role) {
        String url = greenCityUserServerAddress
            + RestTemplateLinks.USER + "/" + id + "/role";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserRoleDto userRoleDto = new UserRoleDto(role);
        HttpEntity<UserRoleDto> entity = new HttpEntity<>(userRoleDto, headers);
        restTemplate.exchange(url, HttpMethod.PATCH, entity, Object.class);
    }

    /**
     * Method for getting all Users.
     *
     * @return {@link List} of {@link UserVO} instances.
     * @author Orest Mamchuk
     */
    public List<UserVO> findAll() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<UserVO[]> exchange = restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_ALL, HttpMethod.GET, entity, UserVO[].class);
        UserVO[] responseDtos = exchange.getBody();
        assert responseDtos != null;
        return Arrays.asList(responseDtos);
    }

    /**
     * Method that allow you to find not 'DEACTIVATED' {@link UserVO} by email.
     *
     * @param email - {@link UserVO}'s email
     * @return {@link UserVO}
     * @author Orest Mamchuk
     */
    public Optional<UserVO> findNotDeactivatedByEmail(String email) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        UserVO body = restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_NOT_DEACTIVATED_BY_EMAIL + RestTemplateLinks.EMAIL
            + email, HttpMethod.GET, entity, UserVO.class)
            .getBody();
        assert body != null;
        return Optional.of(body);
    }

    /**
     * Method find user id by email.
     *
     * @param email of {@link UserVO}
     * @author Orest Mamchuk
     */
    public Long findIdByEmail(String email) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_ID_BY_EMAIL).queryParam("email", email);
        return restTemplate.exchange(url.toUriString(), HttpMethod.GET, entity, Long.class).getBody();
    }

    /**
     * Method for setting {@link UserVO}'s status to DEACTIVATED, so the user will
     * not be able to log in into the system.
     *
     * @param userId      - {@link UserVO}'s id
     * @param userReasons {@link List} of {@link String}.
     * @author Orest Mamchuk
     */
    public void deactivateUser(Long userId, List<String> userReasons) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> entity = new HttpEntity<>(userReasons, headers);
        restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.USER_DEACTIVATE
            + RestTemplateLinks.ID + userId, HttpMethod.PUT, entity, Object.class);
    }

    /**
     * Method for getting {@link String} user language.
     *
     * @param userId of the searched {@link UserVO}.
     * @return current user language {@link String}.
     * @author Vlad Pikhotskyi
     */
    public String getUserLang(Long userId) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        String body = restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.USER_LANG
            + RestTemplateLinks.ID + userId, HttpMethod.GET, entity, String.class).getBody();
        assert body != null;
        return body;
    }

    /**
     * Method for setting {@link UserVO}'s status to ACTIVATED.
     *
     * @param userId - {@link UserVO}'s id
     * @author Orest Mamchuk
     */
    public void setActivatedStatus(Long userId) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.USER_ACTIVATE
            + RestTemplateLinks.ID + userId, HttpMethod.PUT, entity, Object.class);
    }

    /**
     * Method for getting a {@link List} of {@link String} - reasons for
     * deactivation of the current user.
     *
     * @param userId    {@link Long} - user's id.
     * @param adminLang {@link String} - current administrator language.
     * @return {@link List} of {@link String} - reasons for deactivation of the
     *         current user.
     * @author Vlad Pikhotskyi
     */
    public List<String> getDeactivationReason(Long userId, String adminLang) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        String[] reasonDtos = restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.USER_REASONS
            + RestTemplateLinks.ID + userId
            + RestTemplateLinks.ADMIN_LANG + adminLang, HttpMethod.GET, entity, String[].class).getBody();
        assert reasonDtos != null;
        return Arrays.asList(reasonDtos);
    }

    /**
     * Method for setting to a list of {@link UserVO} status DEACTIVATED, so the
     * users will not be able to log in into the system.
     *
     * @param listId {@link List} populated with ids of {@link UserVO} to be
     *               deleted.
     * @author Orest Mamchuk
     */
    public void deactivateAllUsers(List<Long> listId) {
        Gson gson = new Gson();
        String json = gson.toJson(listId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_DEACTIVATE
            + RestTemplateLinks.ID + listId, HttpMethod.PUT, entity, Long[].class);
    }

    /**
     * Register new user from admin panel.
     *
     * @param userDto dto with updated fields.
     * @author Orest Mamchuk
     */
    public void managementRegisterUser(UserManagementDto userDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserManagementDto> entity = new HttpEntity<>(userDto, headers);
        restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.OWN_SECURITY_REGISTER, HttpMethod.POST, entity, Object.class);
    }

    /**
     * send AddEcoNewsMessage to GreenCityUser.
     *
     * @param message with information for sending email about adding new eco news.
     * @author Taras Kavkalo
     */
    public void addEcoNews(EcoNewsForSendEmailDto message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EcoNewsForSendEmailDto> entity = new HttpEntity<>(message, headers);
        restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.ADD_ECO_NEWS, HttpMethod.POST, entity, Object.class)
            .getBody();
    }

    /**
     * send SendReportEmailMessage to GreenCityUser.
     *
     * @param reportEmailMessage with information for sending email report about new
     *                           places.
     * @author Taras Kavkalo
     */
    public void sendReport(SendReportEmailMessage reportEmailMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<SendReportEmailMessage> entity = new HttpEntity<>(reportEmailMessage, headers);
        restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.SEND_REPORT, HttpMethod.POST, entity, Object.class)
            .getBody();
    }

    /**
     * Delete from the database users that have status 'DEACTIVATED' and last
     * visited the site 2 years ago.
     *
     * @author Taras Kavkalo
     */
    public void scheduleDeleteDeactivatedUsers() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.DELETE_DEACTIVATED_USERS,
            HttpMethod.POST, entity, Object.class).getBody();
    }

    /**
     * send SendHabitNotification to GreenCityUser.
     *
     * @param sendHabitNotification with information for sending email to each user
     *                              that hasn't marked any habit during some period.
     * @author Taras Kavkalo
     */
    public void sendHabitNotification(SendHabitNotification sendHabitNotification) {
        HttpEntity<SendHabitNotification> entity = new HttpEntity<>(sendHabitNotification, new HttpHeaders());
        restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.SEND_HABIT_NOTIFICATION, HttpMethod.POST, entity, Object.class)
            .getBody();
    }

    /**
     * Method that allow you to save new {@link UserVO}.
     *
     * @param userVO for save User.
     * @author Orest Mamchuk
     */
    public void save(UserVO userVO) {
        HttpEntity<UserVO> entity = new HttpEntity<>(userVO, new HttpHeaders());
        restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER, HttpMethod.POST, entity, Object.class)
            .getBody();
    }

    /**
     * Method that allow you to search users by several values
     * {@link UserManagementViewDto}.
     *
     * @param pageable    {@link Pageable}.
     * @param userViewDto for search User.
     * @return a dto of {@link PageableAdvancedDto}.
     */
    public PageableAdvancedDto<UserManagementVO> search(Pageable pageable, UserManagementViewDto userViewDto) {
        Sort sort = pageable.getSort();
        StringBuilder orderUrl = new StringBuilder();
        if (!sort.isEmpty()) {
            for (Sort.Order order : sort) {
                orderUrl.append(orderUrl).append(order.getProperty()).append(",").append(order.getDirection());
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserManagementViewDto> entity = new HttpEntity<>(userViewDto, headers);
        return restTemplate.exchange(
            greenCityUserServerAddress + RestTemplateLinks.USER_SEARCH + RestTemplateLinks.PAGE
                + pageable.getPageNumber()
                + RestTemplateLinks.SIZE + pageable.getPageSize()
                + RestTemplateLinks.SORT + orderUrl,
            HttpMethod.POST, entity,
            new ParameterizedTypeReference<PageableAdvancedDto<UserManagementVO>>() {
            }).getBody();
    }
}
