package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.language.LanguageTranslationDTO;
import greencity.entity.HabitFactTranslation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LanguageTranslationDtoMapperTest {

    @InjectMocks
    private LanguageTranslationDtoMapper mapper;

    @Test
    @DisplayName("Test convert from HabitFactTranslation to LanguageTranslationDTO")
    void convert() {
        HabitFactTranslation habitFactTranslation = ModelUtils.getHabitFactTranslation();
        LanguageTranslationDTO result = mapper.convert(habitFactTranslation);

        assertNotNull(result);
        assertEquals(habitFactTranslation.getLanguage().getCode(), result.getLanguage().getCode());
        assertEquals(habitFactTranslation.getContent(), result.getContent());


    }
}