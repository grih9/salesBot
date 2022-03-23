package database;

import bot.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class JDBCConnector {
	private Connection connection = null;

	public JDBCConnector(Boolean isTestConn) {
		String url = "jdbc:postgresql://ec2-63-32-12-208.eu-west-1.compute.amazonaws.com:5432/d7ova8n0gd539v";
		String user = "ytfrrtlrtiiyoc";
		String password = "29237129f83a4c97eaa600ffccbc164a91ebbe370468b3984d5d436ba8481c04";

		if (isTestConn) {
			url = "jdbc:postgresql://ec2-44-193-188-118.compute-1.amazonaws.com:5432/d7ba6v4jgke23f";
			user = "ntallivuhxtyjm";
			password = "94e3bf3270467ff2cc81e3dad31bcd8f592794fb78d08793b429218ab160b0a5";
		}

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void createTables() {
		try {
			Statement stmt = connection.createStatement();

			String CreateSql = "DROP SCHEMA public CASCADE; CREATE SCHEMA public;" +
					"Create Table cities(id serial primary key, name varchar, region varchar); " +
					"Create Table shops(id serial primary key, name varchar, website varchar);" +
					"Create Table users(id serial primary key, name varchar UNIQUE, cityId int REFERENCES cities(id)); " +
					"Create Table cities_shops(id serial primary key, cityId int REFERENCES cities(id), shopId " +
					"int REFERENCES shops(id)); " +
					"Create Table users_shops(id serial primary key, userId int REFERENCES users(id), shopId " +
					"int REFERENCES shops(id)); " +
					"Create Table categories(id serial primary key, name varchar);" +
					"Create Table items(id serial primary key, name varchar, imageurl varchar, price varchar, salePrice varchar," +
					" saleBeginDate varchar, saleEndDate varchar, cityId int REFERENCES shops(id), categoryId int REFERENCES categories(id)) ";

			stmt.executeUpdate(CreateSql);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void truncateTables() {
		try {
			Statement stmt = connection.createStatement();

			String CreateSql = "TRUNCATE TABLE cities CASCADE;" +
					"TRUNCATE TABLE shops CASCADE;" +
					"TRUNCATE TABLE users CASCADE;" +
					"TRUNCATE TABLE cities_shops CASCADE;" +
					"TRUNCATE TABLE users_shops CASCADE;" +
					"TRUNCATE TABLE categories CASCADE;" +
					"TRUNCATE TABLE items CASCADE;";
			stmt.executeUpdate(CreateSql);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Добавление пользователя без города
	public Boolean addUser(String username) {
		try {
			String sqlUser = "insert into users(name) values(?);";

			PreparedStatement psUser = connection.prepareStatement(sqlUser);

			psUser.setString(1, username);

			psUser.executeUpdate();
			psUser.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false; // Пользователь c таким именем уже был добавлен
		}

		return true; // Пользователь успешно добавлен
	}

	public Boolean deleteUser(String username) {
		try {
			String sqlUser = "delete from users where name = ?";
			String deleteUserFromUserShop = "delete from users_shops where userid = (select id from users where name = ?)";

			PreparedStatement psDeleteUserFromUserShop = connection.prepareStatement(deleteUserFromUserShop);
			psDeleteUserFromUserShop.setString(1, username);
			psDeleteUserFromUserShop.executeUpdate();
			psDeleteUserFromUserShop.close();

			PreparedStatement psUser = connection.prepareStatement(sqlUser);
			psUser.setString(1, username);
			psUser.executeUpdate();
			psUser.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	//Добавление города для пользователя по имени
	public Boolean addCity(String username, String cityName) {
		try {
			String sql = "select id from cities where name = ?;";
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, cityName);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return false; // Город не найден;
			}

			int cityId = rs.getInt("id");

			ps.close();

			String sqlUser = "UPDATE users SET cityId = ? WHERE name = ?;";

			PreparedStatement psUser = connection.prepareStatement(sqlUser);

			psUser.setInt(1, cityId);
			psUser.setString(2, username);

			psUser.executeUpdate();
			psUser.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true; // город успешно добавлен;
	}

	//Добавление списка городов
	public Boolean addCities() {
		String sqlInsert = "insert into cities(name, region) values(?, ?);";
		String sqlSelect = "select id from cities where name = ? AND region = ?;";

		try {
			PreparedStatement ps = connection.prepareStatement(sqlInsert);
			PreparedStatement psSelect = connection.prepareStatement(sqlSelect);

			List<City> cities = JDBCUtils.getCitiesFromCSV();

			for (int i = 0; i < 10; i++) {
				psSelect.setString(1, cities.get(i).getName());
				psSelect.setString(2, cities.get(i).getRegion());
				ResultSet rs = psSelect.executeQuery();

				if (!rs.next()) {
					ps.setString(1, cities.get(i).getName());
					ps.setString(2, cities.get(i).getRegion());
					ps.executeUpdate();
				}
			}

			ps.close();
			psSelect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true; // Список городов успешно изменен;
	}

	//Добавление списка категорий
	public Boolean addCategories() {
		String sqlInsert = "insert into categories(name) values(?)";
		String sqlTruncate = "TRUNCATE TABLE categories CASCADE";

		try {
			Statement stmt = connection.createStatement();

			stmt.executeUpdate(sqlTruncate);
			stmt.close();
			PreparedStatement ps = connection.prepareStatement(sqlInsert);

			List<String> categories = JDBCUtils.getCategoriesFromCSV();

			for (String category : categories) {
				ps.setString(1, category);
				ps.executeUpdate();
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true; // Список городов успешно изменен;
	}

	public Boolean addShops() {
		String sqlInsert = "insert into shops(name, website) values(?, ?)";
		String sqlTruncate = "TRUNCATE TABLE shops CASCADE";

		try {
			Statement stmt = connection.createStatement();

			stmt.executeUpdate(sqlTruncate);
			stmt.close();
			PreparedStatement ps = connection.prepareStatement(sqlInsert);
			List<Shop> shops = JDBCUtils.getShopsFromCSV();

			for (Shop shop : shops) {
				ps.setString(1, shop.getName());
				ps.setString(2, shop.getWebsite());
				ps.executeUpdate();
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true; // Список магазинов успешно обновлен
	}

	public Boolean addShopsForSities() {
		String sqlInsert = "insert into cities_shops(cityId, shopId) values(?, ?);";
		String sqlTruncate = "TRUNCATE TABLE cities_shops;";
		String sqlShop = "select id from shops;";
		String sqlCity = "select id from cities limit 10;";

		try {
			Statement stmt = connection.createStatement();

			stmt.executeUpdate(sqlTruncate);
			stmt.close();

			Statement stmtCity = connection.createStatement();
			ResultSet rs = stmtCity.executeQuery(sqlCity);
			PreparedStatement ps = connection.prepareStatement(sqlInsert);
			while (rs.next()) {
				Statement shops = connection.createStatement();
				ResultSet shopsRs = shops.executeQuery(sqlShop);
				while (shopsRs.next()) {
					ps.setInt(1, rs.getInt("id"));
					ps.setInt(2, shopsRs.getInt("id"));
					ps.executeUpdate();
				}
				shops.close();
			}

			ps.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true; // Список магазинов успешно обновлен
	}

	public Boolean setSelectedShops(String username, List<Shop> shops) {
		String sqlInsert = "insert into users_shops(userId, shopId) values((select id from users where name = ?), " +
				"(select id from shops where name = ? and website = ?));";
		String sqlTruncate = "DELETE FROM users_shops WHERE userId = (select id from users where name = ?)";

		try {
			PreparedStatement tr = connection.prepareStatement(sqlTruncate);
			tr.setString(1, username);
			tr.executeUpdate();
			tr.close();

			PreparedStatement ps = connection.prepareStatement(sqlInsert);

			for (Shop shop : shops) {
				ps.setString(1, username);
				ps.setString(2, shop.getName());
				ps.setString(3, shop.getWebsite());
				ps.executeUpdate();
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			return false;
		}

		return true; // Список выбранных магазинов изменен
	}

	public Boolean addItems(String categoty, Shop shop, List<Item> items) {
		String sqlInsert = "insert into items(name, imageurl, price, salePrice, saleBeginDate, saleEndDate, cityId, categoryId) " +
				"values(?, ?, ?, ?, ?, ?, (select id from shops where name = ? and website = ?), (select id from categories where name = ?));";

		try {
			PreparedStatement ps = connection.prepareStatement(sqlInsert);

			for (Item item : items) {
				ps.setString(1, item.getName());
				ps.setString(2, item.getImageURL());
				ps.setString(3, item.getPrice());
				ps.setString(4, item.getSalePrice());
				ps.setString(5, item.getSaleBeginDate());
				ps.setString(6, item.getSaleEndDate());
				ps.setString(7, shop.getName());
				ps.setString(8, shop.getWebsite());
				ps.setString(9, categoty);
				ps.executeUpdate();
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			return false;
		}

		return true;
	}

	public List<Item> getItemsByCategory(String categoty, Shop shop) {
		List<Item> items = new ArrayList<>();
		try {
			String sql = "select items.name, imageURL, price, salePrice, saleBeginDate, saleEndDate, shops.name from items join categories " +
					"on items.categoryId = categories.id join shops " +
					"on items.cityId = shops.id where categories.name = ? AND shops.name = ? AND shops.website = ? ";

			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, categoty);
			ps.setString(2, shop.getName());
			ps.setString(3, shop.getWebsite());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Item item = new Item();
				String name = rs.getString(1);
				name = name.replaceAll("Товар Представлен Не Во Всех Магазинах", "");

				if (name.contains("Товар представлен")) {
					name = name.substring(0, name.indexOf("Товар представлен"));
				}
				while (name.endsWith(".") || name.endsWith(" ")) {
					name = name.substring(0, name.length() - 2);
				}
				item.setName(name);
				item.setImageURL(rs.getString(2));
				item.setPrice(rs.getString(3));
				item.setSalePrice(rs.getString(4));
				item.setSaleBeginDate(rs.getString(5));
				item.setSaleEndDate(rs.getString(6));
				item.setShopName(rs.getString(7));
				items.add(item);
			}

			ps.close();
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}

		return items;
	}

	public List<Item> getItemsByName(String itemName, Shop shop) {
		List<Item> items = new ArrayList<>();
		try {
			String sql = "select items.name, imageURL, price, salePrice, saleBeginDate, saleEndDate, shops.name from items join shops " +
					"on items.cityId = shops.id where shops.name = ? AND shops.website = ? AND LOWER(items.name) LIKE '%' || LOWER(?)  || '%';";

			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, shop.getName());
			ps.setString(2, shop.getWebsite());
			ps.setString(3, "%" + itemName.toLowerCase() + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Item item = new Item();
				String name = rs.getString(1);
				name = name.replaceAll("Товар Представлен Не Во Всех Магазинах", "");

				if (name.contains("Товар представлен")) {
					name = name.substring(0, name.indexOf("Товар представлен"));
				}

				if (name.endsWith(".")) {
					name = name.substring(0, name.length() - 1);
				}
				item.setName(name);
				item.setImageURL(rs.getString(2));
				item.setPrice(rs.getString(3));
				item.setSalePrice(rs.getString(4));
				item.setSaleBeginDate(rs.getString(5));
				item.setSaleEndDate(rs.getString(6));
				item.setShopName(rs.getString(7));
				items.add(item);
			}

			ps.close();
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}

		return items;
	}

	public List<City> getCities() {
		List<City> result = new LinkedList<>();

		try {
			String sql = "select name, region from cities";

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				result.add(new City(rs.getString("name"), rs.getString("region")));
			}

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<String> getCategories() {
		List<String> result = new LinkedList<>();

		try {
			String sql = "select name from categories";

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				result.add(rs.getString("name"));
			}

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}


	public List<Shop> getSelectedShops(String username) {
		List<Shop> result = new LinkedList<>();
		int[] a = {12};
		try {
			String sql = "select shops.name, website from shops join users_shops on shops.id = users_shops.shopId join" +
					" users on users.id = users_shops.userId where users.name = ?";

			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				result.add(new Shop(rs.getString("name"), rs.getString("website")));
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<Shop> getShops() {
		List<Shop> result = new LinkedList<>();

		try {
			/*String sql = "select shops.name, website from shops join cities_shops on shops.id = cities_shops.shopId join" +
					"cities on cities_shops.cityId = cities.id join users on users.cityId = cities.id where users.name like ?";*/
			String sql = "select shops.name, website from shops;";
			/*PreparedStatement ps = connection.prepareStatement(sql);
			//ps.setString(1, username);
			ResultSet rs = ps.executeQuery(sql);*/
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				result.add(new Shop(rs.getString("name"), rs.getString("website")));
			}

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public City getUserCity(String username) {
		City result = null;
		try {
			String sql = "select cities.name, region from cities join users " +
					"on users.cityId = cities.id where users.name = ?";

			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = new City(rs.getString(1), rs.getString(2));
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public String getUserId(String username) {
		String result = null;
		try {
			String sql = "select id from users where users.name = ?";

			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}
