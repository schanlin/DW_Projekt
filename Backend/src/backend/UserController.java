package backend;

import java.util.LinkedList;
import java.util.List;
import java.sql.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@GetMapping("/user")
	public List<User> getAllUsers(){
		List<User> users = User.findAllUsers();
		return users;
	}
	
	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable int id) {
		User user = User.findById(id);
		return user;
	}

}
