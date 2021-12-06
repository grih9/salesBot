package parser;

import java.util.ArrayList;
import java.util.List;

import bot.Item;
import database.City;
import database.Shop;

public class Parser {
    public static List<Item> findItemsByName(String itemName, City city, List<Shop> shops) {
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
}
