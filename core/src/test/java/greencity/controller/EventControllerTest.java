package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import greencity.GreenCityApplication;
import greencity.ModelUtils;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.EventService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static greencity.ModelUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private UserService userService;

    @MockBean
    private EventService eventService;

    private static ObjectWriter writer;

    public static final String EVENT_LINK = "/event";

    @BeforeAll
    static void setUp() {
        ObjectMapper mapper = getObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        writer = mapper
                .writer()
                .withDefaultPrettyPrinter()
                .with(new SimpleDateFormat("yy-MM-yy HH:mm"));
    }

    @Test
    void createEvent_validRequest_201Created() throws Exception {
        int numberOfEventDateLocations = 2;

        var dtoRequestJson = writer.writeValueAsString(getEventCreateDtoRequest(numberOfEventDateLocations));
        var serviceResponse = getEventCreateDtoResponse(numberOfEventDateLocations, 2);
        var dtoResponseJson = writer.writeValueAsString(serviceResponse);

        var dtoMultipartFile = new MockMultipartFile("dto", "", APPLICATION_JSON_VALUE, dtoRequestJson.getBytes());
        var firstMultipartFile = new MockMultipartFile("images", "test1.jpg", MULTIPART_FORM_DATA_VALUE, "test image1 content".getBytes());
        var secondMultipartFile = new MockMultipartFile("images", "test2.jpg", MULTIPART_FORM_DATA_VALUE, "test image2 content".getBytes());

        when(eventService.create(any(), any(), any())).thenReturn(serviceResponse);

        mockMvc.perform(multipart(EVENT_LINK )
                        .file(dtoMultipartFile)
                        .file(firstMultipartFile)
                        .file(secondMultipartFile)
                        .with(csrf())
                        .principal(() -> "username")
                        .locale(Locale.ENGLISH)
                        .accept(MULTIPART_FORM_DATA, APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(dtoResponseJson));

        verify(eventService).create(any(), any(), any());
    }

    @Test
    void deleteEvent_validRequest_200Ok() throws Exception {
        Long eventId = 1L;

        mockMvc.perform(delete(EVENT_LINK + "/" + eventId)
                        .with(csrf())
                        .principal(() -> "username")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(eventService).delete(eq(eventId), any());
    }

    @Test
    void getEventByQuery_validRequest_200ok() throws Exception {
        var serviceResponse = ModelUtils.getPageableAdvancedDtoForEventCreateDtoResponse(1, 1);
        when(eventService.findEventByQuery(anyString(), any(Pageable.class))).thenReturn(serviceResponse);

        mockMvc.perform(get(EVENT_LINK + "/search")
                        .param("query", "Мій Український Event-'моЄ'..."))
                .andExpect(status().isOk())
                .andExpect(content().json(writer.writeValueAsString(serviceResponse)));

        verify(eventService).findEventByQuery(anyString(), any(Pageable.class));
    }

    @Test
    void getEventByQuery_forbiddenQuerySymbols_400BadRequest() throws Exception {
        var message = "Query allows only 1-30 symbols: ENG and UKR alphabetic characters, a dot, a space, apostrophe and hyphen";

        mockMvc.perform(get(EVENT_LINK + "/search")
                        .param("query", "ЫыЪъЁёЭэ123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));

        verify(eventService, never()).findEventByQuery(anyString(), any(Pageable.class));
    }

    @Test
    void getEventByQuery_tooMuchSymbols_400BadRequest() throws Exception {
        var message = "Query allows only 1-30 symbols: ENG and UKR alphabetic characters, a dot, a space, apostrophe and hyphen";

        mockMvc.perform(get(EVENT_LINK + "/search")
                        .param("query", "моє задовге речення на 33 символа"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));

        verify(eventService, never()).findEventByQuery(anyString(), any(Pageable.class));
    }
}
