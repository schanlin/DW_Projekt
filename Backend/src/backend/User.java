package backend;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder ({"userID", "username", "firstname", "lastname"})
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
	
	

}
