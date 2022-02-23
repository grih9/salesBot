package database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShopTest {
    private final String NAME = "name";
    private final String WEBSITE = "website";

    private Shop shop;

    @BeforeEach
    public void setShop() {
        shop = new Shop(NAME, WEBSITE);
    }

    @Test
    public void getName() {
        Assertions.assertEquals(shop.getName(), NAME);
    }

    @Test
    public void setName() {
        String newName = "newName";
        shop.setName(newName);
        Assertions.assertEquals(shop.getName(), newName);
    }

    @Test
    public void getWebsite() {
        Assertions.assertEquals(shop.getWebsite(), WEBSITE);
    }

    @Test
    public void setWebsite() {
        String newWebsite = "newWebsite";
        shop.setWebsite(newWebsite);
        Assertions.assertEquals(shop.getWebsite(), newWebsite);
    }

    @Test
    void testEqual() {
        Shop shop1 = new Shop(NAME, WEBSITE);
        Shop shop2 = new Shop(NAME, WEBSITE);
        Assertions.assertEquals(shop1, shop2);
    }

    @Test
    void testHashCode() {
        Shop shop1 = new Shop(NAME, WEBSITE);
        Shop shop2 = new Shop(NAME, WEBSITE);
        Assertions.assertNotSame(shop1, shop2);
        Assertions.assertEquals(shop1.hashCode(), shop2.hashCode());
    }
}
