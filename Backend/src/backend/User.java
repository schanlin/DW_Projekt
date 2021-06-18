package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonGetter;

public class User {
	private int userID;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String rolle;

	public User(int userID, String username, String firstname, String lastname, String rolle) {
		this.userID = userID;
		this.username = username;
		this.password = null;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rolle = rolle;
	}
	
	public User(int userID, String username, String password, String firstname, String lastname, String rolle) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rolle = rolle;
	}

	public User(int userID, String username, String firstname, String lastname) {
		this.userID = userID;
		this.username = username;
		this.password = null;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rolle = null;
	}
	
	public User(String username, String password, String firstname, String lastname, String rolle) {
		this.userID = 0;
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rolle = rolle;
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

	@JsonGetter
	public String getRolle() {
		return rolle;
	}
	
	public String getPassword() {
		return password;
	}

	public static void createTable() throws SQLException {
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
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
		createAdmin();
	}

	public static void insertData() throws SQLException {
//		List<User> current = findAllUsers();
//		
//		if (!current.isEmpty()) {
//			return;
//		}
		
		User[] teachers = new User[3];
		teachers[0] = new User("storm", "PASSWORT", "Zoya", "Nazyalenski", "Lehrende");
		teachers[1] = new User("hunter", "PASSWORT", "Jarl", "Brum", "Lehrende");
		teachers[2] = new User("tailor", "PASSWORT", "Genya", "Safin", "Lehrende");
		
		for (User teacher: teachers) {
			teacher.insert();
		}
		
		Student[] students = new Student[9];
		students[0] = new Student("demjin", "PASSWORT", "Kaz", "Brekker", "Lernende", "2016a");
		students[1] = new Student("phantom", "PASSWORT", "Inej", "Ghaza", "Lernende", "2016a");
		students[2] = new Student("sharpshooter", "PASSWORT", "Jesper Llewellyn", "Fahey", "Lernende", "2016a");
		students[3] = new Student("merchling", "PASSWORT", "Wylan", "Van Eck", "Lernende", "2016a");
		students[4] = new Student("redbird", "PASSWORT", "Nina", "Zenik", "Lernende", "2016b");
		students[5] = new Student("trassel", "PASSWORT", "Matthias Benedik", "Helvar", "Lernende", "2016b");
		students[6] = new Student("sunny", "PASSWORT", "Alina", "Starkov", "Lernende", "2016b");
		students[7] = new Student("brief", "PASSWORT", "Malyen", "Oretsev", "Lernende", "2016b");
		students[8] = new Student("poenix", "PASSWORT", "Kuwei", "Yul-Bo", "Lernende", null);
		
		for (Student student: students) {
			student.insert();
		}
	}
	
	public void insert() throws SQLException {
		String query = "INSERT INTO user(username, passwort, vorname, nachname, rolle)"
					 + "VALUES (?, ?, ?, ?, ?)";
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, this.username);
			stmt.setString(2, this.password);
			stmt.setString(3, this.firstname);
			stmt.setString(4, this.lastname);
			stmt.setString(5, this.rolle);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public static void createAdmin() throws SQLException {
		List<User> admins = findAllAdmins();
		if (admins.isEmpty()) {
			try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
				String query = "INSERT INTO user (username, passwort, vorname, nachname, rolle)"
		 				+ "VALUES ('admin', 'PASSWORT', 'leer', 'leer', 'Admin')";
				Statement stmt = con.createStatement();
				stmt.executeUpdate(query);
			} catch (SQLException e) {
				throw e;
			}			
		}
	}

	public static List<User> findAllUsers() throws SQLException {
		List<User> users = new LinkedList<>();
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username, vorname, nachname, rolle FROM user";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
	
		while (rs.next()) {
			users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
		}
		return users;
		} catch (SQLException e) {
			throw e;
		}		
	}

	public static User findById(int id) throws SQLException {
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username, vorname, nachname, rolle FROM user WHERE userID=" + id;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User konnte nicht gefunden werden.");
			}
		} catch (SQLException e) {
			throw e;
		}
		
	}
	
	public static User findByName(String name) throws SQLException {
		String[] fullname = name.split(" ");
		
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)) {
			String query;
			if (fullname.length == 2) {
				query = "SELECT userID, username, vorname, nachname, rolle FROM user WHERE vorname=" + fullname[0] + " AND nachname=" + fullname[1];
			} else {
				query = "SELECT userID FROM user WHERE vorname LIKE " + (fullname[0] + "%") 
							 + "AND nachname LIKE " + ("%" + fullname[fullname.length-1]);
			}
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} 
		} catch (SQLException e) {
			throw e;
		}
	}

	public static List<User> findAllAdmins() throws SQLException {
		List<User> admins = new LinkedList<>();
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username FROM user WHERE rolle='Admin'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				admins.add(new User(rs.getInt(1), rs.getString(2), null, null, "Admin"));
			}
			return admins;
		} catch (SQLException e) {
			throw e;
		}		
	}

	public static void delete(int id) throws SQLException {
		User toDelete = User.findById(id);

		if (toDelete.getRolle().equals("Lernende")) {
			Student.delete(id);
			return;
		}

		if (toDelete.getRolle().equals("Lehrende")) {
			if (Subject.findByTeacher(id) != null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lehrperson ist noch aktiven Fächern zugeteilt.");
			}
		}

		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "DELETE FROM user WHERE userID=" + id;
			Statement stmt = con.createStatement();
			int affected = stmt.executeUpdate(query);

			if (affected == 0) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		} catch (SQLException e) {
			throw e;
		}		
	}

}
