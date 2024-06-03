package greencity.mapping;

import greencity.dto.habit.HabitAssignManagementDto;
import greencity.entity.HabitAssign;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.getHabitAssign;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitAssignManagementDtoMapperTest {

    @InjectMocks
    private HabitAssignManagementDtoMapper mapper;

    @Test
    @DisplayName("Test convert from HabitAssign to HabitAssignManagementDto")
    void convert() {
        HabitAssign habitAssign = getHabitAssign();

        HabitAssignManagementDto result = mapper.convert(habitAssign);

        assertNotNull(result);
        assertEquals(habitAssign.getId(), result.getId());
        assertEquals(habitAssign.getStatus(), result.getStatus());
        assertEquals(habitAssign.getCreateDate(), result.getCreateDateTime());
        assertEquals(habitAssign.getUser().getId(), result.getUserId());
        assertEquals(habitAssign.getHabit().getId(), result.getHabitId());
        assertEquals(habitAssign.getDuration(), result.getDuration());
        assertEquals(habitAssign.getHabitStreak(), result.getHabitStreak());
        assertEquals(habitAssign.getWorkingDays(), result.getWorkingDays());
        assertEquals(habitAssign.getLastEnrollmentDate(), result.getLastEnrollment());
    }
}
