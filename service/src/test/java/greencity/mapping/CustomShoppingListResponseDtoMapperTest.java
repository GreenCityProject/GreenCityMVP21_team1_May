package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.shoppinglistitem.CustomShoppingListItemResponseDto;
import greencity.entity.CustomShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomShoppingListResponseDtoMapperTest {

    @InjectMocks
    private CustomShoppingListResponseDtoMapper mapper;

    @Test
    @DisplayName("Test mapping from CustomShoppingListItem to CustomShoppingListItemResponseDto")
    void convert() {

        CustomShoppingListItem customShoppingListItem = ModelUtils.getCustomShoppingListItem();
        CustomShoppingListItemResponseDto result = mapper.convert(customShoppingListItem);

        assert result != null;
        assertEquals(customShoppingListItem.getId(),result.getId());
        assertEquals( customShoppingListItem.getText(), result.getText());
        assertEquals(customShoppingListItem.getStatus(), result.getStatus());


    }

    @Test
    @DisplayName("Test mapping List of CustomShoppingListItem to List of CustomShoppingListItemResponseDto")
    void mapAllToList() {

        CustomShoppingListItem customShoppingListItem1 = CustomShoppingListItem.builder()
                .id(1L)
                .text("Buy milk1")
                .status(ShoppingListItemStatus.ACTIVE)
                .dateCompleted(LocalDateTime.now())
                .build();

        CustomShoppingListItem customShoppingListItem2 = CustomShoppingListItem.builder()
                .id(1L)
                .text("Buy milk2")
                .status(ShoppingListItemStatus.DISABLED)
                .dateCompleted(LocalDateTime.now())
                .build();

        List<CustomShoppingListItem> listToConvert = new ArrayList<>();
        listToConvert.add(customShoppingListItem1);
        listToConvert.add(customShoppingListItem2);

        List<CustomShoppingListItemResponseDto> toVerify = mapper.mapAllToList(listToConvert);


        assert ! toVerify.contains(null);
        assertEquals (2, toVerify.size() );
        assertEquals(customShoppingListItem1.getText(), toVerify.get(0).getText());
        assertEquals(customShoppingListItem2.getText(), toVerify.get(1).getText());
        assertEquals(ShoppingListItemStatus.ACTIVE, toVerify.get(0).getStatus());
        assertEquals(ShoppingListItemStatus.DISABLED, toVerify.get(1).getStatus());
    }
}