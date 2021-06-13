package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

public class User {
	private int userID;
	private String username;
	private String firstname;
	private String lastname;
	
	public User(int userID, String username, String firstname, String lastname) {
		this.userID = userID;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	@JsonGetter
	public int getUserID() {
		return userID;
	}

	@JsonGetter
	public String getUsername() {
		return username;
	}

	@JsonGetter
	public String getFirstname() {
		return firstname;
	}

	@JsonGetter
	public String getLastname() {
		return lastname;
	}
	
	public static boolean createTable() {
		String query = "CREATE TABLE IF NOT EXISTS user("
					 + "userID int AUTO_INCREMENT NOT NULL,"
					 + "username varchar(50) NOT NULL,"
					 + "passwort varchar(256) NOT NULL,"
					 + "vorname varchar(256) NOT NULL,"
					 + "nachname varchar(256) NOT NULL,"
					 + "rolle varchar(10) NOT NULL,"
					 + "klassenID int,"
					 + "PRIMARY KEY(userID),"
					 + "UNIQUE(username),"
					 + "FOREIGN KEY(klassenID) REFERENCES klasse(klassenID))";
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			createAdmin();
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	
	public static void insertData() {
		List<User> current = findAllUsers();
		if (!current.isEmpty()) {
			return;
		}
		String teacher1 = "INSERT INTO user(username, passwort, vorname, nachname, rolle) "
						+ "VALUES('storm', 'PASSWORT', 'Zoya', 'Nazyalenski', 'Lehrende')";
		String teacher2 = "INSERT INTO user(username, passwort, vorname, nachname, rolle) "
						+ "VALUES('hunter', 'PASSWORT', 'Jarl', 'Brum', 'Lehrende')";
		String teacher3 = "INSERT INTO user(username, passwort, vorname, nachname, rolle) "
						+ "VALUES('tailor', 'PASSWORT', 'Genya', 'Safin', 'Lehrende')";
		
		String student1 = "INSERT INTO user(username, passwort, vorname, nachname, rolle, klassenID) "
						+ "VALUES ('demjin', 'PASSWORT', 'Kaz', 'Brekker', 'Lernende', 1)";
		String student2 = "INSERT INTO user(username, passwort, vorname, nachname, rolle, klassenID) "
						+ "VALUES('phantom', 'PASSWORT', 'Inej', 'Ghaza', 'Lernende', 1)";
		String student3 = "INSERT INTO user(username, passwort, vorname, nachname, rolle, klassenID) "
						+ "VALUES('sharpshooter', 'PASSWORT', 'Jesper Llewellyn', 'Fahey', 'Lernende', 1)";
		String student4 = "INSERT INTO user(username, passwort, vorname, nachname, rolle, klassenID) "
						+ "VALUES('merchling', 'PASSWORT', 'Wylan', 'Van Eck', 'Lernende', 1)";
		
		String student5 = "INSERT INTO user(username, passwort, vorname, nachname, rolle, klassenID) "
						+ "VALUES('redbird', 'PASSWORT', 'Nina', 'Zenik', 'Lernende', 2)";
		String student6 = "INSERT INTO user(username, passwort, vorname, nachname, rolle, klassenID) "
						+ "VALUES('trassel', 'PASSWORT', 'Matthias Benedik', 'Helvar', 'Lernende', 2)";
		String student7 = "INSERT INTO user(username, passwort, vorname, nachname, rolle, klassenID) "
						+ "VALUES('sunny', 'PASSWORT', 'Alina', 'Starkov', 'Lernende', 2)";
		String student8 = "INSERT INTO user(username, passwort, vorname, nachname, rolle, klassenID) "
						+ "VALUES('brief', 'PASSWORT', 'Malyen', 'Oretsev', 'Lernende', 2)";
		
		String student9 = "INSERT INTO user(username, passwort, vorname, nachname, rolle) "
						+ "VALUES('phoenix', 'PASSWORT', 'Kuwei', 'Yul-Bo', 'Lernende')";
		
		try(Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			Statement stmt = con.createStatement();
			stmt.executeUpdate(teacher1);
			stmt.executeUpdate(teacher2);
			stmt.executeUpdate(teacher3);
			stmt.executeUpdate(student1);
			stmt.executeUpdate(student2);
			stmt.executeUpdate(student3);
			stmt.executeUpdate(student4);
			stmt.executeUpdate(student5);
			stmt.executeUpdate(student6);
			stmt.executeUpdate(student7);
			stmt.executeUpdate(student8);
			stmt.executeUpdate(student9);
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void createAdmin() {
		List<User> admins = findAllAdmins();
		if (admins.isEmpty()) {
			try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
				String query = "INSERT INTO dw.user (username, passwort, vorname, nachname, rolle)"
							 + "VALUES ('admin', 'PASSWORT', 'leer', 'leer', 'Admin')";
				Statement stmt = con.createStatement();
				stmt.executeUpdate(query);				
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	public static List<User> findAllUsers(){
		List<User> users = new LinkedList<>();
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username, vorname, nachname FROM user";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			
		} catch (SQLException e){
			System.err.println(e.getMessage());
		}
		return users;
	}
	
	public static User findById(int id) {
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username, vorname, nachname FROM user WHERE userID=" + id;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
			} else {
				return null;
			}
		} catch (SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	public static List<User> findAllAdmins(){
		List<User> admins = new LinkedList<>();
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username FROM user WHERE rolle='Admin'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				admins.add(new User(rs.getInt(1), rs.getString(2), null, null));
			}
			
		} catch (SQLException e){
			System.err.println(e.getMessage());
		}
		return admins;
	}

}
