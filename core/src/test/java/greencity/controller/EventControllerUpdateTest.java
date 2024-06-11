package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventUpdateDtoRequest;
import greencity.dto.user.UserVO;
import greencity.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static greencity.ModelUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)

class EventControllerUpdateTest {
    private static final String LINK = "/event";

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(eventController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getAllEventsTest() throws Exception {
        EventCreateDtoResponse eventCreateDtoResponse = getEventCreateDtoResponse(1, 1);
        List<EventCreateDtoResponse> dtoList = List.of(eventCreateDtoResponse);

        when(eventService.getAll()).thenReturn(dtoList);

        mockMvc.perform(get(LINK))
                .andExpect(status().isOk());
              //.andExpect(jsonPath("$").value(dtoList));
              //.andExpect(jsonPath("$").value(new ObjectMapper().writeValueAsString(dtoList)));

        verify(eventService).getAll();
    }

    @Test
    void getEventByIdTest() throws Exception {
        EventCreateDtoResponse eventCreateDtoResponse = getEventCreateDtoResponse(1, 1);

        when(eventService.findEventById(eventCreateDtoResponse.getId())).thenReturn(eventCreateDtoResponse);

        mockMvc.perform(get(LINK + "/{id}", 1))
                .andExpect(status().isOk());

        verify(eventService).findEventById(eventCreateDtoResponse.getId());
    }


//    @Test
//    void updateEventByIdTest() throws Exception {
//        EventCreateDtoResponse eventCreateDtoResponse = EventCreateDtoResponse.builder().id(1L).build();
//
//        EventUpdateDtoRequest eventUpdate = EventUpdateDtoRequest.builder().id(1L).build();
//        MultipartFile[] images = new MockMultipartFile[]{new MockMultipartFile("images", "test1.jpg", "multipart/form-data", "test image1 content".getBytes())};
//        UserVO user = getUserVO();
//
//        when(eventService.update(eventUpdate, images, user)).thenReturn(eventCreateDtoResponse);
//
//        mockMvc.perform(put(LINK + "/update"))
//                .andExpect(status().isOk());
//
//        verify(eventService).update(eventUpdate, images, user);
//    }
}