package greencity.mapping;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UtilsMapperTest {

    @Test
    @DisplayName("Test map single object")
    void map() {
        SourceClass source = new SourceClass("Test", 123);
        TargetClass result = UtilsMapper.map(source, TargetClass.class);

        assertNotNull(result);
        assertEquals(source.getName(), result.getName());
        assertEquals(source.getNumber(), result.getNumber());
    }

    @Test
    @DisplayName("Test map list of objects to list")
    void mapAllToList() {
        List<SourceClass> sourceList = List.of(
                new SourceClass("Test1", 123),
                new SourceClass("Test2", 456)
        );

        List<TargetClass> resultList = UtilsMapper.mapAllToList(sourceList, TargetClass.class);

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(sourceList.get(0).getName(), resultList.get(0).getName());
        assertEquals(sourceList.get(0).getNumber(), resultList.get(0).getNumber());
        assertEquals(sourceList.get(1).getName(), resultList.get(1).getName());
        assertEquals(sourceList.get(1).getNumber(), resultList.get(1).getNumber());
    }

    @Test
    @DisplayName("Test map list of objects to set")
    void mapAllToSet() {
        List<SourceClass> sourceList = List.of(
                new SourceClass("Test1", 123),
                new SourceClass("Test2", 456)
        );

        Set<TargetClass> resultSet = UtilsMapper.mapAllToSet(sourceList, TargetClass.class);

        assertNotNull(resultSet);
        assertEquals(2, resultSet.size());
        assertTrue(resultSet.stream().anyMatch(item -> "Test1".equals(item.getName()) && 123 == item.getNumber()));
        assertTrue(resultSet.stream().anyMatch(item -> "Test2".equals(item.getName()) && 456 == item.getNumber()));
    }

    @Getter
    private static class SourceClass {
        private final String name;
        private final int number;

        public SourceClass(String name, int number) {
            this.name = name;
            this.number = number;
        }

    }

    @Getter
    @Setter
    private static class TargetClass {
        private String name;
        private int number;

        public TargetClass() {
        }

        public TargetClass(String name, int number) {
            this.name = name;
            this.number = number;
        }
    }

}
