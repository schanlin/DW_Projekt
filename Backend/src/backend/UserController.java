package backend;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@GetMapping("/user")
	public List<User> getAllUsers() throws SQLException {
		List<User> users = User.findAllUsers();
		return users;
	}
	
	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable int id) throws SQLException {
		User user = User.findById(id);
		return user;
	}
	
	@DeleteMapping("/user/{id}")
	public void deleteUser(@PathVariable int id) throws SQLException {
		User.delete(id);
	}

}
