package backend;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeacherController {
	
	@GetMapping("/teacher")
	public List<Teacher> getAllTeachers() throws SQLException {
		List<Teacher> teachers = new LinkedList<>();
		teachers = Teacher.findAll();
		return teachers;
	}
	
	@GetMapping("/teacher/{id}")
	public Teacher getTeacherById(@PathVariable int id) throws SQLException {
		Teacher teacher = Teacher.findById(id);
		return teacher;
	}

}
