package database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CityTest {
    private final String NAME = "name";
    private final String REGION = "region";

    private City city;

    @BeforeEach
    public void setCity() {
        city = new City(NAME, REGION);
    }

    @Test
    public void getName() {
        Assertions.assertEquals(city.getName(), NAME);
    }

    @Test
    public void setName() {
        String newName = "newName";
        city.setName(newName);
        Assertions.assertEquals(city.getName(), newName);
    }

    @Test
    public void getRegion() {
        Assertions.assertEquals(city.getRegion(), REGION);
    }

    @Test
    public void setRegion() {
        String newRegion = "newRegion";
        city.setRegion(newRegion);
        Assertions.assertEquals(city.getRegion(), newRegion);
    }

    @Test
    void testEqual() {
        City city1 = new City(NAME, REGION);
        City city2 = new City(NAME, REGION);
        Assertions.assertEquals(city1, city2);
    }

    @Test
    void testHashCode() {
        City city1 = new City(NAME, REGION);
        City city2 = new City(NAME, REGION);
        Assertions.assertNotSame(city1, city2);
        Assertions.assertEquals(city1.hashCode(), city2.hashCode());
    }
}
