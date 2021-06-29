package backend.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;

public class User {
	private int userID;
	private String username;
	private String password;
	private String email;
	private String firstname;
	private String lastname;
	private String rolle;

	public User(int userID, String username, String email, String firstname, String lastname, String rolle) {
		this.userID = userID;
		this.username = username;
		this.password = null;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rolle = rolle;
	}
	
	public User(int userID, String username, String password, String email, String firstname, String lastname, String rolle) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rolle = rolle;
	}

	public User(int userID, String username, String email, String firstname, String lastname) {
		this.userID = userID;
		this.username = username;
		this.password = null;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rolle = null;
	}

	@JsonCreator
	public User(String username, String password, String email, String firstname, String lastname, String rolle) {
		this.userID = 0;
		this.username = username;
		this.password = password;
		this.email = email;
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
	public String getEmail() {
		return email;
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



}
