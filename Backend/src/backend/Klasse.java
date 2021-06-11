package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

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
	
	public static List<Klasse> findAllKlassen(){
		List<Klasse> klassen = new LinkedList<>();
		try(Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM klasse";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				klassen.add(new Klasse(rs.getInt(1), rs.getString(2)));
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return klassen;
	}
	
	public static Klasse findKlasseByStudent(int studentID) {
		Klasse result = null;
		try(Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM lernende WHERE userID=" + studentID;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				return result = Klasse.getKlasseById(rs.getInt(1));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	public static Klasse getKlasseById(int id) {
		Klasse result = null;
		return result;
	}
	
}
