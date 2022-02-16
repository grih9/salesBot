package bot;

public class Item implements Comparable<Item>  {
    private String name;
    private int weight;
    private String price;
    private String salePrice;
    private String saleBeginDate;
    private String saleEndDate;
    private String shopName;
    private String imageURL;

    public Item() {
    }

    public Item(String name, int weight, String price, String salePrice, String saleBeginDate, String saleEndDate,
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSaleBeginDate() {
        return saleBeginDate;
    }

    public void setSaleBeginDate(String saleBeginDate) {
        this.saleBeginDate = saleBeginDate;
    }

    public String getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(String saleEndDate) {
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
        return  "Название: " + name + "\n" +
                (weight != 0 ? ("Вес: " + weight + "\n") : "") +
                (price != null ? ("Цена без скидки: " + price + "\n") : "") +
                "Цена со скидкой: " + salePrice + "\n" +
                (saleBeginDate != null ? ("Начало акции: " + saleBeginDate + "\n") : "") +
                (saleEndDate != null ? ("Окончание акции: " + saleEndDate + "\n") : "") +
                "Сеть: " + shopName + "\n" +
                imageURL;
    }

    @Override
    public int compareTo(Item o) {
        String firstDouble = salePrice.split(" ")[0].replaceAll(" ", "").replaceAll(",", ".");
        String secondDouble =o.getSalePrice().split(" ")[0].replaceAll(" ", "").replaceAll(",", ".");
        double firstPrice = Double.parseDouble(firstDouble);
        double secondPrice = Double.parseDouble(secondDouble);

        if(firstPrice > secondPrice)
            return 1;
        else if(firstPrice < secondPrice)
            return -1;
        return 0;
    }
}
