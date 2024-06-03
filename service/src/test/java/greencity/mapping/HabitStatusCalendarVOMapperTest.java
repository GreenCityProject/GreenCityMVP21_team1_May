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
class HabitStatusCalendarVOMapperTest {

    @InjectMocks
    private HabitStatusCalendarVOMapper mapper;

    @Test
    @DisplayName("Test convert from HabitStatusCalendar to HabitStatusCalendarVO")
    void convert() {
        HabitStatusCalendar habitStatusCalendar = ModelUtils.getHabitStatusCalendar();
        HabitStatusCalendarVO result = mapper.convert(habitStatusCalendar);

        assertNotNull(result);
        assertEquals(habitStatusCalendar.getId(), result.getId());
        assertEquals(habitStatusCalendar.getEnrollDate(), result.getEnrollDate());
        assertEquals(habitStatusCalendar.getHabitAssign().getId(), result.getHabitAssignVO().getId());

    }
}