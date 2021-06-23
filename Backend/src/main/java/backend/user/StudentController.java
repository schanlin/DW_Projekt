package backend.user;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import backend.user.Student;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class StudentController {
	private final StudentDao studentDao;

	public StudentController(StudentDao studentDao) {
		this.studentDao = studentDao;
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
}
