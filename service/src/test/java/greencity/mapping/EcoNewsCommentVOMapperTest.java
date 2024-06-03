package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.econewscomment.EcoNewsCommentVO;
import greencity.entity.EcoNewsComment;
import greencity.enums.CommentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EcoNewsCommentVOMapperTest {

    @InjectMocks
    private EcoNewsCommentVOMapper mapper;

    @Test
    @DisplayName("Test convert from EcoNewsComment to EcoNewsCommentVO")
    void convert() {


        EcoNewsComment toBeConverted = ModelUtils.getEcoNewsComment();
        EcoNewsCommentVO result = mapper.convert(toBeConverted);

        assert result != null;
        assertEquals(toBeConverted.getId(), result.getId());
        assertEquals(toBeConverted.getCreatedDate(), result.getCreatedDate());
        assertEquals(toBeConverted.getModifiedDate(), result.getModifiedDate());
        assertEquals(toBeConverted.getText(), result.getText());
        assertEquals(toBeConverted.getUser().getId() , result.getUser().getId());
        assertEquals(toBeConverted.getEcoNews().getId(),result.getEcoNews().getId());

    }
}