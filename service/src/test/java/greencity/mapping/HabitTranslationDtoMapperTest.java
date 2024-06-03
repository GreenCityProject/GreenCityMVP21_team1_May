package greencity.mapping;

import greencity.dto.habittranslation.HabitTranslationDto;
import greencity.entity.HabitTranslation;
import greencity.entity.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class HabitTranslationDtoMapperTest {

    HabitTranslationDtoMapper habitTranslationDtoMapper = new HabitTranslationDtoMapper();

    HabitTranslation habitTranslation;
    HabitTranslationDto habitTranslationDto;

    @BeforeEach
    void setUp() {
        Language language = Language.builder()
                .code("EN")
                .build();

        habitTranslation = HabitTranslation.builder()
                .name("Test Habit")
                .description("Test Description")
                .habitItem("Test Item")
                .language(language)
                .build();

        habitTranslationDto = HabitTranslationDto.builder()
                .name(habitTranslation.getName())
                .languageCode(habitTranslation.getLanguage().getCode())
                .habitItem(habitTranslation.getHabitItem())
                .description(habitTranslation.getDescription())
                .build();

    }

    @Test
    void testConvert() {
        HabitTranslationDto habitTranslationDtoActual = habitTranslationDtoMapper.convert(habitTranslation);

        Assertions.assertEquals(habitTranslationDto.getDescription(), habitTranslationDtoActual.getDescription());
        Assertions.assertEquals(habitTranslationDto.getHabitItem(), habitTranslationDtoActual.getHabitItem());
        Assertions.assertEquals(habitTranslationDto.getLanguageCode(), habitTranslationDtoActual.getLanguageCode());
        Assertions.assertEquals(habitTranslationDto.getName(), habitTranslationDtoActual.getName());

        Assertions.assertEquals(habitTranslationDto, habitTranslationDtoActual);
    }

    @Test
    void testMapToList() {
        Assertions.assertEquals(List.of(habitTranslationDto), habitTranslationDtoMapper.mapAllToList(List.of(habitTranslation)));
    }
}