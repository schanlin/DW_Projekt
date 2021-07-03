package backend;

import java.io.FileInputStream;
import java.util.Properties;

public class Database {
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
}
