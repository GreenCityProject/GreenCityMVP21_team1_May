package greencity.mapping;

import greencity.dto.shoppinglistitem.ShoppingListItemResponseDto;
import greencity.entity.ShoppingListItem;
import greencity.entity.localization.ShoppingListItemTranslation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemResponseDtoMapperTest {

    @InjectMocks
    private ShoppingListItemResponseDtoMapper mapper;

    @Test
    @DisplayName("Test convert from ShoppingListItem to ShoppingListItemResponseDto")
    void convert() {
        ShoppingListItemTranslation translation1 = ShoppingListItemTranslation.builder()
                .id(1L)
                .content("Translation 1")
                .build();

        ShoppingListItemTranslation translation2 = ShoppingListItemTranslation.builder()
                .id(2L)
                .content("Translation 2")
                .build();

        ShoppingListItem shoppingListItem = ShoppingListItem.builder()
                .id(1L)
                .translations(List.of(translation1, translation2))
                .build();

        ShoppingListItemResponseDto result = mapper.convert(shoppingListItem);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(shoppingListItem.getId(), result.getId(), "IDs should match"),
                () -> assertNotNull(result.getTranslations(), "Translations should not be null"),
                () -> assertEquals(2, result.getTranslations().size(), "Translations size should be 2"),
                () -> assertEquals(translation1.getId(), result.getTranslations().getFirst().getId(), "Translation 1 IDs should match"),
                () -> assertEquals(translation1.getContent(), result.getTranslations().getFirst().getContent(), "Translation 1 content should match"),
                () -> assertEquals(translation2.getId(), result.getTranslations().get(1).getId(), "Translation 2 IDs should match"),
                () -> assertEquals(translation2.getContent(), result.getTranslations().get(1).getContent(), "Translation 2 content should match")
        );
    }
}
