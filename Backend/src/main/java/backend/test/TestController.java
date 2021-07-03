package backend.test;

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
public class TestController {
	private final TestDao testDao;

	public TestController(TestDao testDao) {
		this.testDao = testDao;
	}

	@Operation(summary = "Create a new test")
	@ApiResponse(responseCode = "201", description = "Test was created successfully",
		content = { @Content(mediaType = "application/json",
		schema = @Schema(implementation = Test.class))})
	@PostMapping("/test")
	@ResponseStatus(HttpStatus.CREATED)
	public Test postTest(@RequestBody Test test) {
		return testDao.insert(test);
	}

	@Operation(summary = "Get all tests")
	@ApiResponse(responseCode = "200", description = "Returned all tests",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = Test.class)))})
	@GetMapping("/test")
	public List<Test> getAllTests() {
		List<Test> tests = testDao.findAll();
		return tests;
	}

	@Operation(summary = "Get a test by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Test found",
				content = {@Content(mediaType = "application/json",
				schema = @Schema(implementation = Test.class))}),
			@ApiResponse(responseCode = "404", description = "Test not found",
				content = @Content)
	})
	@GetMapping("/test/{id}")
	public Test getTestById(@Parameter(description = "The id of the test to be searched") @PathVariable int id) {
		Test test = testDao.findById(id);
		if (test==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return test;
	}

	@Operation(summary = "Update a test's attributes")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Test updated successfully"),
			@ApiResponse(responseCode = "403", description = "Not allowed to update archived tests"),
			@ApiResponse(responseCode = "404", description = "Test not found")
	})
	@PutMapping("/test/{id}")
	public void putTest(@RequestBody Test test,@Parameter(description = "The id of the test to be updated") @PathVariable int id) {
		if (testDao.isArchived(id)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to update archived tests");
		}
		int status = testDao.update(test, id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Delete a test")
	@ApiResponses( value = {
			@ApiResponse(responseCode = "204", description = "Test deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Test not found")
	})
	@DeleteMapping("/test/{id}")
	public void deleteTest(@Parameter(description = "Id of the test to be deleted") @PathVariable int id) {
		int status = testDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
