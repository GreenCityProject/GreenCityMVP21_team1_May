package greencity.mapping;

import greencity.dto.shoppinglistitem.ShoppingListItemRequestDto;
import greencity.entity.UserShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemRequestDtoMapperTest {

    @InjectMocks
    private ShoppingListItemRequestDtoMapper mapper;

    @Test
    @DisplayName("Test convert from ShoppingListItemRequestDto to UserShoppingListItem")
    void convert() {
        ShoppingListItemRequestDto shoppingListItemRequestDto = ShoppingListItemRequestDto.builder()
                .id(1L)
                .build();

        UserShoppingListItem result = mapper.convert(shoppingListItemRequestDto);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertNotNull(result.getShoppingListItem(), "ShoppingListItem should not be null"),
                () -> assertEquals(shoppingListItemRequestDto.getId(), result.getShoppingListItem().getId(), "IDs should match"),
                () -> assertEquals(ShoppingListItemStatus.ACTIVE, result.getStatus(), "Status should be ACTIVE")
        );
    }
}
