package greencity.mapping;


import greencity.dto.habit.AddCustomHabitDtoRequest;
import greencity.entity.Habit;
import greencity.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CustomHabitMapperTest {

    @InjectMocks
    private CustomHabitMapper mapper;

    @Test
    @DisplayName("Test to convert Habit to Custom Habit")
    void convertTest() {

        Habit expected = getCustomHabit();

        AddCustomHabitDtoRequest habitDtoRequestToBeConverted = AddCustomHabitDtoRequest.builder()
                .image(expected.getImage())
                .complexity(expected.getComplexity())
                .defaultDuration(expected.getDefaultDuration())
                .build();


        assertEquals(expected, mapper.convert(habitDtoRequestToBeConverted));
    }
}