package greencity.mapping;

import greencity.dto.tag.NewTagDto;
import greencity.entity.Tag;
import greencity.entity.localization.TagTranslation;
import greencity.entity.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NewTagDtoMapperTest {

    @InjectMocks
    private NewTagDtoMapper mapper;

    @Test
    @DisplayName("Test convert from Tag to NewTagDto without English translation")
    void convertWithoutEnglishTranslation() {
        Tag tag = Tag.builder()
                .id(1L)
                .tagTranslations(List.of(
                        TagTranslation.builder()
                                .id(2L)
                                .name("Новини")
                                .language(Language.builder().code("ua").build())
                                .build()
                ))
                .build();

        NewTagDto result = mapper.convert(tag);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(tag.getId(), result.getId(), "IDs should match"),
                () -> assertNull(result.getName(), "English name should be null"),
                () -> assertEquals("Новини", result.getNameUa(), "Ukrainian name should match")
        );
    }

    @Test
    @DisplayName("Test convert from Tag to NewTagDto without Ukrainian translation")
    void convertWithoutUkrainianTranslation() {
        Tag tag = Tag.builder()
                .id(1L)
                .tagTranslations(List.of(
                        TagTranslation.builder()
                                .id(1L)
                                .name("News")
                                .language(Language.builder().code("en").build())
                                .build()
                ))
                .build();

        NewTagDto result = mapper.convert(tag);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(tag.getId(), result.getId(), "IDs should match"),
                () -> assertEquals("News", result.getName(), "English name should match"),
                () -> assertNull(result.getNameUa(), "Ukrainian name should be null")
        );
    }
}
