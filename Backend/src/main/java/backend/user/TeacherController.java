package backend.user;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import backend.user.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TeacherController {
	private final TeacherDao teacherDao;

	public TeacherController(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}
	
	@GetMapping("/teacher")
	public List<Teacher> getAllTeachers() throws SQLException {
		List<Teacher> teachers = teacherDao.findAll();
		return teachers;
	}
	
	@GetMapping("/teacher/{id}")
	public Teacher getTeacherById(@PathVariable int id) throws SQLException {
		Teacher teacher = teacherDao.findById(id);
		if (teacher==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return teacher;
	}

}
