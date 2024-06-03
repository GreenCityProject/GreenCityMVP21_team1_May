package greencity.mapping;

import greencity.dto.habit.HabitDto;
import greencity.dto.habittranslation.HabitTranslationDto;
import greencity.dto.shoppinglistitem.ShoppingListItemDto;
import greencity.entity.HabitTranslation;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static greencity.ModelUtils.getHabitTranslation;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitDtoMapperTest {

    @InjectMocks
    private HabitDtoMapper mapper;

    @Test
    @DisplayName("Test convert from HabitTranslation to HabitDto")
    void convert() {
        HabitTranslation habitTranslation = getHabitTranslation();

        HabitDto result = mapper.convert(habitTranslation);
        List<String> tags = result.getTags();
        HabitTranslationDto habitTranslationDto = result.getHabitTranslation();
        List<ShoppingListItemDto> shoppingListItems = result.getShoppingListItems();

        assertNotNull(result);
        assertEquals(habitTranslation.getHabit().getId(), result.getId());
        assertEquals(habitTranslation.getHabit().getImage(), result.getImage());
        assertEquals(habitTranslation.getHabit().getDefaultDuration(), result.getDefaultDuration());
        assertEquals(habitTranslation.getHabit().getComplexity(), result.getComplexity());
        assertNotNull(habitTranslationDto);
        assertEquals(habitTranslation.getName(), habitTranslationDto.getName());
        assertEquals(habitTranslation.getDescription(), habitTranslationDto.getDescription());
        assertEquals(habitTranslation.getHabitItem(), habitTranslationDto.getHabitItem());
        assertEquals(habitTranslation.getLanguage().getCode(), habitTranslationDto.getLanguageCode());
        assertNotNull(tags);
        assertEquals(1, tags.size());
        assertEquals("News", tags.getFirst());
        assertNotNull(shoppingListItems);
        assertEquals(1, shoppingListItems.size());
        ShoppingListItemDto shoppingListItemDto = shoppingListItems.getFirst();
        assertEquals(1L, shoppingListItemDto.getId());
        assertEquals(ShoppingListItemStatus.ACTIVE.toString(), shoppingListItemDto.getStatus());
        assertEquals("Buy a bamboo toothbrush", shoppingListItemDto.getText());
    }
}
