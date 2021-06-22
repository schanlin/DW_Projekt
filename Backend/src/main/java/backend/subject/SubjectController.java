package backend.subject;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import backend.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SubjectController {
	private final SubjectDao subjectDao;

	public SubjectController(SubjectDao s) {
		this.subjectDao = s;
	}
	
	@GetMapping("/subject")
	public List<Subject> getAllSubjects() {
		List<Subject> subjects = subjectDao.findAll();
		return subjects;
	}
	
	@GetMapping("/subject/{id}")
	public Subject getSubjectById(@PathVariable int id) {
		Subject subject = subjectDao.findById(id);
		if (subject==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return subject;
	}
	
	@DeleteMapping("/subject/{id}")
	public void deleteSubject(@PathVariable int id) {
		int status = subjectDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
