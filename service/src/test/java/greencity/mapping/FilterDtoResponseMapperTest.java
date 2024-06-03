package greencity.mapping;

import greencity.dto.user.UserFilterDtoResponse;
import greencity.entity.Filter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.getFilter;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FilterDtoResponseMapperTest {

    @InjectMocks
    private FilterDtoResponseMapper mapper;

    @Test
    @DisplayName("Test convert from Filter to UserFilterDtoResponse")
    void convert() {
        Filter filter = getFilter();

        UserFilterDtoResponse result = mapper.convert(filter);

        assertNotNull(result);
        assertEquals(filter.getId(), result.getId());
        assertEquals(filter.getName(), result.getName());
        assertEquals("Test", result.getSearchCriteria());
        assertEquals("ADMIN", result.getUserRole());
        assertEquals("ACTIVATED", result.getUserStatus());
    }
}