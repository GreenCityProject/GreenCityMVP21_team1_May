package greencity.service;

import greencity.client.RestClient;
import greencity.dto.user.NotificationDto;
import greencity.dto.user.UserVO;
import greencity.repository.EventRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static greencity.ModelUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@ExtendWith(SpringExtension.class)
class EventServiceImplTest {

    @Mock
    ModelMapper modelMapper;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    EventRepo eventRepo;

    @Mock
    UserService userService;

    @Mock
    RestClient restClient;

    @Mock
    FileService fileService;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void create_validRequestDto_ReturnsResponseDto() {
        var expectedResult = getEventCreateDtoResponse(2, 2);
        var dto = getEventCreateDtoRequest(2);
        var multipartFile1 = new MockMultipartFile("images", "test1.jpg", MULTIPART_FORM_DATA_VALUE, "test image1 content".getBytes());
        var multipartFile2 = new MockMultipartFile("images", "test2.jpg", MULTIPART_FORM_DATA_VALUE, "test image2 content".getBytes());
        var multipartFiles = new MockMultipartFile[]{multipartFile1, multipartFile2};
        var principal = expectedResult.getOrganizer().getEmail();
        var event = getEvent(2, 2);
        var userVO = UserVO.builder()
                .id(expectedResult.getOrganizer().getId())
                .name(expectedResult.getOrganizer().getName())
                .email(expectedResult.getOrganizer().getEmail())
                .build();

        when(userService.findByEmail(principal)).thenReturn(userVO);
        when(fileService.upload(multipartFile1)).thenReturn(expectedResult.getAdditionalImages().get(0).getData());
        when(fileService.upload(multipartFile2)).thenReturn(expectedResult.getAdditionalImages().get(1).getData());
        when(eventRepo.save(any())).thenReturn(event);

        var actualResult = eventService.create(dto, multipartFiles, principal);

        assertEquals(expectedResult, actualResult);

        verify(userService).findByEmail(expectedResult.getOrganizer().getEmail());
        verify(fileService, times(2)).upload(any(MultipartFile.class));
        verify(eventRepo).save(event);
        verify(restClient).sendNotificationToUser(any(NotificationDto.class), eq(principal));
    }
}