package greencity.mapping;

import greencity.dto.tag.TagDto;
import greencity.entity.Tag;
import greencity.entity.localization.TagTranslation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TagDtoMapperTest {

    TagDtoMapper tagDtoMapper = new TagDtoMapper();

    TagDto expected = TagDto.builder()
            .id(123L)
            .name("Tag")
            .build();

    TagTranslation target = TagTranslation.builder()
            .id(5L)
            .name("Tag")
            .tag(Tag.builder().id(123L).build())
            .language(null)
            .build();

    @Test
    void convertTagDtoToTagTest() {
        TagDto actual = tagDtoMapper.convert(target);

        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());

        Assertions.assertEquals(expected, actual);

    }
}