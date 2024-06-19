package greencity.client;

import com.google.gson.Gson;
import greencity.ModelUtils;
import greencity.constant.RestTemplateLinks;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.econews.EcoNewsForSendEmailDto;
import greencity.dto.user.*;
import greencity.enums.EmailNotification;
import greencity.message.SendHabitNotification;
import greencity.message.SendReportEmailMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static greencity.enums.Role.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestClientTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private java.lang.Object Object;
    @Value("${greencityuser.server.address}")
    private String greenCityUserServerAddress;
    @InjectMocks
    private RestClient restClient;

    @Test
    void findByEmail() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        UserVO userVO = ModelUtils.getUserVO();
        RestClient restClient = new RestClient(restTemplate);
        restClient.setGreenCityUserServerAddress("https://www.greencity.com.ua");
        when(restTemplate.exchange("https://www.greencity.com.ua" + RestTemplateLinks.USER_FIND_BY_EMAIL
            + RestTemplateLinks.EMAIL + "taras@gmail.com", HttpMethod.GET,
            entity, UserVO.class)).thenReturn(ResponseEntity.ok(userVO));

        assertEquals(userVO, restClient.findByEmail("taras@gmail.com"));
    }

    @Test
    void findById() {
        UserVO userVO = ModelUtils.getUserVO();
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_BY_ID + RestTemplateLinks.ID + 1L, HttpMethod.GET, entity, UserVO.class))
                .thenReturn(ResponseEntity.ok(userVO));
        assertEquals(userVO, restClient.findById(1L));
    }

    @Test
    void searchBy() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        String query = "Search";
        Pageable pageable = PageRequest.of(0, 10);
        List<UserManagementDto> ecoNewsDtos = Collections.singletonList(new UserManagementDto());
        PageableAdvancedDto<UserManagementDto> pageableAdvancedDto =
            new PageableAdvancedDto<>(ecoNewsDtos, 2, 0, 3, 0, true, true, true, true);
        RestClient restClient = new RestClient(restTemplate);
        restClient.setGreenCityUserServerAddress("https://www.greencity.com.ua");
        when(restTemplate.exchange("https://www.greencity.com.ua"
            + RestTemplateLinks.SEARCH_BY + RestTemplateLinks.PAGE + pageable.getPageNumber()
            + RestTemplateLinks.SIZE + pageable.getPageSize()
            + RestTemplateLinks.QUERY + query, HttpMethod.GET, entity,
            new ParameterizedTypeReference<PageableAdvancedDto<UserManagementDto>>() {
            })).thenReturn(ResponseEntity.ok(pageableAdvancedDto));
        assertEquals(pageableAdvancedDto, restClient.searchBy(pageable, query));
    }

    @Test
    void updateUser() {
        // given
        UserManagementDto userManagementDto = new UserManagementDto();
        UserManagementUpdateDto userManagementUpdateDto = new UserManagementUpdateDto();
        userManagementDto.setId(1L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserManagementUpdateDto> entity = new HttpEntity<>(userManagementUpdateDto, headers);
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER + "/1", HttpMethod.PUT, entity, Object.class))
                .thenReturn(ResponseEntity.ok(Object));
        // when
        restClient.updateUser(userManagementDto);
        // then
        assertEquals(ResponseEntity.ok(Object), restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER + "/1", HttpMethod.PUT, entity, Object.class));
    }

    @Test
    void updateRole() {
        // given
        UserRoleDto userRoleDto = new UserRoleDto(ROLE_ADMIN);
        String url = greenCityUserServerAddress
            + RestTemplateLinks.USER + "/1/role";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRoleDto> entity = new HttpEntity<>(userRoleDto, headers);

        // when
        restClient.updateRole(1L, ROLE_ADMIN);

        // then
        verify(restTemplate).exchange(url, HttpMethod.PATCH, entity, Object.class);
    }

    @Test
    void findAll() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        UserVO userVO = ModelUtils.getUserVO();
        UserVO[] userVOS = new UserVO[] {userVO};
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_ALL, HttpMethod.GET, entity, UserVO[].class))
                .thenReturn(ResponseEntity.of(Optional.of(userVOS)));

        assertEquals(Arrays.asList(userVOS), restClient.findAll());
    }

    @Test
    void findNotDeactivatedByEmail() {
        String email = "test@gmail.com";
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        UserVO userVO = ModelUtils.getUserVO();
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_NOT_DEACTIVATED_BY_EMAIL + RestTemplateLinks.EMAIL
            + email, HttpMethod.GET, entity, UserVO.class)).thenReturn(ResponseEntity.ok(userVO));

        assertEquals(Optional.of(userVO), restClient.findNotDeactivatedByEmail(email));
    }

    @Test
    void findNotDeactivatedByEmailFromCookies() {
        String email = "test@gmail.com";
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        UserVO userVO = ModelUtils.getUserVO();
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_NOT_DEACTIVATED_BY_EMAIL + RestTemplateLinks.EMAIL
            + email, HttpMethod.GET, entity, UserVO.class)).thenReturn(ResponseEntity.ok(userVO));

        assertEquals(Optional.of(userVO), restClient.findNotDeactivatedByEmail(email));
    }

    @Test
    void findIdByEmail() {
        String email = "test@gmail.com";
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        RestClient restClient = new RestClient(restTemplate);
        restClient.setGreenCityUserServerAddress("https://www.greencity.com.ua");
        when(restTemplate.exchange("https://www.greencity.com.ua"
            + RestTemplateLinks.USER_FIND_ID_BY_EMAIL
            + RestTemplateLinks.EMAIL + email, HttpMethod.GET, entity, Long.class))
                .thenReturn(ResponseEntity.ok(1L));

        assertEquals(1L, restClient.findIdByEmail(email));
    }

    @Test
    void getDeactivationReason() {
        String[] test = new String[] {"test", "test"};
        List<String> listString = Arrays.asList(test);
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        when(restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.USER_REASONS
            + RestTemplateLinks.ID + 1L
            + RestTemplateLinks.ADMIN_LANG + "en", HttpMethod.GET, entity, String[].class))
                .thenReturn(ResponseEntity.ok(test));
        assertEquals(listString, restClient.getDeactivationReason(1L, "en"));
    }

    @Test
    void getUserLang() {
        String test = "test";
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        when(restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.USER_LANG
            + RestTemplateLinks.ID + 1L, HttpMethod.GET, entity, String.class))
                .thenReturn(ResponseEntity.ok(test));
        assertEquals(test, restClient.getUserLang(1L));
    }

    @Test
    void deactivateUser() {
        List<String> test = List.of("test", "test");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> entity = new HttpEntity<>(test, headers);
        when(restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.USER_DEACTIVATE
            + RestTemplateLinks.ID + 1L, HttpMethod.PUT, entity, Object.class))
                .thenReturn(ResponseEntity.ok(Object));
        restClient.deactivateUser(1L, test);
        verify(restTemplate).exchange(greenCityUserServerAddress + RestTemplateLinks.USER_DEACTIVATE
            + RestTemplateLinks.ID + 1L, HttpMethod.PUT, entity, Object.class);
    }

    @Test
    void setActivatedStatus() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        when(restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.USER_ACTIVATE
            + RestTemplateLinks.ID + 1L, HttpMethod.PUT, entity, Object.class))
                .thenReturn(ResponseEntity.ok(Object));
        restClient.setActivatedStatus(1L);
        verify(restTemplate).exchange(greenCityUserServerAddress + RestTemplateLinks.USER_ACTIVATE
            + RestTemplateLinks.ID + 1L, HttpMethod.PUT, entity, Object.class);
    }

    @Test
    void deactivateAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Long[] longs = new Long[] {1L, 2L};
        List<Long> listId = Arrays.asList(longs);
        Gson gson = new Gson();
        String json = gson.toJson(listId);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        restClient.deactivateAllUsers(listId);

        verify(restTemplate).exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_DEACTIVATE
            + RestTemplateLinks.ID + listId, HttpMethod.PUT, entity, Long[].class);

    }

    @Test
    void managementRegisterUser() {
        // given
        UserManagementDto userManagementDto = new UserManagementDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserManagementDto> entity = new HttpEntity<>(userManagementDto, headers);
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.OWN_SECURITY_REGISTER, HttpMethod.POST, entity, Object.class))
                .thenReturn(ResponseEntity.ok(Object));

        // when
        restClient.managementRegisterUser(userManagementDto);

        // then
        verify(restTemplate).exchange(greenCityUserServerAddress
            + RestTemplateLinks.OWN_SECURITY_REGISTER, HttpMethod.POST, entity, Object.class);
    }

    @Test
    void addEcoNews() {
        EcoNewsForSendEmailDto message = ModelUtils.getEcoNewsForSendEmailDto();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EcoNewsForSendEmailDto> entity = new HttpEntity<>(message, httpHeaders);
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.ADD_ECO_NEWS, HttpMethod.POST, entity, Object.class))
                .thenReturn(ResponseEntity.ok(Object));
        restClient.addEcoNews(message);

        verify(restTemplate).exchange(greenCityUserServerAddress
            + RestTemplateLinks.ADD_ECO_NEWS, HttpMethod.POST, entity, Object.class);
    }

    @Test
    void sendReport() {
        SendReportEmailMessage message = ModelUtils.getSendReportEmailMessage();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<SendReportEmailMessage> entity = new HttpEntity<>(message, headers);
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.SEND_REPORT, HttpMethod.POST, entity, Object.class))
                .thenReturn(ResponseEntity.ok(Object));
        restClient.sendReport(message);

        verify(restTemplate).exchange(greenCityUserServerAddress
            + RestTemplateLinks.SEND_REPORT, HttpMethod.POST, entity, Object.class);
    }

    @Test
    void sendHabitNotification() {
        SendHabitNotification notification = ModelUtils.getSendHabitNotification();
        HttpEntity<SendHabitNotification> entity = new HttpEntity<>(notification, new HttpHeaders());
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.SEND_HABIT_NOTIFICATION, HttpMethod.POST, entity, Object.class))
                .thenReturn(ResponseEntity.ok(Object));
        restClient.sendHabitNotification(notification);

        verify(restTemplate).exchange(greenCityUserServerAddress
            + RestTemplateLinks.SEND_HABIT_NOTIFICATION, HttpMethod.POST, entity, Object.class);
    }

    @Test
    void save() {
        UserVO userVO = ModelUtils.getUserVO();
        HttpEntity<UserVO> entity = new HttpEntity<>(userVO, new HttpHeaders());
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER, HttpMethod.POST, entity, Object.class)).thenReturn(ResponseEntity.ok(Object));
        restClient.save(userVO);

        verify(restTemplate).exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER, HttpMethod.POST, entity, Object.class);
    }

    @Test
    void saveTest() {
        UserVO userVO = ModelUtils.getUserVO();
        HttpEntity<UserVO> entity = new HttpEntity<>(userVO, new HttpHeaders());
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER, HttpMethod.POST, entity, Object.class)).thenReturn(ResponseEntity.ok(Object));
        restClient.save(userVO);

        verify(restTemplate).exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER, HttpMethod.POST, entity, Object.class);

    }

    @Test
    void findUserForManagementByPage() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        List<UserManagementDto> ecoNewsDtos = Collections.singletonList(new UserManagementDto());
        PageableAdvancedDto<UserManagementDto> pageableAdvancedDto =
            new PageableAdvancedDto<>(ecoNewsDtos, 2, 0, 3, 0, true, true, true, true);
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_USER_FOR_MANAGEMENT + RestTemplateLinks.PAGE + pageable.getPageNumber()
            + RestTemplateLinks.SIZE + pageable.getPageSize() + "&sort=id,ASC", HttpMethod.GET, entity,
            new ParameterizedTypeReference<PageableAdvancedDto<UserManagementDto>>() {
            })).thenReturn(ResponseEntity.ok(pageableAdvancedDto));
        assertEquals(pageableAdvancedDto, restClient.findUserForManagementByPage(pageable));
    }

    @Test
    void searchTest() {
        Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserManagementViewDto userViewDto =
            UserManagementViewDto.builder()
                .id("1L")
                .name("vivo")
                .email("test@ukr.net")
                .userCredo("Hello")
                .role("1")
                .userStatus("1")
                .build();
        List<UserManagementVO> userManagementVOS = Collections.singletonList(new UserManagementVO());
        PageableAdvancedDto<UserManagementVO> userAdvancedDto =
            new PageableAdvancedDto<>(userManagementVOS, 20, 0, 0, 0,
                true, true, true, true);
        HttpEntity<UserManagementViewDto> entity = new HttpEntity<>(userViewDto, headers);
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_SEARCH + RestTemplateLinks.PAGE + pageable.getPageNumber()
            + RestTemplateLinks.SIZE + pageable.getPageSize()
            + RestTemplateLinks.SORT, HttpMethod.POST, entity,
            new ParameterizedTypeReference<PageableAdvancedDto<UserManagementVO>>() {
            })).thenReturn(ResponseEntity.ok(userAdvancedDto));
        assertEquals(userAdvancedDto, restClient.search(pageable, userViewDto));
    }

    @Test
    void scheduleDeleteDeactivatedUsers() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        when(restTemplate.exchange(greenCityUserServerAddress + RestTemplateLinks.DELETE_DEACTIVATED_USERS,
            HttpMethod.POST, entity, Object.class))
                .thenReturn(ResponseEntity.ok(Object));
        restClient.scheduleDeleteDeactivatedUsers();
        verify(restTemplate, times(1)).exchange(greenCityUserServerAddress + RestTemplateLinks.DELETE_DEACTIVATED_USERS,
            HttpMethod.POST, entity, Object.class);
    }

    @Test
    void findAllByEmailNotification() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        List<UserVO> userVOS = Collections.singletonList(ModelUtils.getUserVO());
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.USER_FIND_ALL_BY_EMAIL_NOTIFICATION
            + RestTemplateLinks.EMAIL_NOTIFICATION + EmailNotification.IMMEDIATELY,
            HttpMethod.GET, entity, new ParameterizedTypeReference<List<UserVO>>() {
            }))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(userVOS));

        assertEquals(userVOS, restClient.findAllByEmailNotification(EmailNotification.IMMEDIATELY));
    }

    @Test
    void findAllRegistrationMonthsMap() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        Map<Integer, Long> expected = Collections.singletonMap(1, 1L);
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.FIND_ALL_REGISTRATION_MONTHS_MAP,
            HttpMethod.GET, entity, new ParameterizedTypeReference<Map<Integer, Long>>() {
            })).thenReturn(ResponseEntity.status(HttpStatus.OK).body(expected));

        assertEquals(expected, restClient.findAllRegistrationMonthsMap());
    }

    @Test
    void findAllUsersCities() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        List<String> expected = Collections.singletonList("text");
        when(restTemplate.exchange(greenCityUserServerAddress
            + RestTemplateLinks.FIND_ALL_USERS_CITIES,
            HttpMethod.GET, entity, new ParameterizedTypeReference<List<String>>() {
            }))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(expected));
        assertEquals(expected, restClient.findAllUsersCities());
    }
}
