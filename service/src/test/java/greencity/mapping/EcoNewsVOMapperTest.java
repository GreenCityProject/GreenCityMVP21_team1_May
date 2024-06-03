package greencity.mapping;

import greencity.dto.econews.EcoNewsVO;
import greencity.entity.EcoNews;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.getEcoNews;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EcoNewsVOMapperTest {

    @InjectMocks
    private EcoNewsVOMapper mapper;

    @Test
    @DisplayName("Test convert from EcoNews to EcoNewsVO")
    void convert() {

        EcoNews ecoNews = getEcoNews();
        EcoNewsVO result = mapper.convert(ecoNews);

        assertNotNull(result);
        assertEquals(ecoNews.getId(), result.getId());
        assertNotNull(result.getAuthor());
        assertEquals(ecoNews.getAuthor().getId(), result.getAuthor().getId());
        assertEquals(ecoNews.getAuthor().getName(), result.getAuthor().getName());
        assertEquals(ecoNews.getAuthor().getUserStatus(), result.getAuthor().getUserStatus());
        assertEquals(ecoNews.getAuthor().getRole(), result.getAuthor().getRole());
        assertEquals(ecoNews.getCreationDate(), result.getCreationDate());
        assertEquals(ecoNews.getImagePath(), result.getImagePath());
        assertEquals(ecoNews.getSource(), result.getSource());
        assertEquals(ecoNews.getText(), result.getText());
        assertEquals(ecoNews.getTitle(), result.getTitle());
        assertNotNull(result.getTags());
        assertEquals(ecoNews.getTags().size(), result.getTags().size());
        assertEquals(ecoNews.getTags().getFirst().getId(), result.getTags().getFirst().getId());
        assertEquals(ecoNews.getTags().getFirst().getTagTranslations().getFirst().getName(), result.getTags().getFirst().getTagTranslations().getFirst().getName());
        assertNotNull(result.getUsersLikedNews());
        assertEquals(ecoNews.getUsersLikedNews().size(), result.getUsersLikedNews().size());
        assertEquals(ecoNews.getUsersDislikedNews().size(), result.getUsersDislikedNews().size());
        assertNotNull(result.getEcoNewsComments());
        assertEquals(ecoNews.getEcoNewsComments().size(), result.getEcoNewsComments().size());
        assertEquals(ecoNews.getEcoNewsComments().getFirst().getId(), result.getEcoNewsComments().getFirst().getId());
        assertEquals(ecoNews.getEcoNewsComments().getFirst().getText(), result.getEcoNewsComments().getFirst().getText());
        assertNotNull(result.getEcoNewsComments().getFirst().getUser());
        assertEquals(ecoNews.getEcoNewsComments().getFirst().getUser().getId(), result.getEcoNewsComments().getFirst().getUser().getId());
    }
}
