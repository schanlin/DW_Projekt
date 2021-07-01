package backend.klasse;

import backend.user.StudentDao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;



@RestController
public class KlassenController {

	private final KlasseDao klasseDao;
	private final StudentDao studentDao;

	public KlassenController(KlasseDao klasseDao, StudentDao studentDao) {
		this.klasseDao = klasseDao;
		this.studentDao = studentDao;
	}

	@Operation(summary = "Create a new class")
	@ApiResponse(responseCode = "201", description = "Class created successfully",
			content = {@Content(mediaType = "application/json",
			schema = @Schema(implementation = Klasse.class))})
	@PostMapping("/klasse")
	@ResponseStatus(HttpStatus.CREATED)
	public Klasse postKlasse(@RequestBody Klasse klasse) {
		return klasseDao.insert(klasse);
	}

	@Operation(summary = "Get all classes")
	@ApiResponse(responseCode = "200", description = "returned all classes",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = Klasse.class)))})
	@GetMapping("/klasse")
	public List<Klasse> getAllKlassen() {
		return klasseDao.findAll();
	}

	@Operation(summary = "Get a class be its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Class found",
				content = {@Content(mediaType = "application/json",
				schema = @Schema(implementation = Klasse.class))}),
			@ApiResponse(responseCode = "404", description = "Class not found")
	})
	@GetMapping("/klasse/{id}")
	public Klasse getKlasseById(@Parameter(description = "The id of the class to be searched") @PathVariable int id) {
		Klasse klasse = klasseDao.findById(id);
		if (klasse==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return klasse;
	}

	@Operation(summary = "Update class attributes")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Class updated successfully"),
			@ApiResponse(responseCode = "404", description = "Class not found")
	})
	@PutMapping("/klasse/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void putKlasse(@RequestBody Klasse klasse,@Parameter(description = "The id of the class to be updated") @PathVariable int id) {
		int status = klasseDao.update(klasse, id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Assign or deassign a student")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Assignment successful"),
			@ApiResponse(responseCode = "404", description = "Student not found")
	})
	@PutMapping("/klasse/{klassenId}/assign/{studentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void assignStudent(@Parameter(description = "The id of the new class. Use 0 if you want to deassign") @PathVariable int klassenId,
							  @Parameter(description = "The id of the student who is to be reassigned") @PathVariable int userId) {
		int status;
		if(klassenId==0) {
			status = studentDao.deassign(userId);
			if (status==0) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		} else {
			status = studentDao.assign(klassenId, userId);
			if (status==0) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		}
	}

	@Operation(summary = "Delete a class")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Class deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Class not found")
	})
	@DeleteMapping("/klasse/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteKlasse(@PathVariable int id) {
		int status = klasseDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
