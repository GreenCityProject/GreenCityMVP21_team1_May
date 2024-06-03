package greencity.mapping;

import greencity.dto.econews.AddEcoNewsDtoRequest;
import greencity.entity.EcoNews;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static greencity.ModelUtils.getAddEcoNewsDtoRequest;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AddEcoNewsDtoRequestMapperTest {

    @InjectMocks
    private AddEcoNewsDtoRequestMapper mapper;

    @Test
    @DisplayName("Test convert from AddEcoNewsDtoRequest to EcoNews")
    void convert() {
        AddEcoNewsDtoRequest dto = getAddEcoNewsDtoRequest();

        EcoNews result = mapper.convert(dto);

        assertNotNull(result);
        assertEquals(dto.getSource(), result.getSource());
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getText(), result.getText());
        assertEquals(dto.getShortInfo(), result.getShortInfo());
        assertNotNull(result.getCreationDate());
        assertTrue(result.getCreationDate().isBefore(ZonedDateTime.now()) || result.getCreationDate().isEqual(ZonedDateTime.now()));
    }
}
