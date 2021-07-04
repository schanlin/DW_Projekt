package backend.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserDao userDao;

	public UserController(UserDao userDao){
		this.userDao = userDao;
	}

	@Operation(summary = "Add a new user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User was created successfully",
				content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = User.class))}),
			@ApiResponse(responseCode = "400", description = "Username is already taken",
				content = @Content)
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User postUser(@RequestBody User user) {
		List<String> usernames = userDao.findAllUsernames();
		if (usernames.contains(user.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken");
		}
		return userDao.insert(user);
	}

	@Operation(summary = "Get all users")
	@ApiResponse(responseCode = "200", description = "All available users found",
			content = { @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = User.class)))})
	@GetMapping
	public List<User> getAllUsers() {
		List<User> users = userDao.findAll();
		return users;
	}

	@Operation(summary = "Get the userdata of the user who is logged into the current session")
	@ApiResponse(responseCode = "200", description = "Userdata found",
		content = {@Content(mediaType = "application/json",
		schema = @Schema(implementation = User.class))})
	@GetMapping("/current")
	public User getActiveUser(Principal principal) {
		return userDao.findByUsername(principal.getName());
	}

	@Operation(summary = "Get a user by their id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User found",
				content = {@Content(mediaType = "application/json",
				schema = @Schema(implementation = User.class))}),
			@ApiResponse(responseCode = "404", description = "User not found",
				content = @Content)
	})
	@GetMapping("/{id}")
	public User getUserById(@Parameter(description = "id of the user to be searched") @PathVariable int id) {
		User found = userDao.findById(id);

		if (found==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return found;
	}

	@Operation(summary = "Update a user's attributes")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "User updated successfully"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void putUser(@RequestBody User user, @Parameter(description = "The id of the user to be updated") @PathVariable int id) {
		int status = userDao.update(user, id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Update password of a user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Password updated successfully"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PutMapping("/{id}/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(@RequestBody ChangePasswordRequest request, @Parameter(description = "Id of the user who's password should be updated") @PathVariable int id) {
		int status = userDao.updatePassword(request.getNewPassword(), id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Delete a user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Deleted user successfully"),
			@ApiResponse(responseCode = "403", description = "Not allowed to delete this user"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@Parameter(description = "Id of the user to be deleted") @PathVariable int id) {
		int status = userDao.delete(id);
		if (status==-1) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete teachers with active subjects.");
		} else if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} else if (status==-2) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete last remaining admin.");
		}
	}

}
