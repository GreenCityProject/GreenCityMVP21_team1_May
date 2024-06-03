package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.habitstatistic.HabitStatisticDto;
import greencity.entity.HabitStatistic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitStatisticDtoMapperTest {

    @InjectMocks
    private HabitStatisticDtoMapper mapper;

    @Test
    @DisplayName("Test covert form HabitStatistic to HabitStatisticDto")
    void convert() {
        HabitStatistic habitStatistic = ModelUtils.getHabitStatistic();
        HabitStatisticDto result = mapper.convert(habitStatistic);

        assertNotNull(result);
        assertEquals(habitStatistic.getId(), result.getId());
        assertEquals(habitStatistic.getHabitRate(), result.getHabitRate());
        assertEquals(habitStatistic.getCreateDate(), result.getCreateDate());
        assertEquals(habitStatistic.getAmountOfItems(), result.getAmountOfItems());
        assertEquals(habitStatistic.getHabitAssign().getId(), result.getHabitAssignId());
    }
}