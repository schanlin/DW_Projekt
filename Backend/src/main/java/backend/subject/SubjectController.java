package backend.subject;

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

import java.util.List;

@RestController
public class SubjectController {
	private final SubjectDao subjectDao;

	public SubjectController(SubjectDao s) {
		this.subjectDao = s;
	}

	@Operation(summary = "Create a new subject")
	@ApiResponses( value = {
			@ApiResponse(responseCode = "201", description = "Subject was created successfully",
					content = {@Content(mediaType = "application/json",
					schema = @Schema(implementation = Subject.class))}),
			@ApiResponse(responseCode = "400", description = "Name already taken for this class",
			content = @Content)
	})

	@PostMapping("/subject")
	@ResponseStatus(HttpStatus.CREATED)
	public Subject postSubject(@RequestBody Subject subject) {
		List<String> subjectNames = subjectDao.findAllSubjectnamesByClass(subject.getKlasse());
		if (subjectNames.contains(subject.getSubjectName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject with that name already exists for this class.");
		}
		return subjectDao.insert(subject);
	}

	@Operation(summary = "Get all subjects")
	@ApiResponse(responseCode = "200", description = "Returned all subjects",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = Subject.class)))})
	@GetMapping("/subject")
	public List<Subject> getAllSubjects() {
		List<Subject> subjects = subjectDao.findAll();
		return subjects;
	}

	@Operation(summary = "Get a subject by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Subject found",
				content = {@Content(mediaType = "application/json",
				schema = @Schema(implementation = Subject.class))}),
			@ApiResponse(responseCode = "404", description = "Subject not found",
				content = @Content)
	})
	@GetMapping("/subject/{id}")
	public Subject getSubjectById(@Parameter(description = "The id of the subject to be searched") @PathVariable int id) {
		Subject subject = subjectDao.findById(id);
		if (subject==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return subject;
	}

	@Operation(summary = "Update subject attributes")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Subject updated successfully"),
			@ApiResponse(responseCode = "205", description = "Subject was archived, teacher and class were set to <null>"),
			@ApiResponse(responseCode = "403", description = "Subjects without dependencies should be deleted instead of archived"),
			@ApiResponse(responseCode = "404", description = "Subject not found"),
			@ApiResponse(responseCode = "409", description = "Cannot make changes to archived subjects"),
	})
	@PutMapping("/subject/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void putSubject(@RequestBody Subject subject, @Parameter(description = "Id of the subject to be updated") @PathVariable int id) {
		int status = subjectDao.update(subject, id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		if (status==-1) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot make any changes to archived subjects.");
		}
		if (status==-2) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Subjects without dependent tests should be deleted instead of archived.");
		}
		if (status==-3) {
			throw new ResponseStatusException(HttpStatus.RESET_CONTENT, "Subject was archived, teacher and class set to 'null'.");
		}
	}

	@Operation(summary = "Delete a subject")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Subject deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Subject not found"),
			@ApiResponse(responseCode = "409", description = "Cannot delete subjects with dependent tests")
	})
	@DeleteMapping("/subject/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteSubject(@PathVariable int id) {
		int status = subjectDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		if (status==-1) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete subjects with dependent tests.");
		}
	}

}
