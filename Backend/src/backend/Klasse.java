package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Klasse {
	private int klassenID;
	private String klassenName;

	public Klasse(int klassenID, String klassenName) {
		this.klassenID = klassenID;
		this.klassenName = klassenName;
	}

	@JsonGetter
	public String getKlassenName() {
		return klassenName;
	}

//	public List<Student> getStudents() {
//		
//	}

	@JsonGetter
	public int getKlassenID() {
		return klassenID;
	}

	public static void createTable() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS klasse(" 
					 + "klassenID int AUTO_INCREMENT NOT NULL,"
				     + "name varchar(256) NOT NULL," 
					 + "PRIMARY KEY(klassenID)," 
				     + "UNIQUE(name))";
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
	}

	public static void insertData() throws SQLException {
		List<Klasse> current = findAll();
		if (current.isEmpty()) {
			String klasse1 = "INSERT INTO klasse (name) VALUES ('2016a')";
			String klasse2 = "INSERT INTO klasse (name) VALUES ('2016b')";
			Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(klasse1);
			stmt.executeUpdate(klasse2);
		}
	}

	public static List<Klasse> findAll() throws SQLException {
		List<Klasse> klassen = new LinkedList<>();
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT * FROM klasse";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()) {
			klassen.add(new Klasse(rs.getInt(1), rs.getString(2)));
		}
			
		return klassen;
	}

	public static Klasse findByStudent(int studentID) throws SQLException{
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT * FROM klasse JOIN user ON user.klassenID = klasse.klassenID WHERE userID=" + studentID;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		if (rs.next()) {
			return new Klasse(rs.getInt(1), rs.getString(2));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	public static Klasse findKlasseById(int id) throws SQLException{
		Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password);
		String query = "SELECT * FROM klasse WHERE klassenID=" + id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		if (rs.next()) {
			return new Klasse(rs.getInt(1), rs.getString(2));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
