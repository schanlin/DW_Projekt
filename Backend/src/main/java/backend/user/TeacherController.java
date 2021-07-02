package backend.user;

import backend.subject.Subject;
import backend.subject.SubjectDao;
import backend.test.Test;
import backend.test.TestDao;
import backend.test_result.AverageByStudent;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TeacherController {
	private final TeacherDao teacherDao;
	private final SubjectDao subjectDao;
	private final TestDao testDao;
	private final TestResultDao testResultDao;

	public TeacherController(TeacherDao teacherDao, SubjectDao subjectDao, TestDao testDao, TestResultDao testResultDao) {
		this.teacherDao = teacherDao;
		this.subjectDao = subjectDao;
		this.testDao = testDao;
		this.testResultDao = testResultDao;
	}

	@Operation(summary = "Get all teachers")
	@ApiResponse(responseCode = "200", description = "Returned all teachers",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = User.class)))})
	@GetMapping("/teacher")
	public List<Teacher> getAllTeachers() {
		List<Teacher> teachers = teacherDao.findAll();
		return teachers;
	}

	@Operation(summary = "Get a teacher by userId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Teacher found",
			content = {@Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = User.class)))}),
			@ApiResponse(responseCode = "404", description = "Teacher not found")
	})
	@GetMapping("/teacher/{id}")
	public Teacher getTeacherById(@PathVariable int id) {
		Teacher teacher = teacherDao.findById(id);
		if (teacher==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return teacher;
	}

	@Operation(summary = "Get a list of all subjects")
	@ApiResponse(responseCode = "200", description = "Returned all subjects",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = Subject.class)))})
	@GetMapping("/teacher/{id}/subjects")
	public List<Subject> getSubjectsByTeacher(@Parameter(description = "The id of the teacher whose subjects are to be searched")
												@PathVariable int id) {
		return subjectDao.findByTeacher(id);
	}


	@Operation(summary = "Get averages of all students from a subject")
	@ApiResponse(responseCode = "200", description = "Returned all Averages",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = AverageByStudent.class)))})
	@GetMapping("/teacher/subjects/{subjectId}/overview")
	public List<AverageByStudent> getAverageByStudent(@Parameter(description = "Id of the subject you want the overview of")
														  @PathVariable int subjectId) {
		return testResultDao.findAllAveragesByStudent(subjectId);
	}

	@Operation(summary = "Get all tests for a subject")
	@ApiResponse(responseCode = "200", description = "Returned all tests",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = Test.class)))})
	@GetMapping("/teacher/subjects/{subjectId}/tests")
	public List<Test> getTestsBySubject(@Parameter(description = "The id of the subject you want the tests from")
													  @PathVariable int subjectId) {
		return testDao.findBySubject(subjectId);
	}

	@Operation(summary = "Get all results for a test")
	@ApiResponse(responseCode = "200", description = "Returned all results",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = TestResult.class)))})
	@GetMapping("/teacher/tests/{testId}/results")
	public List<TestResult> getResultsByTest(@Parameter(description = "The id if the test you want the results to")
												 @PathVariable int testId) {
		return testResultDao.findByTest(testId);
	}

	@Operation(summary = "Add or update a mark for a test")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "New result was saved",
				content = {@Content(mediaType = "application/json",
				schema = @Schema(implementation = TestResult.class))}),
			@ApiResponse(responseCode = "204", description = "Mark was updated",
				content = @Content),
			@ApiResponse(responseCode = "403", description = "Cannot change archived tests",
				content = @Content)
	})
	@PostMapping("/teacher/tests/results")
	public ResponseEntity postNewMark(@RequestBody TestResult testResult) {
		if (testDao.isArchived(testResult.getTestID())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot change archived tests");
		}
		int status = testResultDao.insertOrUpdate(testResult);
		if (status==1) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else if (status==2) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			throw new IllegalStateException();
		}
	}

}
