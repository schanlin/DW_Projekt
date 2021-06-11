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
		String query = "CREATE TABLE user("
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
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	
	public static List<User> findAllUsers(){
		List<User> users = new LinkedList<>();
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username, vorname, nachname FROM users";
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

}
