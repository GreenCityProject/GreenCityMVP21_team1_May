package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.econews.EcoNewsDto;
import greencity.entity.EcoNews;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EcoNewsDtoMapperTest {

    @InjectMocks
    private EcoNewsDtoMapper mapper;

    @Test
    @DisplayName("Test convert form EcoNews to EcoNewsDto")
    void convert() {

        EcoNews ecoNews = ModelUtils.getEcoNews();
        EcoNewsDto result = mapper.convert(ecoNews);

        assertNotNull(result);
        assertEquals(ecoNews.getCreationDate(), result.getCreationDate());
        assertEquals(ecoNews.getImagePath(), result.getImagePath());
        assertEquals(ecoNews.getId(), result.getId());
        assertEquals(ecoNews.getTitle(), result.getTitle());
        assertEquals(ecoNews.getText(), result.getContent());
        assertEquals(ecoNews.getShortInfo(), result.getShortInfo());
        assertNotNull(result.getAuthor());
        assertEquals(ecoNews.getAuthor().getId(), result.getAuthor().getId());

    }
}