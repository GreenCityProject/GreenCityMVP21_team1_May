package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.category.CategoryDtoResponse;
import greencity.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CategoryDtoResponseMapperTest {

    @InjectMocks
    private CategoryDtoResponseMapper mapper;

    @Test
    @DisplayName("Test converting Category to CategoryDtoResponse")
    void convert() {

        Category toBeConverted = ModelUtils.getCategoryWithNameId();

        CategoryDtoResponse expected = CategoryDtoResponse.builder()
                .id(toBeConverted.getId())
                .name(toBeConverted.getName())
                .build();



        assertEquals(expected,mapper.convert(toBeConverted));
    }
}