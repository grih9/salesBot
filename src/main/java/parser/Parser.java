package parser;

import java.util.ArrayList;
import java.util.List;

import bot.Item;
import database.City;
import database.Shop;

public class Parser {
    private City city;
    private List<Shop> shops;

    public Parser(City city, List<Shop> shops) {
        this.city = city;
        this.shops = shops;
    }

    public List<Item> findItemByName(String itemName) {
        List<Item> items = new ArrayList<>();

        //

        return items;
    }
}
