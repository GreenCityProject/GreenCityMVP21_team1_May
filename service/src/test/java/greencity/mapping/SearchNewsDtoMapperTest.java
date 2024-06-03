package greencity.mapping;

import greencity.dto.search.SearchNewsDto;
import greencity.entity.EcoNews;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static greencity.ModelUtils.getEcoNews;

@ExtendWith(MockitoExtension.class)
class SearchNewsDtoMapperTest {
    @InjectMocks
    private SearchNewsDtoMapper mapper;

    @BeforeEach
    void setUp() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }

    @Test
    @DisplayName("Test convert from EcoNews to SearchNewsDto")
    void convert() {
        EcoNews ecoNews = getEcoNews();

        SearchNewsDto result = mapper.convert(ecoNews);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(ecoNews.getId(), result.getId(), "IDs should match"),
                () -> assertEquals(ecoNews.getTitle(), result.getTitle(), "Titles should match"),
                () -> assertNotNull(result.getAuthor(), "Author should not be null"),
                () -> assertEquals(ecoNews.getAuthor().getId(), result.getAuthor().getId(), "Author IDs should match"),
                () -> assertEquals(ecoNews.getAuthor().getName(), result.getAuthor().getName(), "Author names should match"),
                () -> assertEquals(ecoNews.getCreationDate(), result.getCreationDate(), "Creation dates should match"),
                () -> assertNotNull(result.getTags(), "Tags should not be null"),
                () -> assertEquals(1, result.getTags().size(), "Tags size should be 1"),
                () -> assertEquals("News", result.getTags().getFirst(), "Tag name should match")
        );
    }
}