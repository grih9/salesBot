package bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemTest {
    private final String NAME = "name";
    private final int WEIGHT = 1000;
    private final String PRICE = "500 Р";
    private final String SALE_PRICE = "150 Р";
    private final String SALE_BEGIN_DATE = "20.12.2021";
    private final String SALE_END_DATE = "20.10.2022";
    private final String SHOP_NAME = "Пятёрочка";
    private final String IMAGE_URL = "https://mykaleidoscope.ru/uploads/posts/2021-09/1632416216_56-mykaleidoscope-ru-" +
            "p-shokoladnii-tort-s-yagodami-krasivo-foto-57.jpg";
    private String itemString = "Название: " + NAME + "\n" +
            "Вес: " + WEIGHT + "\n" +
            "Цена без скидки: " + PRICE + "\n" +
            "Цена со скидкой: " + SALE_PRICE + "\n" +
            "Начало акции: " + SALE_BEGIN_DATE + "\n" +
            "Окончание акции: " + SALE_END_DATE + "\n" +
            "Сеть: " + SHOP_NAME + "\n" + IMAGE_URL;
    private Item item;

    @BeforeEach
    public void setItem() {
        item = new Item(NAME, WEIGHT, PRICE, SALE_PRICE, SALE_BEGIN_DATE, SALE_END_DATE, SHOP_NAME, IMAGE_URL);
    }

    @Test
    public void getName() {
        Assertions.assertEquals(item.getName(), NAME);
    }

    @Test
    public void setName() {
        String newName = "newName";
        item.setName(newName);
        Assertions.assertEquals(item.getName(), newName);
    }

    @Test
    public void getWeight() {
        Assertions.assertEquals(item.getWeight(), WEIGHT);
    }

    @Test
    public void setWeight() {
        int newWeight = 5000;
        item.setWeight(newWeight);
        Assertions.assertEquals(item.getWeight(), newWeight);
    }

    @Test
    public void getPrice() {
        Assertions.assertEquals(item.getPrice(), PRICE);
    }

    @Test
    public void setPrice() {
        String newPrice = "2500 Р";
        item.setPrice(newPrice);
        Assertions.assertEquals(item.getPrice(), newPrice);
    }

    @Test
    public void getSalePrice() {
        Assertions.assertEquals(item.getSalePrice(), SALE_PRICE);
    }

    @Test
    public void setSalePrice() {
        String newSalePrice = "750 Р";
        item.setSalePrice(newSalePrice);
        Assertions.assertEquals(item.getSalePrice(), newSalePrice);
    }

    @Test
    public void getSaleBeginDate() {
        Assertions.assertEquals(item.getSaleBeginDate(), SALE_BEGIN_DATE);
    }

    @Test
    public void setSaleBeginDate() {
        String newSaleBeginDate = "11.11.2021";
        item.setSaleBeginDate(newSaleBeginDate);
        Assertions.assertEquals(item.getSaleBeginDate(), newSaleBeginDate);
    }

    @Test
    public void getSaleEndDate() {
        Assertions.assertEquals(item.getSaleEndDate(), SALE_END_DATE);
    }

    @Test
    public void setSaleEndDate() {
        String newSaleEndDate = "11.11.2022";
        item.setSaleEndDate(newSaleEndDate);
        Assertions.assertEquals(item.getSaleEndDate(), newSaleEndDate);
    }

    @Test
    public void getShopName() {
        Assertions.assertEquals(item.getShopName(), SHOP_NAME);
    }

    @Test
    public void setShopName() {
        String newShopName = "Перекрёсток";
        item.setShopName(newShopName);
        Assertions.assertEquals(item.getShopName(), newShopName);
    }

    @Test
    public void getImageURL() {
        Assertions.assertEquals(item.getImageURL(), IMAGE_URL);
    }

    @Test
    public void setImageURL() {
        String newImageUrl = "https://www.culture.ru/storage/images/f89f98f6bf3b5b7577b24ac11f8bcb85/" +
                "4b4eac459d4ef69aa9774cdbec0f2de1.jpeg";
        item.setImageURL(newImageUrl);
        Assertions.assertEquals(item.getImageURL(), newImageUrl);
    }

    @Test
    public void testToStringWithAllFields() {
        setItem();
        Assertions.assertEquals(item.toString(), itemString);
    }

    @Test
    public void testToStringWithNullableFields() {
        setItem();
        Assertions.assertEquals(item.toString(), itemString);

        item.setWeight(0);
        itemString = itemString.replace("Вес: " + WEIGHT + "\n", "");
        Assertions.assertEquals(item.toString(), itemString);

        item.setPrice(null);
        itemString = itemString.replace("Цена без скидки: " + PRICE + "\n", "");
        Assertions.assertEquals(item.toString(), itemString);

        item.setSaleBeginDate(null);
        itemString = itemString.replace("Начало акции: " + SALE_BEGIN_DATE + "\n", "");
        Assertions.assertEquals(item.toString(), itemString);

        item.setSaleEndDate(null);
        itemString = itemString.replace("Окончание акции: " + SALE_END_DATE + "\n", "");
        Assertions.assertEquals(item.toString(), itemString);

        item.setName(null);
        Assertions.assertNotEquals(item.toString(), itemString.replace("Название: " + NAME + "\n", ""));

        item.setName(NAME);
        item.setSalePrice(null);
        Assertions.assertNotEquals(item.toString(), itemString.replace("Цена со скидкой: " + SALE_PRICE + "\n", ""));

        item.setSalePrice(SALE_PRICE);
        item.setShopName(null);
        Assertions.assertNotEquals(item.toString(), itemString.replace("Сеть: " + SHOP_NAME + "\n", ""));

        item.setShopName(SHOP_NAME);
        item.setImageURL(null);
        Assertions.assertNotEquals(item.toString(), itemString.replace(IMAGE_URL, ""));
    }

    @Test
    public void testCompareTo() {
        setItem();
        Item itemToCompare = new Item();
        itemToCompare.setSalePrice("500 Р");
        Assertions.assertEquals(-1, item.compareTo(itemToCompare));

        itemToCompare.setSalePrice("50 Р");
        Assertions.assertEquals(1, item.compareTo(itemToCompare));

        itemToCompare.setSalePrice("150 Р");
        Assertions.assertEquals(0, item.compareTo(itemToCompare));
    }

    @Test
    void testHashCode() {
        Item item1 = new Item();
        Item item2 = new Item();
        Assertions.assertNotSame(item1, item2);
        Assertions.assertEquals(item1.hashCode(), item2.hashCode());
    }
}
