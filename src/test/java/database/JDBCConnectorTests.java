package database;

import bot.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JDBCConnectorTests {
    @BeforeAll
    static public void initAll() {
        JDBCConnector.setTestConn();
        JDBCConnector.createTables();
    }

    @Test
    public void connectionIsNotNull() {
        Assertions.assertNotNull(JDBCConnector.getConnection());
    }

    @Test
    public void addUser() {
        JDBCConnector.addUser("Denis");
        Assertions.assertNotNull(JDBCConnector.getUserId("Denis"));
        JDBCConnector.truncateTables();
    }

    @Test
    public void addAlreadyExistedUser() {
        JDBCConnector.addUser("Denis");
        Assertions.assertFalse(JDBCConnector.addUser("Denis"));

        JDBCConnector.truncateTables();
    }

    @Test
    public void addCity() {
        JDBCConnector.addUser("Denis");
        JDBCConnector.addCities();
        JDBCConnector.addCity("Denis", "Санкт-Петербург");

        Assertions.assertEquals(JDBCConnector.getUserCity("Denis").getName(), "Санкт-Петербург");

        JDBCConnector.truncateTables();
    }

    @Test
    public void addCities() {
        List<City> cities = JDBCUtils.getCitiesFromCSV();
        Assertions.assertTrue(JDBCConnector.addCities());
        Assertions.assertEquals(JDBCConnector.getCities(), cities);

        JDBCConnector.truncateTables();
    }

    @Test
    public void addCategories() {
        List<String> categories = JDBCUtils.getCategoriesFromCSV();
        Assertions.assertTrue(JDBCConnector.addCategories());
        Assertions.assertEquals(JDBCConnector.getCategories(), categories);

        JDBCConnector.truncateTables();
    }

    @Test
    public void addShops() {
        List<Shop> shops = JDBCUtils.getShopsFromCSV();
        Assertions.assertTrue(JDBCConnector.addShops());
        Assertions.assertEquals(JDBCConnector.getShops(), shops);

        JDBCConnector.truncateTables();
    }

    @Test
    public void setNullSelectedShopsList() {
        JDBCConnector.addUser("Denis");

        Assertions.assertFalse(JDBCConnector.setSelectedShops("Denis", null));
        Assertions.assertTrue(JDBCConnector.getSelectedShops("Denis").isEmpty());

        JDBCConnector.truncateTables();
    }

    @Test
    public void setAllSelectedShops() {
        JDBCConnector.addUser("Denis");

        List<Shop> shops = JDBCUtils.getShopsFromCSV();
        JDBCConnector.addShops();

        Assertions.assertTrue(JDBCConnector.setSelectedShops("Denis", shops));
        Assertions.assertEquals(JDBCConnector.getSelectedShops("Denis"), shops);

        JDBCConnector.truncateTables();
    }

    @Test
    public void setZeroSelectedShops() {
        JDBCConnector.addUser("Denis");

        List<Shop> shops = new ArrayList<>();
        JDBCConnector.addShops();

        Assertions.assertTrue(JDBCConnector.setSelectedShops("Denis", shops));
        Assertions.assertEquals(JDBCConnector.getSelectedShops("Denis"), shops);

        JDBCConnector.truncateTables();
    }

    @Test
    public void setHalfSelectedShops() {
        JDBCConnector.addUser("Denis");

        JDBCConnector.addShops();
        List<Shop> selectedShops = new ArrayList<>();
        List<Shop> shops = JDBCUtils.getShopsFromCSV();

        for (int i = 0; i <  shops.size(); i+=2) {
            selectedShops.add(shops.get(i));
        }

        Assertions.assertTrue(JDBCConnector.setSelectedShops("Denis", selectedShops));
        Assertions.assertEquals(JDBCConnector.getSelectedShops("Denis"), selectedShops);

        JDBCConnector.truncateTables();
    }

    @Test
    public void addNullItems() {
        JDBCConnector.addShops();
        JDBCConnector.addCategories();

        List<String> categories = JDBCUtils.getCategoriesFromCSV();
        List<Shop> shops = JDBCUtils.getShopsFromCSV();

        Assertions.assertFalse(JDBCConnector.addItems(categories.get(0), shops.get(0), null));
        Assertions.assertTrue(JDBCConnector.getItemsByCategory(categories.get(0), shops.get(0)).isEmpty());

        JDBCConnector.truncateTables();
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

        JDBCConnector.addShops();
        JDBCConnector.addCategories();

        List<Item> items = new ArrayList<>();
        items.add(new Item("Сыр БЕЛЕБЕЕВСКИЙ полутвердый, 45%, 450 г", 0, "409.10", "269.99",
                null, "9 марта", shop.getName(),
                "https://leonardo.edadeal.io/dyn/cr/catalyst/offers/zw7i4b6r6jmymjlifaxg7z346i.jpg"));
        items.add(new Item("Сыр TOPN TASTE, топпинг Чеддер, классический, 160 г", 0, "149.99", "99.99",
                null, "9 марта", shop.getName(),
                "https://leonardo.edadeal.io/dyn/cr/catalyst/offers/rdvxulvl5llkp76lzgc7w5obgu.jpg"));

        Assertions.assertTrue(JDBCConnector.addItems(category, shop, items));
        Assertions.assertEquals(JDBCConnector.getItemsByCategory(category, shop), items);

        JDBCConnector.truncateTables();
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

        Assertions.assertTrue(JDBCConnector.addItems(category, shop, items));
        Assertions.assertEquals(JDBCConnector.getItemsByCategory(category, shop), items);

        JDBCConnector.truncateTables();
    }
}