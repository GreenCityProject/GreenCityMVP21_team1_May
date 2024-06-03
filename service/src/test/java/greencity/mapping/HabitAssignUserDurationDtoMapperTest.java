package greencity.mapping;

import greencity.dto.habit.HabitAssignUserDurationDto;
import greencity.entity.HabitAssign;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.getHabitAssign;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitAssignUserDurationDtoMapperTest {

    @InjectMocks
    private HabitAssignUserDurationDtoMapper mapper;

    @Test
    @DisplayName("Test convert from HabitAssign to HabitAssignUserDurationDto")
    void convert() {
        HabitAssign habitAssign = getHabitAssign();

        HabitAssignUserDurationDto result = mapper.convert(habitAssign);

        assertNotNull(result);
        assertEquals(habitAssign.getId(), result.getHabitAssignId());
        assertEquals(habitAssign.getUser().getId(), result.getUserId());
        assertEquals(habitAssign.getHabit().getId(), result.getHabitId());
        assertEquals(habitAssign.getStatus(), result.getStatus());
        assertEquals(habitAssign.getWorkingDays(), result.getWorkingDays());
        assertEquals(habitAssign.getDuration(), result.getDuration());
    }
}
