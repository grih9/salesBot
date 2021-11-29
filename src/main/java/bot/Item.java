package bot;

import java.util.Date;

public class Item {
    private String name;
    private int weight;
    private float price;
    private float salePrice;
    private Date saleBeginDate;
    private Date saleEndDate;
    private String shopName;
    private String imageURL;

    public Item() {
    }

    public Item(String name, int weight, float price, float salePrice, Date saleBeginDate, Date saleEndDate,
                String shopName, String imageURL) {
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.salePrice = salePrice;
        this.saleBeginDate = saleBeginDate;
        this.saleEndDate = saleEndDate;
        this.shopName = shopName;
        this.imageURL = imageURL;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public Date getSaleBeginDate() {
        return saleBeginDate;
    }

    public void setSaleBeginDate(Date saleBeginDate) {
        this.saleBeginDate = saleBeginDate;
    }

    public Date getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(Date saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return  "Название " + name + '\'' +
                "Вес " + weight + '\'' +
                "Цена без скидки " + price + '\'' +
                "Цена со скидкой " + salePrice + '\'' +
                "Начало акции " + saleBeginDate + '\'' +
                "Окончание акции " + saleEndDate + '\'' +
                "Сеть " + shopName;
    }
}
