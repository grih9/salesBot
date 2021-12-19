package database;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class JDBCConnector {
	private Connection connection;

	public void createTable() {
		try {
			Statement stmt = this.connection.createStatement();

			String CreateSql = "DROP SCHEMA public CASCADE;" +
					"CREATE SCHEMA public;" +
					"Create Table cities(id serial primary key, name varchar, region varchar); " +
					"Create Table shops(id serial primary key, name varchar, website varchar);" +
					"Create Table users(id serial primary key, name varchar UNIQUE, cityId int REFERENCES cities(id)); " +
					"Create Table cities_shops(id serial primary key, cityId int REFERENCES cities(id), shopId " +
					"int REFERENCES shops(id)); " +
					"Create Table users_shops(id serial primary key, userId int REFERENCES users(id), shopId " +
					"int REFERENCES shops(id)); " +
					"Create Table categories(id serial primary key, name varchar); ";

			stmt.executeUpdate(CreateSql);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public JDBCConnector() {
		try {
			Class.forName("org.postgresql.Driver");

			String url = "jdbc:postgresql://ec2-63-32-12-208.eu-west-1.compute.amazonaws.com:5432/d7ova8n0gd539v";
			String user = "ytfrrtlrtiiyoc";
			String password = "29237129f83a4c97eaa600ffccbc164a91ebbe370468b3984d5d436ba8481c04";

			this.connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//Добавление пользователя без города
	public Boolean addUser(String username) {
		try {
			/*String sql = "select id from users where name = ?";
			PreparedStatement ps = this.connection.prepareStatement(sql);

			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return false; // Пользователь c таким именем уже был добавлен
			}*/

			String sqlUser = "insert into users(name) values(?);";

			PreparedStatement psUser = this.connection.prepareStatement(sqlUser);

			psUser.setString(1, username);

			psUser.executeUpdate();
			psUser.close();

		} catch (SQLException e) {
			if (Objects.equals(e.getSQLState(), "23505")) {
				return false; // Пользователь c таким именем уже был добавлен
			} else {
				e.printStackTrace();
			}
		}

		return true; // Пользователь успешно добавлен
	}

	//Добавление города для пользователя по имени
	public Boolean addCity(String username, String cityName) {

		try {
			String sql = "select id from cities where name = ?;";
			PreparedStatement ps = this.connection.prepareStatement(sql);

			ps.setString(1, cityName);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return false; // Город не найден;
			}

			int cityId = rs.getInt("id");

			ps.close();

			String sqlUser = "UPDATE users SET cityId = ? WHERE name = ?;";

			PreparedStatement psUser = this.connection.prepareStatement(sqlUser);

			psUser.setInt(1, cityId);
			psUser.setString(2, username);

			psUser.executeUpdate();
			psUser.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true; // город успешно добавлен;
	}

	//Добавление списка городов
	public Boolean addCities() {
		String sqlInsert = "insert into cities(name, region) values(?, ?);";
		String sqlSelect = "select id from cities where name = ? AND region = ?;";
		String fileName = "city_1.csv";

		try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
			String[] lineInArray;
			String name;
			String region;

			PreparedStatement ps = this.connection.prepareStatement(sqlInsert);
			PreparedStatement psSelect = this.connection.prepareStatement(sqlSelect);
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

				psSelect.setString(1, name);
				psSelect.setString(2, region);
				ResultSet rs = psSelect.executeQuery();

				if (!rs.next()) {
					ps.setString(1, name);
					ps.setString(2, region);
					ps.executeUpdate();
				}
			}

			ps.close();
			psSelect.close();
		} catch (IOException | CsvValidationException | SQLException e) {
			e.printStackTrace();
		}

		return true; // Список городов успешно изменен;
	}

	//Добавление списка категорий
	public Boolean addCategories() {
		String sqlInsert = "insert into categories(name) values(?)";
		String sqlTruncate = "TRUNCATE TABLE categories";
		String fileName = "categories.csv";

		try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
			Statement stmt = this.connection.createStatement();

			stmt.executeUpdate(sqlTruncate);
			stmt.close();

			String[] lineInArray;
			PreparedStatement ps = this.connection.prepareStatement(sqlInsert);
			reader.readNext();

			while ((lineInArray = reader.readNext()) != null) {
				ps.setString(1, lineInArray[0]);
				ps.executeUpdate();
			}

			ps.close();
		} catch (IOException | CsvValidationException | SQLException e) {
			e.printStackTrace();
		}

		return true; // Список городов успешно изменен;
	}

	public Boolean addShops() {
		String sqlInsert = "insert into shops(name, website) values(?, ?)";
		String sqlTruncate = "TRUNCATE TABLE shops CASCADE";
		String fileName = "shops.csv";

		try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
			Statement stmt = this.connection.createStatement();

			stmt.executeUpdate(sqlTruncate);
			stmt.close();

			String[] lineInArray;
			PreparedStatement ps = this.connection.prepareStatement(sqlInsert);
			reader.readNext();

			while ((lineInArray = reader.readNext()) != null) {
				ps.setString(1, lineInArray[0]);
				ps.setString(2, lineInArray[1]);
				ps.executeUpdate();
			}

			ps.close();
		} catch (IOException | CsvValidationException | SQLException e) {
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
			Statement stmt = this.connection.createStatement();

			stmt.executeUpdate(sqlTruncate);
			stmt.close();

			Statement stmtCity = this.connection.createStatement();
			ResultSet rs = stmtCity.executeQuery(sqlCity);
			PreparedStatement ps = this.connection.prepareStatement(sqlInsert);
			while (rs.next()) {
				Statement shops = this.connection.createStatement();
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
				"(select id from shops where name = ? and website =?));";
		String sqlTruncate = "DELETE FROM users_shops WHERE userId = (select id from users where name = ?)";

		try {
			PreparedStatement tr = this.connection.prepareStatement(sqlTruncate);
			tr.setString(1, username);
			tr.executeUpdate();
			tr.close();

			PreparedStatement ps = this.connection.prepareStatement(sqlInsert);

			for (Shop shop : shops) {
				ps.setString(1, username);
				ps.setString(2, shop.getName());
				ps.setString(3, shop.getWebsite());
				ps.executeUpdate();
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true; // Список выбранных магазинов изменен
	}

	public List<String> getCategories() {
		List<String> result = new LinkedList<>();

		try {
			String sql = "select name from categories";

			Statement stmt = this.connection.createStatement();
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

			PreparedStatement ps = this.connection.prepareStatement(sql);
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

	public List<Shop> getShops(String username) {
		List<Shop> result = new LinkedList<>();

		try {
			/*String sql = "select shops.name, website from shops join cities_shops on shops.id = cities_shops.shopId join" +
					"cities on cities_shops.cityId = cities.id join users on users.cityId = cities.id where users.name like ?";*/
			String sql = "select shops.name, website from shops;";
			/*PreparedStatement ps = this.connection.prepareStatement(sql);
			//ps.setString(1, username);
			ResultSet rs = ps.executeQuery(sql);*/
			Statement stmt = this.connection.createStatement();
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

			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			result = new City(rs.getString("name"), rs.getString("region"));

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}
