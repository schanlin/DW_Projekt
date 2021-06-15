package backend;

import java.sql.*;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Subject {
	private final int subjectID;
	private String subjectName;
	private int klasseID;
	private int teacherID;
	private boolean archived;
	
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
	
	@JsonGetter
	public boolean isArchived() {
		return archived;
	}

	public Subject(int subjectID, String subjectName, int klasse, int teacher) {
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.klasseID = klasse;
		this.teacherID = teacher;
		this.archived = false;
	}
	
	public Subject(int subjectID, String subjectName, int klasse, int teacher, boolean archived) {
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.klasseID = klasse;
		this.teacherID = teacher;
		this.archived = archived;
	}
	
	public static void createTable() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS fach("
					 + "fachID int AUTO_INCREMENT NOT NULL,"
					 + "name varchar(256) NOT NULL,"
					 + "klassenID int,"
					 + "lehrID int,"
					 + "archiviert bool,"
					 + "PRIMARY KEY(fachID),"
					 + "FOREIGN KEY(klassenID) REFERENCES klasse(klassenID),"
					 + "FOREIGN KEY(lehrID) REFERENCES user(userID))";
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
	}
	
	public static List<Subject> findAll() throws SQLException {
		List<Subject> subjects = new LinkedList<>();
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT * FROM fach";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
	
		while(rs.next()) {
			subjects.add(new Subject(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getBoolean(5)));
		}
		return subjects;
	}
	
	public static Subject findById(int id) throws SQLException {
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT * FROM fach WHERE fachID=" + id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		if (rs.next()) {
			return new Subject(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getBoolean(5));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} 
	}
	
	public static List<Subject> findByTeacher(int teacherID) throws SQLException {
		List<Subject> subjects = new LinkedList<>();
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT * FROM fach WHERE teacher=" + teacherID;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()) {
			subjects.add(new Subject(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
		}

		return subjects;
	}
	
	public static void archive(List<Subject> toArchive) throws SQLException {
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
			for (Subject subject: toArchive) {
				String query = "UPDATE fach SET archived=TRUE WHERE fachID=" + subject.subjectID;
				Statement stmt = con.createStatement();
				stmt.executeUpdate(query);
			}
	}
}
