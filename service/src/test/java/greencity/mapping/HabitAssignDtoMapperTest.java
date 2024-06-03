package greencity.mapping;

import greencity.dto.habit.HabitAssignDto;
import greencity.dto.habitstatuscalendar.HabitStatusCalendarDto;
import greencity.entity.HabitAssign;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.getHabitAssign;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitAssignDtoMapperTest {

    @InjectMocks
    private HabitAssignDtoMapper mapper;

    @Test
    @DisplayName("Test convert from HabitAssign to HabitAssignDto")
    void convert() {
        HabitAssign habitAssign = getHabitAssign();

        HabitAssignDto result = mapper.convert(habitAssign);

        assertNotNull(result);
        assertEquals(habitAssign.getId(), result.getId());
        assertEquals(habitAssign.getStatus(), result.getStatus());
        assertEquals(habitAssign.getCreateDate(), result.getCreateDateTime());
        assertEquals(habitAssign.getUser().getId(), result.getUserId());
        assertEquals(habitAssign.getDuration(), result.getDuration());
        assertEquals(habitAssign.getHabitStreak(), result.getHabitStreak());
        assertEquals(habitAssign.getWorkingDays(), result.getWorkingDays());
        assertEquals(habitAssign.getLastEnrollmentDate(), result.getLastEnrollmentDate());

        assertEquals(habitAssign.getHabitStatusCalendars().size(), result.getHabitStatusCalendarDtoList().size());

        habitAssign.getHabitStatusCalendars().forEach(expected -> {
            HabitStatusCalendarDto actual = result.getHabitStatusCalendarDtoList().stream()
                    .filter(dto -> dto.getId().equals(expected.getId()))
                    .findFirst()
                    .orElse(null);
            assertNotNull(actual);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getEnrollDate(), actual.getEnrollDate());
        });
    }
}
