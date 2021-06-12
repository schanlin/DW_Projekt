package backend;

import java.sql.*;
import java.util.List;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Subject {
	private final int subjectID;
	private String subjectName;
	private int klasseID;
	private int teacherID;
	
	@JsonGetter
	public String getSubjectName() {
		return subjectName;
	}

	@JsonGetter
	public int getKlasse() {
		return klasseID;
	}

	@JsonGetter
	public int getTeacher() {
		return teacherID;
	}

//	public List<Test> getTests() {
//		return tests;
//	}

	@JsonGetter
	public int getSubjectID() {
		return subjectID;
	}

	public Subject(int subjectID, String subjectName, int klasse, int teacher) {
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.klasseID = klasse;
		this.teacherID = teacher;
	}
	
	public static boolean createTable() {
		String query = "CREATE TABLE fach("
					 + "fachID int AUTO_INCREMENT NOT NULL,"
					 + "name varchar(256) NOT NULL,"
					 + "klassenID int,"
					 + "lehrID int,"
					 + "PRIMARY KEY(fachID),"
					 + "FOREIGN KEY(klassenID) REFERENCES klasse(klassenID),"
					 + "FOREIGN KEY(lehrID) REFERENCES user(userID))";
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	
	public static List<Subject> findAllSubjects(){
		List<Subject> subjects = new LinkedList<>();
		try(Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM fach";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				subjects.add(new Subject(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return subjects;
	}
	
	public static List<Subject> findSubjectsByTeacher(int teacherID){
		List<Subject> subjects = new LinkedList<>();
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM fach WHERE teacher=" + teacherID;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				subjects.add(new Subject(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return subjects;
	}
}
