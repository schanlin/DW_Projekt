package backend;

import java.sql.*;

public class Datenbank {
	
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/test_db";
		String user = "root";
		String password = "Feuersturm11";
		
		try (Connection con = DriverManager.getConnection(url, user, password)){
			System.out.println("Erfolgreich mit Datenbank verbunden.");
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}

}
