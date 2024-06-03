package greencity.mapping;

import greencity.dto.habittranslation.HabitTranslationDto;
import greencity.entity.HabitTranslation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class HabitTranslationMapperTest {

    HabitTranslationMapper habitTranslationMapper = new HabitTranslationMapper();

    HabitTranslation habitTranslation;
    HabitTranslationDto habitTranslationDto;

    @BeforeEach
    void setUp() {
        habitTranslationDto = HabitTranslationDto.builder()
                .name("Test Habit")
                .habitItem("Test Item")
                .description("Test Description")
                .build();

        habitTranslation = HabitTranslation.builder()
                .name(habitTranslationDto.getName())
                .description(habitTranslationDto.getDescription())
                .habitItem(habitTranslationDto.getHabitItem())
                .build();
    }

    @Test
    void testConvert() {
        HabitTranslation habitTranslationActual = habitTranslationMapper.convert(habitTranslationDto);

        Assertions.assertEquals(habitTranslation.getId(), habitTranslationActual.getId());
        Assertions.assertEquals(habitTranslation.getName(), habitTranslationActual.getName());
        Assertions.assertEquals(habitTranslation.getDescription(), habitTranslationActual.getDescription());
        Assertions.assertEquals(habitTranslation.getHabitItem(), habitTranslationActual.getHabitItem());
        Assertions.assertEquals(habitTranslation.getLanguage(), habitTranslationActual.getLanguage());
        Assertions.assertEquals(habitTranslation.getHabit(), habitTranslationActual.getHabit());

        Assertions.assertEquals(habitTranslation, habitTranslationActual);
    }

    @Test
    void testMapToList() {
        Assertions.assertEquals(List.of(habitTranslation), habitTranslationMapper.mapAllToList(List.of(habitTranslationDto)));
    }
}