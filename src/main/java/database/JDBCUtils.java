package database;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtils {
    public static List<City> getCitiesFromCSV() {
        List<City> cities = new ArrayList<>();
        String fileName = "city_1.csv";

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            String[] lineInArray;
            String name;
            String region;

            reader.readNext();
            for (int i = 0; i < 10; i++) {
                lineInArray = reader.readNext();
                name = lineInArray[0];

                int ind = lineInArray[0].indexOf("г ");
                while (ind > 0 && name.charAt(ind) != ' ') {
                    name = name.substring(ind + 1);
                    ind = name.indexOf("г ");
                }
                name = name.substring(ind + 2);

                int index = lineInArray[5].indexOf(" - ");
                if (index == -1) {
                    region = lineInArray[5];
                } else {
                    region = lineInArray[5].substring(0, index);
                }
                cities.add(new City(name, region));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public static List<String> getCategoriesFromCSV() {
        List<String> categories = new ArrayList<>();
        String fileName = "categories.csv";

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            String[] lineInArray;
            reader.readNext();

            while ((lineInArray = reader.readNext()) != null) {
                categories.add(lineInArray[0]);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static List<Shop> getShopsFromCSV() {
        List<Shop> shops = new ArrayList<>();
        String fileName = "shops.csv";

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            String[] lineInArray;
            reader.readNext();

            while ((lineInArray = reader.readNext()) != null) {
                shops.add(new Shop(lineInArray[0], lineInArray[1]));
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return shops;
    }
}
