package greencity.filters;

import greencity.entity.ShoppingListItem;
import greencity.entity.ShoppingListItem_;
import greencity.entity.localization.ShoppingListItemTranslation;
import greencity.entity.localization.ShoppingListItemTranslation_;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemSpecificationTest {

    @Mock
    private Root<ShoppingListItem> root;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Mock
    private Path<Long> pathLong;

    @Mock
    private Root<ShoppingListItemTranslation> itemTranslationRoot;

    @Mock
    private Path<ShoppingListItem> shoppingListItemPath;

    @Test
    void toPredicate_ContentCriteria_GeneratesCorrectPredicate() {
        SearchCriteria searchCriteria = SearchCriteria.builder().value("test").type("content").build();
        List<SearchCriteria> searchCriteriaList = List.of(searchCriteria);
        ShoppingListItemSpecification specification = new ShoppingListItemSpecification(searchCriteriaList);

        when(criteriaQuery.from(ShoppingListItemTranslation.class)).thenReturn(itemTranslationRoot);
        when(criteriaBuilder.like(any(Path.class), eq("%test%"))).thenReturn(predicate);
        when(root.get(ShoppingListItem_.id)).thenReturn(pathLong);
        when(itemTranslationRoot.get(ShoppingListItemTranslation_.shoppingListItem)).thenReturn(shoppingListItemPath);
        when(shoppingListItemPath.get(ShoppingListItem_.id)).thenReturn(pathLong);
        when(criteriaBuilder.equal(pathLong, pathLong)).thenReturn(predicate);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.and(predicate, predicate)).thenReturn(predicate);

        Predicate result = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertNotNull(result, "Predicate shouldn't null");
        verify(criteriaQuery).from(ShoppingListItemTranslation.class);
        verify(criteriaBuilder).like(any(Path.class), eq("%test%"));
        verify(criteriaBuilder).equal(pathLong, pathLong);
    }
}
