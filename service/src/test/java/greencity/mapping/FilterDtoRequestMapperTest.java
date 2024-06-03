package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.user.UserFilterDtoRequest;
import greencity.entity.Filter;
import greencity.enums.FilterType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FilterDtoRequestMapperTest {

    @InjectMocks
    private FilterDtoRequestMapper mapper;

    @Test
    void convert() {
        UserFilterDtoRequest dto = ModelUtils.getUserFilterDtoRequest();

        Filter result = mapper.convert(dto);

        assertNotNull(result);
        assertEquals("Test_Filter", result.getName());
        assertEquals(FilterType.USERS.toString(), result.getType());
        assertEquals("Test;USER;ACTIVATED", result.getValues());    }
}