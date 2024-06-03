package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.econewscomment.AddEcoNewsCommentDtoRequest;
import greencity.dto.user.EcoNewsAuthorDto;
import greencity.entity.EcoNewsComment;
import greencity.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.getEcoNewsAuthorDto;
import static greencity.ModelUtils.getEcoNewsComment;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EcoNewsAuthorDtoMapperTest {

    @InjectMocks
    private EcoNewsAuthorDtoMapper mapper;


    @Test
    @DisplayName("Test to map User to EcoNewsAuthorDto")
    void convert() {

        EcoNewsAuthorDto expected = getEcoNewsAuthorDto();

        User userToBeConverted = User.builder()
                .id(expected.getId())
                .name(expected.getName())
                .build();


        assertEquals(expected, mapper.convert(userToBeConverted));
    }
}