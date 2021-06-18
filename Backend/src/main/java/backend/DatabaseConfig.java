package backend;

import backend.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.sql.SQLException;

@ConfigurationProperties(prefix = "dw.database")
public class DatabaseConfig {
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public  String url;
	public  String user;
	public  String password;

}
