package greencity.filters;

import greencity.annotations.RatingCalculationEnum;
import greencity.entity.RatingStatistics;
import greencity.entity.RatingStatistics_;
import greencity.entity.User;
import greencity.entity.User_;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingStatisticsSpecificationTest {

    @Mock
    private Root<RatingStatistics> root;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path path;

    @Mock
    private Join<RatingStatistics, User> userJoin;

    @Mock
    private Predicate predicate;

    private RatingStatisticsSpecification createSpecification(String key, Object value, String type) {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(key)
                .value(value)
                .type(type)
                .build();
        List<SearchCriteria> criteriaList = List.of(searchCriteria);
        return new RatingStatisticsSpecification(criteriaList);
    }

    @Test
    void toPredicate_UserIdCriteria_GeneratesCorrectPredicate() {
        RatingStatisticsSpecification specification = createSpecification("user.id", 1, "userId");

        when(root.join(RatingStatistics_.user)).thenReturn(userJoin);
        when(userJoin.get(User_.id)).thenReturn(path);
        when(criteriaBuilder.equal(path, 1)).thenReturn(predicate);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.and(predicate, predicate)).thenReturn(predicate);

        Predicate result = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertNotNull(result, "Predicate shouldn't be null");
        verify(criteriaBuilder).equal(path, 1);
    }

    @Test
    void toPredicate_UserMailCriteria_GeneratesCorrectPredicate() {
        RatingStatisticsSpecification specification = createSpecification("user.email", "test@example.com", "userMail");

        when(root.join(RatingStatistics_.user)).thenReturn(userJoin);
        when(userJoin.get(User_.email)).thenReturn(path);
        when(criteriaBuilder.like(path, "%test@example.com%")).thenReturn(predicate);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.and(predicate, predicate)).thenReturn(predicate);

        Predicate result = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertNotNull(result, "Predicate shouldn't be null");
        verify(criteriaBuilder).like(path, "%test@example.com%");
    }

    @Test
    void toPredicate_EnumCriteria_GeneratesCorrectPredicate() {
        RatingStatisticsSpecification specification = createSpecification("event", "WIN", "enum");

        List<RatingCalculationEnum> selectedEnums = Arrays.stream(RatingCalculationEnum.values())
                .filter(x -> x.toString().toLowerCase().contains("win".toLowerCase()))
                .toList();

        Predicate disjunctionPredicate = mock(Predicate.class);
        when(criteriaBuilder.disjunction()).thenReturn(disjunctionPredicate);

        for (RatingCalculationEnum enumValue : selectedEnums) {
            Predicate enumPredicate = mock(Predicate.class);
            when(criteriaBuilder.equal(root.get("event"), enumValue)).thenReturn(enumPredicate);
            when(criteriaBuilder.or(disjunctionPredicate, enumPredicate)).thenReturn(disjunctionPredicate);
        }

        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.and(predicate, disjunctionPredicate)).thenReturn(predicate);

        Predicate result = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertNotNull(result, "Predicate shouldn't be null");

        verify(criteriaBuilder).disjunction();
        for (RatingCalculationEnum enumValue : selectedEnums) {
            verify(criteriaBuilder).equal(root.get("event"), enumValue);
            verify(criteriaBuilder).or(disjunctionPredicate, criteriaBuilder.equal(root.get("event"), enumValue));
        }
    }
}