package greencity.mapping;

import greencity.dto.shoppinglistitem.ShoppingListItemDto;
import greencity.entity.localization.ShoppingListItemTranslation;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static greencity.ModelUtils.getShoppingListItemTranslations;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemDtoMapperTest {

    @InjectMocks
    private ShoppingListItemDtoMapper mapper;

    @Test
    @DisplayName("Test convert from ShoppingListItemTranslation to ShoppingListItemDto")
    void convert() {
        List<ShoppingListItemTranslation> translations = getShoppingListItemTranslations();

        for (ShoppingListItemTranslation translation : translations) {
            ShoppingListItemDto result = mapper.convert(translation);

            assertAll(
                    () -> assertNotNull(result, "Result should not be null"),
                    () -> assertEquals(translation.getShoppingListItem().getId(), result.getId(), "IDs should match"),
                    () -> assertEquals(translation.getContent(), result.getText(), "Content should match"),
                    () -> assertEquals(ShoppingListItemStatus.ACTIVE.toString(), result.getStatus(), "Status should be ACTIVE")
            );
        }
    }
}
