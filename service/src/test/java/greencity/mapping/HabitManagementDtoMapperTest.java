package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.habit.HabitManagementDto;
import greencity.dto.habittranslation.HabitTranslationManagementDto;
import greencity.entity.Habit;
import greencity.entity.HabitTranslation;
import greencity.entity.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitManagementDtoMapperTest {

    @InjectMocks
    private HabitManagementDtoMapper mapper;

    @Test
    @DisplayName("Test convert from Habit to HabitManagementDto")
    void convert() {


        Habit habit = ModelUtils.getHabit();
        HabitManagementDto result = mapper.convert(habit);

        assertNotNull(result);
        assertEquals(habit.getId(), result.getId());
        assertEquals(habit.getImage(), result.getImage());
        assertEquals(habit.getComplexity(), result.getComplexity());
        assertEquals(habit.getDefaultDuration(), result.getDefaultDuration());
        assertEquals(habit.getHabitTranslations().size(), result.getHabitTranslations().size());
        long size = habit.getHabitTranslations().size();
        if (!habit.getHabitTranslations().isEmpty()) {
            for (int i = 0; i < size; i++) {
                assertEquals(habit.getHabitTranslations().get(i).getId(), result.getHabitTranslations().get(i).getId());
                assertEquals(habit.getHabitTranslations().get(i).getName(), result.getHabitTranslations().get(i).getName());
                assertEquals(habit.getHabitTranslations().get(i).getDescription(), result.getHabitTranslations().get(i).getDescription());
                assertEquals(habit.getHabitTranslations().get(i).getHabitItem(), result.getHabitTranslations().get(i).getHabitItem());
            }

        }
    }
}