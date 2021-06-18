package backend;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
	
	@GetMapping("/student")
	public List<Student> getAllStudents() throws SQLException {
		List<Student> students = new LinkedList<>();
		students = Student.findAll();
		return students;
	}
	
	@GetMapping("/student/{id}")
	public Student getStudentById(int id) throws SQLException {
		Student student = Student.findById(id);
		return student;
	}
}
