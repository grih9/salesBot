package bot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void setItem() {
        item = new Item(NAME, WEIGHT, PRICE, SALE_PRICE, SALE_BEGIN_DATE, SALE_END_DATE, SHOP_NAME, IMAGE_URL);
    }

    @Test
    public void getName() {
        Assert.assertEquals(item.getName(), NAME);
    }

    @Test
    public void setName() {
        String newName = "newName";
        item.setName(newName);
        Assert.assertEquals(item.getName(), newName);
    }

    @Test
    public void getWeight() {
        Assert.assertEquals(item.getWeight(), WEIGHT);
    }

    @Test
    public void setWeight() {
        int newWeight = 5000;
        item.setWeight(newWeight);
        Assert.assertEquals(item.getWeight(), newWeight);
    }

    @Test
    public void getPrice() {
        Assert.assertEquals(item.getPrice(), PRICE);
    }

    @Test
    public void setPrice() {
        String newPrice = "2500 Р";
        item.setPrice(newPrice);
        Assert.assertEquals(item.getPrice(), newPrice);
    }

    @Test
    public void getSalePrice() {
        Assert.assertEquals(item.getSalePrice(), SALE_PRICE);
    }

    @Test
    public void setSalePrice() {
        String newSalePrice = "750 Р";
        item.setSalePrice(newSalePrice);
        Assert.assertEquals(item.getSalePrice(), newSalePrice);
    }

    @Test
    public void getSaleBeginDate() {
        Assert.assertEquals(item.getSaleBeginDate(), SALE_BEGIN_DATE);
    }

    @Test
    public void setSaleBeginDate() {
        String newSaleBeginDate = "11.11.2021";
        item.setSaleBeginDate(newSaleBeginDate);
        Assert.assertEquals(item.getSaleBeginDate(), newSaleBeginDate);
    }

    @Test
    public void getSaleEndDate() {
        Assert.assertEquals(item.getSaleEndDate(), SALE_END_DATE);
    }

    @Test
    public void setSaleEndDate() {
        String newSaleEndDate = "11.11.2022";
        item.setSaleEndDate(newSaleEndDate);
        Assert.assertEquals(item.getSaleEndDate(), newSaleEndDate);
    }

    @Test
    public void getShopName() {
        Assert.assertEquals(item.getShopName(), SHOP_NAME);
    }

    @Test
    public void setShopName() {
        String newShopName = "Перекрёсток";
        item.setShopName(newShopName);
        Assert.assertEquals(item.getShopName(), newShopName);
    }

    @Test
    public void getImageURL() {
        Assert.assertEquals(item.getImageURL(), IMAGE_URL);
    }

    @Test
    public void setImageURL() {
        String newImageUrl = "https://www.culture.ru/storage/images/f89f98f6bf3b5b7577b24ac11f8bcb85/" +
                "4b4eac459d4ef69aa9774cdbec0f2de1.jpeg";
        item.setImageURL(newImageUrl);
        Assert.assertEquals(item.getImageURL(), newImageUrl);
    }

    @Test
    public void testToStringWithAllFields() {
        setItem();
        Assert.assertEquals(item.toString(), itemString);
    }

    @Test
    public void testToStringWithNullableFields() {
        setItem();
        item.setWeight(0);
        itemString = itemString.replace("Вес: " + WEIGHT + "\n", "");
        Assert.assertEquals(item.toString(), itemString);

        item.setPrice(null);
        itemString = itemString.replace("Цена без скидки: " + PRICE + "\n", "");
        Assert.assertEquals(item.toString(), itemString);

        item.setSaleBeginDate(null);
        itemString = itemString.replace("Начало акции: " + SALE_BEGIN_DATE + "\n", "");
        Assert.assertEquals(item.toString(), itemString);

        item.setSaleEndDate(null);
        itemString = itemString.replace("Окончание акции: " + SALE_END_DATE + "\n", "");
        Assert.assertEquals(item.toString(), itemString);
    }

    @Test
    public void testCompareTo() {
        setItem();
        Item itemToCompare = new Item();
        itemToCompare.setSalePrice("500 Р");
        Assert.assertEquals(-1, item.compareTo(itemToCompare));

        itemToCompare.setSalePrice("50 Р");
        Assert.assertEquals(1, item.compareTo(itemToCompare));

        itemToCompare.setSalePrice("150 Р");
        Assert.assertEquals(0, item.compareTo(itemToCompare));
    }
}
