package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Student extends User{
	private String klasse;
	
	public Student(int userID, String username, String vorname, String nachname, String klasse) {
		super(userID, username, vorname, nachname);
		this.klasse = klasse;
	}
	
	@JsonGetter
	public String getKlasse() {
		return klasse;
	}
	
	public static List<Student> findAll() throws SQLException{
		List<Student> students = new LinkedList<>();
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT userID, username, vorname, nachname, klasse.name "
					 + "FROM users JOIN klasse ON users.klassenID = klasse.ID WHERE rolle='Lernende'";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		while(rs.next()) {
				students.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
		}
		return students;
	}
	
	public static Student findById(int id) throws SQLException {
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT userID, username, vorname, nachname, klasse.name"
					 + "FROM users JOIN klasse ON users.klassenID = klasse.ID WHERE rolle='Lernende' "
					 + "AND userID=" + id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		if (rs.next()) {
			return new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	public static void deassign(int id) throws SQLException {
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "UPDATE user SET klassenID=null WHERE userID=" + id;
		Statement stmt = con.createStatement();
		int affected = stmt.executeUpdate(query);
		if (affected==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	public static void deassignAll(int klassenID) throws SQLException {
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "UPDATE user SET klassenID=null WHERE klassenID=" + klassenID;
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
	}
	
	public static void delete() {
//		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "DELETE FROM user, ergebnis USING ergebnis JOIN user ON";
	}
	
}
