package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.econewscomment.EcoNewsCommentDto;
import greencity.entity.EcoNewsComment;
import greencity.entity.User;
import greencity.enums.CommentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EcoNewsCommentDtoMapperTest {

    @InjectMocks
    private EcoNewsCommentDtoMapper mapper;

    @Test
    @DisplayName("Test convert from EcoNewsComment to EcoNewsCommentDto")
    void convert() {
        
        EcoNewsComment ecoNewsComment = ModelUtils.getEcoNewsComment();
        EcoNewsCommentDto result = mapper.convert(ecoNewsComment);

        assert result != null;
        assertEquals( ecoNewsComment.getId(), result.getId());
        assertEquals( ecoNewsComment.getModifiedDate(), result.getModifiedDate());
        assertEquals( ecoNewsComment.getUser().getId(), result.getAuthor().getId());
        assertEquals( ecoNewsComment.getUser().getName(), result.getAuthor().getName());
        assertEquals( ecoNewsComment.getText(), result.getText() );
        assertEquals( ecoNewsComment.getComments().size(), result.getReplies());
        assertEquals( ecoNewsComment.getUsersLiked().size(), result.getLikes());
        assertEquals( ecoNewsComment.isCurrentUserLiked(), result.isCurrentUserLiked());
        assertEquals( CommentStatus.EDITED, result.getStatus());

    }
}