package backend;

import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
	
	@GetMapping("/student")
	public List<Student> getAllStudents(){
		List<Student> students = new LinkedList<>();
		students = Student.findAllStudents();
		return students;
	}
	
	@GetMapping("/student/{id}")
	public Student getStudentById(int id) {
		Student student = Student.findById(id);
		return student;
	}
}
