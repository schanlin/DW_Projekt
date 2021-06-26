package backend.user;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import backend.subject.Subject;
import backend.subject.SubjectDao;
import backend.user.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TeacherController {
	private final TeacherDao teacherDao;
	private  final SubjectDao subjectDao;

	public TeacherController(TeacherDao teacherDao, SubjectDao subjectDao) {
		this.teacherDao = teacherDao;
		this.subjectDao = subjectDao;
	}
	
	@GetMapping("/teacher")
	public List<Teacher> getAllTeachers() {
		List<Teacher> teachers = teacherDao.findAll();
		return teachers;
	}
	
	@GetMapping("/teacher/{id}")
	public Teacher getTeacherById(@PathVariable int id) {
		Teacher teacher = teacherDao.findById(id);
		if (teacher==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return teacher;
	}

	@GetMapping("/teacher/{id}/subjects")
	public List<Subject> getSubjectsByTeacher(@PathVariable int id) {
		return subjectDao.findByTeacher(id);
	}

	@GetMapping("/teacher/{teacherId}/subjects/{subjectId}/overview")
	public List<AverageByStudent> getAverageByStudent(@PathVariable int teacherId, @PathVariable int subjectId) {

	}

}
