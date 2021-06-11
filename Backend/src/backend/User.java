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
	
	public User() {}

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
	
	public static List<User> findAllUsers(){
		List<User> users = new LinkedList<>();
		try (Connection con = 
			 DriverManager.getConnection("jdbc:mysql://localhost:3306/dw", "root", "Feuersturm11")){
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
