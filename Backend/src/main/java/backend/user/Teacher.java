package backend.user;

import java.sql.*;
import java.util.List;

import backend.subject.Subject;

public class Teacher extends User {
	
	public Teacher(int userID, String username, String vorname, String nachname, String rolle) {
		super(userID, username, vorname, nachname, rolle);
	}

}
