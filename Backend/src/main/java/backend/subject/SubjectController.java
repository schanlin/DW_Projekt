package backend.subject;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import backend.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SubjectController {
	private final SubjectDao subjectDao;

	public SubjectController(SubjectDao s) {
		this.subjectDao = s;
	}

	@PostMapping("/subject")
	@ResponseStatus(HttpStatus.CREATED)
	public Subject postSubject(@RequestBody Subject subject) {
		return subjectDao.insert(subject);
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

	@PutMapping("/subject/{id}")
	public void updateSubject(@RequestBody Subject subject, @PathVariable int id) {
		int status = subjectDao.update(subject, id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		if (status==-1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot make any changes to archived subjects.");
		}
	}

	@PutMapping("/subject/{id}/archive")
	public void archiveSubject(@PathVariable int id) {
		int status = subjectDao.archive(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/subject/{id}")
	public void deleteSubject(@PathVariable int id) {
		int status = subjectDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
