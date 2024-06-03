package greencity.mapping;

import greencity.dto.shoppinglistitem.ShoppingListItemWithStatusRequestDto;
import greencity.entity.UserShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemWithStatusRequestDtoMapperTest {

    @InjectMocks
    private ShoppingListItemWithStatusRequestDtoMapper mapper;

    @Test
    @DisplayName("Test convert from ShoppingListItemWithStatusRequestDto to UserShoppingListItem")
    void convert() {
        ShoppingListItemWithStatusRequestDto itemDto = ShoppingListItemWithStatusRequestDto.builder()
                .id(1L)
                .status(ShoppingListItemStatus.ACTIVE)
                .build();

        UserShoppingListItem result = mapper.convert(itemDto);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertNotNull(result.getShoppingListItem(), "ShoppingListItem should not be null"),
                () -> assertEquals(itemDto.getId(), result.getShoppingListItem().getId(), "Ids should match"),
                () -> assertEquals(itemDto.getStatus(), result.getStatus(), "Statuses should match")
        );
    }
}
