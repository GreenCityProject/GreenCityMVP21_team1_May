package greencity.controller;

import static greencity.ModelUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import greencity.GreenCityApplication;
import greencity.dto.PageableDto;
import greencity.dto.habit.AddCustomHabitDtoRequest;
import greencity.dto.habit.AddCustomHabitDtoResponse;
import greencity.dto.habit.HabitDto;
import greencity.dto.shoppinglistitem.ShoppingListItemDto;
import greencity.dto.user.UserProfilePictureDto;
import greencity.dto.user.UserVO;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.HabitService;
import greencity.service.LanguageService;
import greencity.service.TagsService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Import;

import java.util.*;

@WithMockUser(username = "user")
@WebMvcTest(HabitController.class)
@Import(CustomExceptionHandler.class)
@ContextConfiguration(classes = {GreenCityApplication.class})
class HabitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private LanguageService languageService;

    @MockBean
    private UserService userService;

    @MockBean
    private TagsService tagsService;

    @MockBean
    private HabitService habitService;

    private UserVO userVO;

    private PageableDto<HabitDto> pageableDto;

    @BeforeEach
    void setUp() {
        userVO = getUserVO();
        pageableDto = getPageableHabitDto(1, 1, 1);

        when(userService.findByEmail(anyString())).thenReturn(userVO);
        when(languageService.findAllLanguageCodes()).thenReturn(List.of("en"));
    }

    @Test
    @DisplayName("Test to get habit by ID and return habit details if exists")
    void getHabitById_HabitExists_ReturnsHabit() throws Exception {
        HabitDto habitDto = getHabitDto();

        when(habitService.getByIdAndLanguageCode(1L, "en")).thenReturn(habitDto);

        mockMvc.perform(get("/habit/{id}", 1L)
                        .locale(Locale.ENGLISH)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.image").value("someImageUrl"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.complexity").value(2));

        verify(habitService).getByIdAndLanguageCode(1L, "en");
    }

    @Test
    @DisplayName("Test to get all habits and return habits list if exists")
    void getAll_HabitsExist_ReturnsHabits() throws Exception {
        when(habitService.getAllHabitsByLanguageCode(any(), any(), eq("en"))).thenReturn(pageableDto);

        mockMvc.perform(get("/habit")
                        .locale(Locale.ENGLISH)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(habitService).getAllHabitsByLanguageCode(any(), any(), eq("en"));
    }

    @Test
    @DisplayName("Test to get shopping list items for a habit and return items if exists")
    void getShoppingListItems_HabitExists_ReturnsItems() throws Exception {
        ShoppingListItemDto item = getShoppingListItemDto(1L, "Example text", "Pending");
        List<ShoppingListItemDto> items = Collections.singletonList(item);

        when(habitService.getShoppingListForHabit(1L, "en")).thenReturn(items);

        mockMvc.perform(get("/habit/1/shopping-list")
                        .locale(Locale.ENGLISH)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].text").value("Example text"));

        verify(habitService).getShoppingListForHabit(1L, "en");
    }

    @Test
    @DisplayName("Test to get all habits by tags and language code and return habits list if exists")
    void getAllByTagsAndLanguageCode_ValidTags_ReturnsHabits() throws Exception {
        when(habitService.getAllByTagsAndLanguageCode(any(Pageable.class), anyList(), eq("en")))
                .thenReturn(pageableDto);

        mockMvc.perform(get("/habit/tags/search")
                        .param("tags", "tag1", "tag2")
                        .param("page", "0")
                        .param("size", "10")
                        .locale(Locale.forLanguageTag("en"))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(habitService).getAllByTagsAndLanguageCode(any(Pageable.class), eq(Arrays.asList("tag1", "tag2")), eq("en"));
    }

    @Test
    @DisplayName("Test to get all habits by different parameters and return habits list if exists")
    void getAllByDifferentParameters_ValidParameters_ReturnsHabits() throws Exception {
        when(habitService.getAllByDifferentParameters(eq(userVO), any(), any(), any(), any(), eq("en")))
                .thenReturn(pageableDto);

        mockMvc.perform(get("/habit/search")
                        .param("tags", "tag1", "tag2")
                        .param("isCustomHabit", "true")
                        .param("complexities", "1", "2")
                        .locale(Locale.ENGLISH)
                        .with(authentication(createAuth(userVO))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(habitService).getAllByDifferentParameters(eq(userVO), any(), any(), any(), any(), eq("en"));
    }

    @Test
    @DisplayName("Test to throw BadRequestException when no parameters provided")
    void getAllByDifferentParameters_NoParamsProvided_ThrowsBadRequestException() throws Exception {
        when(habitService.getAllByDifferentParameters(
                eq(userVO),
                any(Pageable.class),
                eq(Optional.empty()),
                eq(Optional.empty()),
                eq(Optional.empty()),
                eq("en")
        )).thenThrow(new BadRequestException("You should enter at least one parameter"));

        mockMvc.perform(get("/habit/search")
                        .locale(Locale.ENGLISH))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(
                        BadRequestException.class,
                        result.getResolvedException()
                ))
                .andExpect(result -> assertEquals(
                        "You should enter at least one parameter",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()
                ));
    }

    @Test
    @DisplayName("Test to get all habit tags and return tags if exists")
    void findAllHabitsTags_TagsExist_ReturnsTags() throws Exception {
        List<String> tags = List.of("Recycling", "Conservation");

        when(tagsService.findAllHabitsTags("en")).thenReturn(tags);

        mockMvc.perform(get("/habit/tags").locale(Locale.ENGLISH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("Recycling"))
                .andExpect(jsonPath("$[1]").value("Conservation"));

        verify(tagsService).findAllHabitsTags(eq("en"));
    }

    @Test
    @DisplayName("Test to add a custom habit and create habit if valid")
    void addCustomHabit_ValidHabit_CreatesHabit() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AddCustomHabitDtoRequest request = AddCustomHabitDtoRequest.builder().complexity(1).build();
        AddCustomHabitDtoResponse response = AddCustomHabitDtoResponse.builder().id(1L).complexity(1).build();
        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile jsonFile = new MockMultipartFile(
                "request",
                "",
                "application/json",
                requestJson.getBytes()
        );
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        when(habitService.addCustomHabit(any(AddCustomHabitDtoRequest.class), any(MultipartFile.class), eq("user")))
                .thenReturn(response);

        mockMvc.perform(multipart("/habit/custom")
                        .file(jsonFile)
                        .file(image)
                        .with(csrf())
                        .principal(() -> "user")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));

        verify(habitService).addCustomHabit(any(AddCustomHabitDtoRequest.class), any(MultipartFile.class), eq("user"));
    }

    @Test
    @DisplayName("Test to get friends assigned to habit profile pictures and return pictures if exists")
    void getFriendsAssignedToHabitProfilePictures_HabitExists_ReturnsPictures() throws Exception {
        Long habitId = 1L;
        UserProfilePictureDto pic1 = getUserProfilePictureDto(1L, "url1", "Description 1");
        UserProfilePictureDto pic2 = getUserProfilePictureDto(2L, "url2", "Description 2");
        List<UserProfilePictureDto> pictures = List.of(pic1, pic2);

        when(habitService.getFriendsAssignedToHabitProfilePictures(habitId, userVO.getId())).thenReturn(pictures);

        mockMvc.perform(get("/habit/" + habitId + "/friends/profile-pictures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("url1"))
                .andExpect(jsonPath("$[1].name").value("url2"));

        verify(habitService).getFriendsAssignedToHabitProfilePictures(habitId, userVO.getId());
    }

    @Test
    @DisplayName("Test to handle BadRequestException using CustomExceptionHandler")
    void whenBadRequestExceptionThrown_thenHandledByCustomExceptionHandler() throws Exception {
        when(habitService.getAllByDifferentParameters(
                eq(userVO),
                any(Pageable.class),
                eq(Optional.empty()),
                eq(Optional.empty()),
                eq(Optional.empty()),
                eq("en")
        )).thenThrow(new BadRequestException("You should enter at least one parameter"));

        mockMvc.perform(get("/habit/search")
                        .locale(Locale.ENGLISH))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("You should enter at least one parameter"));
    }

    private Authentication createAuth(UserVO user) {
        return new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}