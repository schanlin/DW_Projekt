package backend;

import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeacherController {
	
	@GetMapping("/teacher")
	public List<Teacher> getAllTeachers(){
		List<Teacher> teachers = new LinkedList<>();
		teachers = Teacher.findAllTeachers();
		return teachers;
	}
	
	@GetMapping("/teacher/{id}")
	public Teacher getTeacherById(@PathVariable int id) {
		Teacher teacher = Teacher.findById(id);
		return teacher;
	}

}
