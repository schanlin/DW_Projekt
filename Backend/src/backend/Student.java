package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

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
	
	public static List<Student> findAllStudents(){
		List<Student> students = new LinkedList<>();
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username, vorname, nachname, klasse.name "
						 + "FROM users JOIN klasse ON users.klassenID = klasse.ID WHERE rolle='Lernende'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				students.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return students;
	}
	
	public static Student findById(int id){
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username, vorname, nachname, klasse.name"
						 + "FROM users JOIN klasse ON users.klassenID = klasse.ID WHERE rolle='Lernende' "
						 + "AND userID=" + id;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				return new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
}
