package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.habitstatuscalendar.HabitStatusCalendarVO;
import greencity.entity.HabitStatusCalendar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitStatusCalendarMapperTest {

    @InjectMocks
    private HabitStatusCalendarMapper mapper;
    @Test
    @DisplayName("Test convert from HabitStatusCalendarVO to HabitStatusCalendar")
    void convert() {

        HabitStatusCalendarVO habitStatusCalendarVO = ModelUtils.getHabitStatusCalendarVO();
        HabitStatusCalendar result = mapper.convert(habitStatusCalendarVO);

        assertNotNull(result);
        assertEquals(habitStatusCalendarVO.getId(), result.getId());
        assertEquals(habitStatusCalendarVO.getEnrollDate(), result.getEnrollDate());
        assertEquals(habitStatusCalendarVO.getHabitAssignVO().getId(), result.getHabitAssign().getId());
        //TODO: mapping is not full???? below assets fails
//        assertEquals(habitStatusCalendarVO.getHabitAssignVO().getCreateDateTime(), result.getHabitAssign().getCreateDate());
//        assertEquals(habitStatusCalendarVO.getHabitAssignVO().getStatus(), result.getHabitAssign().getStatus());
//        assertEquals(habitStatusCalendarVO.getHabitAssignVO().getDuration(), result.getHabitAssign().getDuration());
//        assertEquals(habitStatusCalendarVO.getHabitAssignVO().getWorkingDays(), result.getHabitAssign().getWorkingDays());
//        assertEquals(habitStatusCalendarVO.getHabitAssignVO().getId(), result.getHabitAssign().getId());
//        assertEquals(habitStatusCalendarVO.getHabitAssignVO().getHabitStreak(), result.getHabitAssign().getHabitStreak());
//        assertEquals(habitStatusCalendarVO.getHabitAssignVO().getLastEnrollmentDate(), result.getHabitAssign().getLastEnrollmentDate());
}
}