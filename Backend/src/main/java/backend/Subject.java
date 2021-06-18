package backend;

import java.sql.*;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Subject {
	private int subjectID;
	private String subjectName;
	private String klasse;
	private String teacher;
	private boolean archived;
	
	@JsonGetter
	public String getSubjectName() {
		return subjectName;
	}

	@JsonGetter
	public String getKlasse() {
		return klasse;
	}

	@JsonGetter
	public String getTeacher() {
		return teacher;
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

	public Subject(int subjectID, String subjectName, String klasse, String name) {
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.klasse = klasse;
		this.teacher = name;
		this.archived = false;
	}
	
	public Subject(int subjectID, String subjectName, String klasse, String teacher, boolean archived) {
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.klasse = klasse;
		this.teacher = teacher;
		this.archived = archived;
	}
	
	public Subject(String name, String klassenName, String teacherName) {
		this.subjectName = name;
		this.klasse = klassenName;
		this.teacher = teacherName;
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
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public void insert() throws SQLException {
		String query = "INSERT INTO fach(name, klassenID, lehrID, archiviert) VALUES(?, ?, ?, ?)";
		
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, this.subjectName);
			//stmt.setInt(2, Klasse.findByName(this.klasse).getKlassenID());
			stmt.setInt(3, Teacher.findByName(this.teacher).getUserID());
			stmt.setBoolean(4, false);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public static void insertData() throws SQLException {
		Subject[] testSubjects = new Subject[6];
		testSubjects[0] = new Subject("Deutsch", "2016a", "Zoya Nazyalenski");
		testSubjects[1] = new Subject("Mathematik", "2016a", "Jarl Brum");
		testSubjects[2] = new Subject("Biologie", "2016a", "Zoya Nazyalenski");
		testSubjects[3] = new Subject("Deutsch", "2016b", "Zoya Nazyalenski");
		testSubjects[4] = new Subject("Mathematik", "2016b", "Jarl Brum");
		testSubjects[5] = new Subject("Biologie", "2016b", "Jarl Brum");
		for (Subject sub: testSubjects) {
			sub.insert();
		}
	}
	
	public static List<Subject> findAll() throws SQLException {
		List<Subject> subjects = new LinkedList<>();
		
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT fach.fachID, fach.name, klasse.name, user.vorname, user.nachname, fach.archiviert "
						 + "FROM ((fach JOIN klasse ON fach.klassenID = klasse.klassenID) "
						 + "JOIN user ON fach.lehrID=user.userID)";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()) {
				subjects.add(new Subject(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4) + rs.getString(5), rs.getBoolean(6)));
			}
			return subjects;
		} catch (SQLException e) {
			throw e;
		}		
	}
	
	public static Subject findById(int id) throws SQLException {
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT fach.fachID, fach.name, klasse.name, user.vorname, user.nachname, fach.archiviert "
					 	 + "FROM ((fach JOIN klasse ON fach.klassenID = klasse.klassenID) "
					 	 + "JOIN user ON fach.lehrID=user.userID) WHERE fachID=" + id;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				return new Subject(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4) + rs.getString(5), rs.getBoolean(6));
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		} catch (SQLException e) {
			throw e;
		}		 
	}
	
	public static List<Subject> findByTeacher(int teacherID) throws SQLException {
		List<Subject> subjects = new LinkedList<>();
		
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT fach.fachID, fach.name, klasse.name, user.vorname, user.nachname, fach.archiviert "
				 	 	 + "FROM ((fach JOIN klasse ON fach.klassenID = klasse.klassenID) "
				 	 	 + "JOIN user ON fach.lehrID=user.userID) WHERE lehrID=" + teacherID;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				subjects.add(new Subject(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4) + rs.getString(5), rs.getBoolean(6)));
			}

			return subjects;
		} catch (SQLException e) {
			throw e;
		}		
	}
	
	public static void archive(List<Subject> toArchive) throws SQLException {
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			for (Subject subject: toArchive) {
				String query = "UPDATE fach SET archived=TRUE WHERE fachID=" + subject.subjectID;
				Statement stmt = con.createStatement();
				stmt.executeUpdate(query);
			}
		} catch (SQLException e) {
			throw e;
		}			
	}
}
