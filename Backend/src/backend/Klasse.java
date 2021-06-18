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
	
	public Klasse(String name) {
		this.klassenName = name;
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
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)) {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			throw e;
		}		
	}
	
	public void insert() throws SQLException {
		String query = "INSERT INTO klasse (name) VALUES (?)";
		
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, this.klassenName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public static void insertData() throws SQLException {
		Klasse klasse1 = new Klasse("2016a");
		Klasse klasse2 = new Klasse("2016b");
		klasse1.insert();
		klasse2.insert();
	}

	public static List<Klasse> findAll() throws SQLException {
		List<Klasse> klassen = new LinkedList<>();
		
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM klasse";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				klassen.add(new Klasse(rs.getInt(1), rs.getString(2)));
			}
				
			return klassen;
		} catch (SQLException e) {
			throw e;
		}	
	}

	public static Klasse findByStudent(int studentID) throws SQLException {
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM klasse JOIN user ON user.klassenID = klasse.klassenID WHERE userID=" + studentID;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				return new Klasse(rs.getInt(1), rs.getString(2));
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		} catch (SQLException e) {
			throw e;
		}		
	}

	public static Klasse findById(int id) throws SQLException {
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM klasse WHERE klassenID=" + id;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				return new Klasse(rs.getInt(1), rs.getString(2));
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		} catch (SQLException e) {
			throw e;
		}		
	}
	
	public static Klasse findByName(String name) throws SQLException {
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM klasse WHERE name=" + name;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				return new Klasse(rs.getInt(1), rs.getString(2));
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		} catch (SQLException e) {
			throw e;
		}
	}

}
