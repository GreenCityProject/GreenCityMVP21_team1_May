package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.category.CategoryDto;
import greencity.entity.Category;
import greencity.repository.CategoryRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
class CategoryDtoMapperTest {

    @InjectMocks
    private CategoryDtoMapper mapper;


    @Test
    @DisplayName("Converting CategoryDto to Category")
    void convertTest() {

        Category expected = Category.builder()
                .name("Some Test Name")
                //.nameUa("Переклад")
                .build();
        CategoryDto toBeConverted = CategoryDto.builder()
                .name(expected.getName())
                //.nameUa(expected.getNameUa())
                .build();

        assertEquals(expected, mapper.convert(toBeConverted));

    }
}