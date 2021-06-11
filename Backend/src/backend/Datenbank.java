package backend;

import java.sql.*;

public class Datenbank {
	public static final String url = System.getProperty("dw.database.url");
	public static final String user = System.getProperty("dw.database.user");
	public static final String password = System.getProperty("dw.database.password");
	
	public static void main(String[] args) {
		
		try (Connection con = DriverManager.getConnection(url, user, password)){
			System.out.println("Erfolgreich mit Datenbank verbunden.");
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}

}
