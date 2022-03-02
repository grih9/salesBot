package database;

import bot.Item;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JDBCConnectorTest {
    static JDBCConnector jdbc = new JDBCConnector(true);

    @BeforeAll
    static public void beforeAll() {
        jdbc.createTables();
    }

    @AfterEach
    public void truncateTables() {
        jdbc.truncateTables();
    }

    @Test
    public void connectionIsNotNull() {
        Assertions.assertNotNull(jdbc.getConnection());
    }

    @Test
    public void addUser() {
        jdbc.addUser("Denis");
        Assertions.assertNotNull(jdbc.getUserId("Denis"));
    }

    @Test
    public void deleteUser() {
        Assertions.assertTrue(jdbc.addUser("Denis"));
        Assertions.assertTrue(jdbc.deleteUser("Denis"));
        Assertions.assertNull(jdbc.getUserId("Denis"));
    }

    @Test
    public void addUserAfterDelete() {
        Assertions.assertTrue(jdbc.addUser("Denis"));
        Assertions.assertTrue(jdbc.deleteUser("Denis"));
        Assertions.assertTrue(jdbc.addUser("Denis"));
    }

    @Test
    public void addAlreadyExistedUser() {
        jdbc.addUser("Denis");
        Assertions.assertFalse(jdbc.addUser("Denis"));
    }

    @Test
    public void addCity() {
        Assertions.assertTrue(jdbc.addUser("Denis"));
        Assertions.assertTrue(jdbc.addCities());
        Assertions.assertTrue(jdbc.addCity("Denis", "Санкт-Петербург"));
        Assertions.assertEquals(jdbc.getUserCity("Denis").getName(), "Санкт-Петербург");
    }

    @Test
    public void addCities() {
        List<City> cities = JDBCUtils.getCitiesFromCSV();
        Assertions.assertTrue(jdbc.addCities());
        Assertions.assertEquals(jdbc.getCities(), cities);
    }

    @Test
    public void addCategories() {
        List<String> categories = JDBCUtils.getCategoriesFromCSV();
        Assertions.assertTrue(jdbc.addCategories());
        Assertions.assertEquals(jdbc.getCategories(), categories);
    }

    @Test
    public void addShops() {
        List<Shop> shops = JDBCUtils.getShopsFromCSV();
        Assertions.assertTrue(jdbc.addShops());
        Assertions.assertEquals(jdbc.getShops(), shops);
    }

    @Test
    public void setNullSelectedShopsList() {
        jdbc.addUser("Denis");

        Assertions.assertFalse(jdbc.setSelectedShops("Denis", null));
        Assertions.assertTrue(jdbc.getSelectedShops("Denis").isEmpty());
    }

    @Test
    public void setAllSelectedShops() {
        jdbc.addUser("Denis");

        List<Shop> shops = JDBCUtils.getShopsFromCSV();
        jdbc.addShops();

        Assertions.assertTrue(jdbc.setSelectedShops("Denis", shops));
        Assertions.assertEquals(jdbc.getSelectedShops("Denis"), shops);
    }

    @Test
    public void setZeroSelectedShops() {
        jdbc.addUser("Denis");

        List<Shop> shops = new ArrayList<>();
        jdbc.addShops();

        Assertions.assertTrue(jdbc.setSelectedShops("Denis", shops));
        Assertions.assertEquals(jdbc.getSelectedShops("Denis"), shops);
    }

    @Test
    public void setHalfSelectedShops() {
        jdbc.addUser("Denis");

        jdbc.addShops();
        List<Shop> selectedShops = new ArrayList<>();
        List<Shop> shops = JDBCUtils.getShopsFromCSV();

        for (int i = 0; i <  shops.size(); i+=2) {
            selectedShops.add(shops.get(i));
        }

        Assertions.assertTrue(jdbc.setSelectedShops("Denis", selectedShops));
        Assertions.assertEquals(jdbc.getSelectedShops("Denis"), selectedShops);
    }

    @Test
    public void addNullItems() {
        jdbc.addShops();
        jdbc.addCategories();

        List<String> categories = JDBCUtils.getCategoriesFromCSV();
        List<Shop> shops = JDBCUtils.getShopsFromCSV();

        Assertions.assertFalse(jdbc.addItems(categories.get(0), shops.get(0), null));
        Assertions.assertTrue(jdbc.getItemsByCategory(categories.get(0), shops.get(0)).isEmpty());
    }

    private static Stream<Arguments> shopAndCategoriesProvider() {
        List<String> categories = JDBCUtils.getCategoriesFromCSV();
        List<Shop> shops = JDBCUtils.getShopsFromCSV();
        List<Arguments> arguments = new ArrayList<>();

        for (int i = 0; i < shops.size(); i++) {
            arguments.add(Arguments.of(shops.get(i), categories.get(i % categories.size())));
        }

        return arguments.stream();
    }

    @ParameterizedTest
    @MethodSource("shopAndCategoriesProvider")
    public void getItemsByCategory(Shop shop, String category) {

        jdbc.addShops();
        jdbc.addCategories();

        List<Item> items = new ArrayList<>();
        items.add(new Item("Сыр БЕЛЕБЕЕВСКИЙ полутвердый, 45%, 450 г", 0, "409.10", "269.99",
                null, "9 марта", shop.getName(),
                "https://leonardo.edadeal.io/dyn/cr/catalyst/offers/zw7i4b6r6jmymjlifaxg7z346i.jpg"));
        items.add(new Item("Сыр TOPN TASTE, топпинг Чеддер, классический, 160 г", 0, "149.99", "99.99",
                null, "9 марта", shop.getName(),
                "https://leonardo.edadeal.io/dyn/cr/catalyst/offers/rdvxulvl5llkp76lzgc7w5obgu.jpg"));

        Assertions.assertTrue(jdbc.addItems(category, shop, items));
        Assertions.assertEquals(jdbc.getItemsByCategory(category, shop), items);
    }

    @ParameterizedTest
    @MethodSource("shopAndCategoriesProvider")
    public void getItemsByName(Shop shop, String category) {
        String name = "Сыр";
        List<Item> items = new ArrayList<>();
        items.add(new Item("Сыр БЕЛЕБЕЕВСКИЙ полутвердый, 45%, 450 г", 0, "409.10", "269.99",
                null, "9 марта", shop.getName(),
                "https://leonardo.edadeal.io/dyn/cr/catalyst/offers/zw7i4b6r6jmymjlifaxg7z346i.jpg"));
        items.add(new Item("TOPN TASTE, топпинг Чеддер, классический, 160 г", 0, "149.99", "99.99",
                null, "9 марта", shop.getName(),
                "https://leonardo.edadeal.io/dyn/cr/catalyst/offers/rdvxulvl5llkp76lzgc7w5obgu.jpg"));

        items = items.stream().filter(e -> e.getName().contains(name.toLowerCase())).collect(Collectors.toList());

        Assertions.assertTrue(jdbc.addItems(category, shop, items));
        Assertions.assertEquals(jdbc.getItemsByCategory(category, shop), items);
    }
}