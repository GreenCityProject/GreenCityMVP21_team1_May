package greencity.mapping;

import greencity.dto.habit.HabitAssignDto;
import greencity.dto.habit.HabitDto;
import greencity.dto.user.UserShoppingListItemAdvanceDto;
import greencity.entity.HabitAssign;
import greencity.entity.UserShoppingListItem;
import greencity.enums.HabitAssignStatus;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitAssignMapperTest {

    @InjectMocks
    private HabitAssignMapper mapper;

    @Test
    @DisplayName("Test convert from HabitAssignDto to HabitAssign")
    void convert() {
        HabitAssignDto dto = HabitAssignDto.builder()
                .id(1L)
                .duration(30)
                .habitStreak(15)
                .createDateTime(ZonedDateTime.now())
                .status(HabitAssignStatus.INPROGRESS)
                .workingDays(20)
                .lastEnrollmentDate(ZonedDateTime.now())
                .habit(HabitDto.builder().id(1L).complexity(2).defaultDuration(30).build())
                .userShoppingListItems(Arrays.asList(
                        UserShoppingListItemAdvanceDto.builder()
                                .id(1L)
                                .shoppingListItemId(1L)
                                .status(ShoppingListItemStatus.INPROGRESS)
                                .dateCompleted(LocalDateTime.now())
                                .build(),
                        UserShoppingListItemAdvanceDto.builder()
                                .id(2L)
                                .shoppingListItemId(2L)
                                .status(ShoppingListItemStatus.DONE)
                                .dateCompleted(LocalDateTime.now())
                                .build()
                ))
                .build();

        HabitAssign result = mapper.convert(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getDuration(), result.getDuration());
        assertEquals(dto.getHabitStreak(), result.getHabitStreak());
        assertEquals(dto.getCreateDateTime(), result.getCreateDate());
        assertEquals(dto.getStatus(), result.getStatus());
        assertEquals(dto.getWorkingDays(), result.getWorkingDays());
        assertEquals(dto.getLastEnrollmentDate(), result.getLastEnrollmentDate());

        assertNotNull(result.getHabit());
        assertEquals(dto.getHabit().getId(), result.getHabit().getId());
        assertEquals(dto.getHabit().getComplexity(), result.getHabit().getComplexity());
        assertEquals(dto.getDuration(), result.getHabit().getDefaultDuration());

        List<UserShoppingListItem> resultList = result.getUserShoppingListItems();
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        UserShoppingListItemAdvanceDto expectedItem = dto.getUserShoppingListItems().getFirst();
        UserShoppingListItem actualItem = resultList.getFirst();
        assertEquals(expectedItem.getId(), actualItem.getId());
        assertEquals(expectedItem.getShoppingListItemId(), actualItem.getShoppingListItem().getId());
        assertEquals(expectedItem.getStatus(), actualItem.getStatus());
        assertEquals(expectedItem.getDateCompleted(), actualItem.getDateCompleted());
    }
}
