package parser;

import java.util.ArrayList;
import java.util.List;

import bot.Item;
import database.City;
import database.Shop;

public class Parser {
    private static City city;
    private static List<Shop> shops;
    private static List<String> cathegories;

    public static List<Item> findItemsByName(String itemName) {
        List<Item> items = new ArrayList<>();
        for (Shop shop : shops) {

            switch (shop.getName()) {
                case ("Пятёрочка"):
                    //
                    break;
                case ("Перекрёсток"):
                    //
                    break;
                case ("Магнит"):
                    //
                    break;
                case ("Ашан"):
                    //
                    break;
                case ("Дикси"):
                    //
                    break;
                case ("Spar"):
                    //
                    break;
                case ("О'КЕЙ"):
                    //
                    break;
                case ("Лента"):
                    //
                    break;
                case ("Карусель"):
                    //
                    break;
                case ("Prisma"):
                    //
                    break;
            }
        }
        return items;
    }

    public static void setCity(City city) {
        Parser.city = city;
    }

    public static void setShops(List<Shop> shops) {
        Parser.shops = shops;
    }

    public static void setCathegories(List<String> cathegories) {
        Parser.cathegories = cathegories;
    }
}
