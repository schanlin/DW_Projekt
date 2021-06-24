package backend.user;

import java.sql.SQLException;
import java.util.List;

import backend.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserDao userDao;

	public UserController(UserDao userDao){
		this.userDao = userDao;
	}

	@GetMapping("/hello")
	public String hello() {
		return "hello world";
	}

	@PostMapping
	public User postUser(@RequestBody User user) {
		return userDao.insert(user);
	}

	@GetMapping
	public List<User> getAllUsers() {
		List<User> users = userDao.findAll();
		return users;
	}
	
	@GetMapping("/{id}")
	public User getUserById(@PathVariable int id) {
		User user = userDao.findById(id);
		if (user==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return user;
	}

	@PutMapping("/{id}")
	public void putUser(@RequestBody User user, @PathVariable int id) {
		int status = userDao.update(user, id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable int id) throws SQLException {
		int status = userDao.delete(userDao.findById(id));
		if (status<0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete teachers with active subjects.");
		} else if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
