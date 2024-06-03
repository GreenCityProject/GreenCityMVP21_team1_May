package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.habit.HabitVO;
import greencity.dto.habitfact.HabitFactDtoResponse;
import greencity.dto.habitfact.HabitFactTranslationDto;
import greencity.dto.habitfact.HabitFactTranslationVO;
import greencity.dto.habitfact.HabitFactVO;
import greencity.entity.EcoNewsComment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitFactDtoResponseMapperTest {

    @InjectMocks
    private HabitFactDtoResponseMapper mapper;

    @Test
    @DisplayName("Test comvert from HabitFactVO to HabitFactDtoResponse")
    void convert() {

        HabitFactVO habitFactVO = ModelUtils.getHabitFactV0();
        HabitFactDtoResponse result = mapper.convert(habitFactVO);

        assertNotNull(result);
        assertEquals(habitFactVO.getId(), result.getId());
        assertEquals(habitFactVO.getHabit(), result.getHabit());
        assertEquals(habitFactVO.getTranslations().size(), result.getTranslations().size());
        
    }
}