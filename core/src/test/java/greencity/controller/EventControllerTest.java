package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import greencity.GreenCityApplication;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
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

        mockMvc.perform(multipart(EVENT_LINK + "/create")
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
}
