package backend.user;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import backend.test_result.AverageBySubject;
import backend.test_result.TestResult;
import backend.test_result.TestResultDao;
import backend.user.Student;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class StudentController {
	private final StudentDao studentDao;
	private final TestResultDao testResultDao;

	public StudentController(StudentDao studentDao, TestResultDao testResultDao) {
		this.studentDao = studentDao;
		this.testResultDao = testResultDao;
	}

	@PostMapping("/student")
	public Student postStudent(@RequestBody Student student) {
		return studentDao.insert(student);
	}
	
	@GetMapping("/student")
	public List<Student> getAllStudents() {
		List<Student> students = new LinkedList<>();
		students = studentDao.findAll();
		return students;
	}

	@GetMapping("/student/{id}")
	public Student getStudentById(@PathVariable int id) {
		Student student = studentDao.findById(id);
		if (student==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return student;
	}

	@DeleteMapping("/student/{id}")
	public void deleteStudent(@PathVariable int id) {
		int status = studentDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/student/{id}/subjects")
	public List<AverageBySubject> getAverageBySubjectAndStudent(@PathVariable int id) {
		return testResultDao.findAllAveragesBySubject(id);
	}

	@GetMapping("/student/{studentId}/subjects/{subjectId}/results")
	public List<TestResult> getResultsByStudentAndSubject(@PathVariable int studentId, @PathVariable int subjectId) {
		return testResultDao.findBySubjectAndStudent(studentId, subjectId);
	}
}
