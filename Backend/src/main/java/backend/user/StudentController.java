package backend.user;

import backend.test_result.AverageBySubject;
import backend.test_result.TestResult;
import backend.test_result.TestResultDao;
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
public class StudentController {
	private final StudentDao studentDao;
	private final TestResultDao testResultDao;

	public StudentController(StudentDao studentDao, TestResultDao testResultDao) {
		this.studentDao = studentDao;
		this.testResultDao = testResultDao;
	}

	@Operation(summary = "Create a new student")
	@ApiResponse(responseCode = "201", description = "Student created successfully",
		content = {@Content(mediaType = "application/json",
		schema = @Schema(implementation = Student.class))})
	@PostMapping("/student")
	@ResponseStatus(HttpStatus.CREATED)
	public Student postStudent(@RequestBody Student student) {
		return studentDao.insert(student);
	}

	@Operation(summary = "Get all students")
	@ApiResponse(responseCode = "200", description = "Returned all students",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = Student.class)))})
	@GetMapping("/student")
	public List<Student> getAllStudents() {
		return studentDao.findAll();
	}

	@Operation(summary = "Get a student by their userId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student found",
				content = {@Content(mediaType = "application/json",
				schema = @Schema(implementation = Student.class))}),
			@ApiResponse(responseCode = "404", description = "Student not found",
				content = @Content)
	})
	@GetMapping("/student/{id}")
	public Student getStudentById(@Parameter(description = "The userId of the student to be found") @PathVariable int id) {
		Student student = studentDao.findById(id);
		if (student==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return student;
	}

	@Operation(summary = "Delete a student")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Student deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Student not found")
	})
	@DeleteMapping("/student/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteStudent(@Parameter(description = "The userId of the student to be deleted") @PathVariable int id) {
		int status = studentDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Get a student's averages in all their subjects")
	@ApiResponse(responseCode = "200", description = "Returned all averages",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = AverageBySubject.class)))})
	@GetMapping("/student/{studentId}/subjects")
	public List<AverageBySubject> getAverageBySubject(@Parameter(description = "The userId of the student")
																	@PathVariable int studentId) {
		return testResultDao.findAllAveragesBySubject(studentId);
	}

	@Operation(summary = "Get a student's test results by subject")
	@ApiResponse(responseCode = "200", description = "Returned all results",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = TestResult.class)))})
	@GetMapping("/student/{studentId}/subjects/{subjectId}/results")
	public List<TestResult> getResultsByStudentAndSubject(@Parameter(description = "The userId of the student") @PathVariable int studentId,
														  @Parameter(description = "The id of the subject") @PathVariable int subjectId) {
		return testResultDao.findBySubjectAndStudent(studentId, subjectId);
	}
}
