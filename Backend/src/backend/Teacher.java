package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Teacher extends User{
	
	public Teacher(int userID, String username, String vorname, String nachname) {
		super(userID, username, vorname, nachname);
	}
	
	public List<Subject> getSubjects() throws SQLException {
		List<Subject> subjects = Subject.findByTeacher(this.getUserID());
		return subjects;
	}
	
	public static List<Teacher> findAll() throws SQLException {
		List<Teacher> teachers = new LinkedList<>();
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT userID, username, vorname, nachname FROM user WHERE rolle='Lehrende'";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			teachers.add(new Teacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
		}
		return teachers;
	}
	
	public static Teacher findById(int id) throws SQLException {
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT userID, username, vorname, nachname "
					 + "FROM user WHERE rolle='Lehrende' AND userID=" + id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
	
		if (rs.next()) {
			return new Teacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
