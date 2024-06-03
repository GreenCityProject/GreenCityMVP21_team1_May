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

class TagVOMapperTest {
    TagVOMapper mapper = new TagVOMapper();

    LanguageVO language = LanguageVO.builder()
            .id(23L)
            .code("en")
            .build();

    TagTranslationVO ttVO = TagTranslationVO.builder()
            .id(5L)
            .name("Tag")
            .languageVO(language)
            .build();

    TagTranslation tt = TagTranslation.builder()
            .id(ttVO.getId())
            .name(ttVO.getName())
            .language(languageVOToLanguage(language))
            .build();

    final TagVO expected = TagVO.builder()
            .id(1L)
            .type(TagType.ECO_NEWS)
            .tagTranslations(List.of(ttVO))
            .build();

    final Tag target = Tag.builder()
            .id(1L)
            .type(TagType.ECO_NEWS)
            .tagTranslations(List.of(tt))
            .build();

    @Test
    void convertTest() {
        TagVO actual = mapper.convert(target);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getTagTranslations(), actual.getTagTranslations());
        assertEquals(expected.getEcoNews(), actual.getEcoNews());
        assertEquals(expected.getHabits(), actual.getHabits());

        assertEquals(expected, actual);
    }

    // should be extracted as a LanguageMapper?
    Language languageVOToLanguage(LanguageVO language){
        return Language.builder().id(language.getId()).code(language.getCode()).build();
    }
}