package greencity.controller;

import greencity.GreenCityApplication;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.user.UserVO;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.EventService;
import greencity.service.LanguageService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static greencity.ModelUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "username")
@WebMvcTest(EventController.class)
@Import(CustomExceptionHandler.class)
@ContextConfiguration(classes = {GreenCityApplication.class})
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private LanguageService languageService;

    @MockBean
    private UserService userService;

    @MockBean
    private EventService eventService;

    private UserVO userVO;

    public static final String EVENT_LINK = "/event";

//    private PageableDto<HabitDto> pageableDto;

    @BeforeEach
    void setUp() {
//        userVO = getUserVO();
//        pageableDto = getPageableHabitDto(1, 1, 1);

//        when(userService.findByEmail(anyString())).thenReturn(userVO);
//        when(languageService.findAllLanguageCodes()).thenReturn(List.of("en"));
    }

    @Test
    void createEvent_201Created_ReturnsDto() throws Exception {
        var mapper = getObjectMapper();
        var dtoRequest = mapper.writeValueAsString(getEventCreateDtoRequest(1));
        var serviceResponse = getEventCreateDtoResponse(1, 1);
        var json = new MockMultipartFile("dto", "", APPLICATION_JSON_VALUE, dtoRequest.getBytes());
        var image1 = new MockMultipartFile("images", "test1.jpg", MULTIPART_FORM_DATA_VALUE, "test image1 content".getBytes());
        var image2 = new MockMultipartFile("images", "test2.jpg", MULTIPART_FORM_DATA_VALUE, "test image2 content".getBytes());

        when(eventService.create(any(), any(), any())).thenReturn(serviceResponse);

        var actualResp = mapper.writeValueAsString(serviceResponse);
        mockMvc.perform(multipart(EVENT_LINK + "/create")
                        .file(image1)
                        .file(image2)
                        .file(json)
                        .with(csrf())
                        .principal(() -> "username")
//                        .content(dtoRequest)
                        .locale(Locale.ENGLISH)
                        .accept(MULTIPART_FORM_DATA, APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(actualResp));

        verify(eventService).create(any(), any(), any());

    }
}
