package backend;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Datenbank {
	public static String url;
	public static String user;
	public static String password;
	
	public static void loadProperties() {
		Properties properties = new Properties();
		try {
			FileInputStream in = new FileInputStream("application.properties");
			properties.load(in);
			in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		url = properties.getProperty("dw.database.url");
		user = properties.getProperty("dw.database.user");
		password = properties.getProperty("dw.database.password");		
	}
	
	public static void initializeDatabase(){
		try {
			Klasse.createTable();
			Klasse.insertData();
			User.createTable();
			User.insertData();
		} catch (SQLException e) {
			System.err.println("Tabellen konnten nicht angelegt werden: " + e.getMessage());
		}
	}
}
