package greencity.filters;

import greencity.entity.*;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitFactSpecificationTest {

    @Mock
    private Root<HabitFact> root;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path path;

    @Mock
    private Path<Long> pathLong;

    @Mock
    private Path<HabitFact> habitFactPath;

    @Mock
    private Predicate predicate;

    @Mock
    private Root<HabitFactTranslation> habitFactTranslationRoot;

    private HabitFactSpecification createSpecification(String key, String type) {
        SearchCriteria searchCriteria = new SearchCriteria(key, "1", type);
        List<SearchCriteria> criteriaList = List.of(searchCriteria);
        return new HabitFactSpecification(criteriaList);
    }

    @Test
    void toPredicate_IdCriteria_GeneratesCorrectPredicate() {
        HabitFactSpecification specification = createSpecification("1", "id");

        when(root.get("1")).thenReturn(path);
        when(criteriaBuilder.equal(path, "1")).thenReturn(predicate);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.and(predicate, predicate)).thenReturn(predicate);

        Predicate result = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertNotNull(result, "Predicate should not be null");
        verify(criteriaBuilder).equal(path, "1");
    }

    @Test
    void toPredicate_HabitIdCriteria_GeneratesCorrectPredicate() {
        HabitFactSpecification specification = createSpecification("1", "habitId");

        Join<HabitFact, Habit> habitJoin = mock(Join.class);
        when(root.join(HabitFact_.habit)).thenReturn(habitJoin);
        when(criteriaBuilder.equal(habitJoin.get(Habit_.id), "1")).thenReturn(predicate);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.and(predicate, predicate)).thenReturn(predicate);

        Predicate result = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertNotNull(result, "Predicate should not be null");
        verify(criteriaBuilder).equal(habitJoin.get(Habit_.id), "1");
    }

    @Test
    void toPredicate_ContentCriteria_GeneratesCorrectPredicate() {
        HabitFactSpecification specification = createSpecification("test", "content");

        when(criteriaQuery.from(HabitFactTranslation.class)).thenReturn(habitFactTranslationRoot);
        when(criteriaBuilder.like(any(Path.class), eq("%test%"))).thenReturn(predicate);
        when(root.get(HabitFact_.id)).thenReturn(pathLong);
        when(habitFactTranslationRoot.get(HabitFactTranslation_.habitFact)).thenReturn(habitFactPath);
        when(habitFactPath.get(HabitFact_.id)).thenReturn(pathLong);
        when(criteriaBuilder.equal(pathLong, pathLong)).thenReturn(predicate);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.and(predicate, predicate)).thenReturn(predicate);

        Predicate result = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertNotNull(result, "Predicate should not be null");
        verify(criteriaBuilder).like(any(Path.class), eq("%test%"));
        verify(criteriaBuilder).equal(pathLong, pathLong);
    }
}