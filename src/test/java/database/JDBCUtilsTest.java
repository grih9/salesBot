package database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JDBCUtilsTest {
    @Test
    public void shopsListIsNotEmpty() {
        Assertions.assertNotEquals(JDBCUtils.getShopsFromCSV().size(), 0);
    }

    @Test
    public void categoriesListIsNotEmpty() {
        Assertions.assertNotEquals(JDBCUtils.getCategoriesFromCSV().size(), 0);
    }

    @Test
    public void citiesListIsNotEmpty() {
        Assertions.assertNotEquals(JDBCUtils.getCitiesFromCSV().size(), 0);
    }
}
