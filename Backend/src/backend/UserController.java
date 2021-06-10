package backend;

import java.util.LinkedList;
import java.util.List;
import java.sql.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@GetMapping("/user")
	public List<User> getAllUsers(){
		List<User> users = new LinkedList<>();
		try (Connection con = 
			 DriverManager.getConnection("jdbc:mysql://localhost:3306/dw", "root", "Feuersturm11")){
			String query = "SELECT userID, username, vorname, nachname FROM users";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return users;
	}

}
