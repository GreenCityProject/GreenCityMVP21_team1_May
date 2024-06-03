package greencity.mapping;


import greencity.dto.language.LanguageVO;
import greencity.dto.tag.TagTranslationVO;
import greencity.dto.tag.TagVO;
import greencity.entity.Language;
import greencity.entity.Tag;
import greencity.entity.localization.TagTranslation;
import greencity.enums.TagType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagMapperTest {
    TagMapper mapper = new TagMapper();

    Language language = Language.builder()
            .id(23L)
            .code("en")
            .build();

    TagTranslation tagTranslation = TagTranslation.builder()
            .id(1L)
            .name("Tag")
            .language(language)
            .build();

    TagTranslationVO tagTranslationVO= TagTranslationVO.builder()
            .id(tagTranslation.getId())
            .name(tagTranslation.getName())
            .languageVO(languageToLanguageVO(language))
            .build();

    final Tag expected = Tag.builder()
            .id(1L)
            .type(TagType.ECO_NEWS)
            .tagTranslations(List.of(tagTranslation))
            .build();

    final TagVO targetTag = TagVO.builder()
            .id(1L)
            .type(TagType.ECO_NEWS)
            .tagTranslations(List.of(tagTranslationVO))
            .build();

    @Test
    void convertTest() {
        Tag actual = mapper.convert(targetTag);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getTagTranslations(), actual.getTagTranslations());
        assertEquals(expected.getEcoNews(), actual.getEcoNews());
        assertEquals(expected.getHabits(), actual.getHabits());

        assertEquals(expected, actual);
    }

    // should be extracted as a LanguageVOMapper?
    LanguageVO languageToLanguageVO(Language language){
        return LanguageVO.builder().id(language.getId()).code(language.getCode()).build();
    }
}