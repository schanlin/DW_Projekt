package backend.user;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

import backend.user.User;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserDao userDao;

	public UserController(UserDao userDao){
		this.userDao = userDao;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User postUser(@RequestBody User user) {
		List<String> usernames = userDao.findAllUsernames();
		if (usernames.contains(user.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken.");
		}
		return userDao.insert(user);
	}

	@GetMapping
	public List<User> getAllUsers() {
		List<User> users = userDao.findAll();
		return users;
	}

	@GetMapping("/current")
	public User getActiveUser(Principal principal) {
		return userDao.findByUsername(principal.getName());
	}
	
	@GetMapping("/{id}")
	public User getUserById(@PathVariable int id) {
		User found = userDao.findById(id);

		if (found==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return found;
	}

	@PutMapping("/{id}")
	public void putUser(@RequestBody User user, @PathVariable int id) {
		int status = userDao.update(user, id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id}/password")
	public void changePassword(@RequestBody ChangePasswordRequest request, @PathVariable int id) {
		int status = userDao.updatePassword(request.getNewPassword(), id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable int id) {
		int status = userDao.delete(userDao.findById(id));
		if (status<0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete teachers with active subjects.");
		} else if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
