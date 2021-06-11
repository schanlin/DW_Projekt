package backend;

import java.sql.*;

public class Datenbank {
	public static final String url = "jdbc:mysql://localhost:3306/test_db";
	public static final String user = "root";
	public static final String password = "Feuersturm11";
	
	public static void main(String[] args) {
		
		try (Connection con = DriverManager.getConnection(url, user, password)){
			System.out.println("Erfolgreich mit Datenbank verbunden.");
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}

}
