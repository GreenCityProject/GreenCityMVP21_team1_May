package greencity.mapping;

import greencity.dto.shoppinglistitem.CustomShoppingListItemResponseDto;
import greencity.entity.CustomShoppingListItem;
import greencity.entity.ShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static greencity.mapping.UtilsMapper.map;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomShoppingListMapperTest {

    @InjectMocks
    private CustomShoppingListMapper mapper;

    @Test
    @DisplayName("Test Convert CustomShoppingListItemResponseDto to CustomShoppingListItem")
    void convert() {
        CustomShoppingListItemResponseDto toBeConverted = CustomShoppingListItemResponseDto.builder()
                .id(2L)
                .text("Test text")
                .status(ShoppingListItemStatus.ACTIVE)
                .build();

        CustomShoppingListItem expected = CustomShoppingListItem.builder()
                .id(toBeConverted.getId())
                .text(toBeConverted.getText())
                .status(toBeConverted.getStatus())
                .build();

        assertEquals(expected, mapper.convert(toBeConverted));
    }

    @Test
    @DisplayName("Test Convert List of CustomShoppingListItemResponseDto to List of CustomShoppingListItem")
    void mapAllToList() {

        CustomShoppingListItemResponseDto toBeConvertedItem1 = CustomShoppingListItemResponseDto.builder()
                .id(2L)
                .text("Test text")
                .status(ShoppingListItemStatus.ACTIVE)
                .build();

        CustomShoppingListItemResponseDto toBeConvertedItem2 = CustomShoppingListItemResponseDto.builder()
                .id(3L)
                .text("Test text")
                .status(ShoppingListItemStatus.DISABLED)
                .build();

        List<CustomShoppingListItemResponseDto>  listToBeConverted = new ArrayList<>();
        listToBeConverted.add(toBeConvertedItem1);
        listToBeConverted.add(toBeConvertedItem2);

        CustomShoppingListItem expectedItem1 = mapper.convert(toBeConvertedItem1);
        CustomShoppingListItem expectedItem2 = mapper.convert(toBeConvertedItem2);
        List<CustomShoppingListItem> expected = new ArrayList<>();
        expected.add(expectedItem1);
        expected.add(expectedItem2);
        
        assertEquals(expected, mapper.mapAllToList(listToBeConverted));
    }
}