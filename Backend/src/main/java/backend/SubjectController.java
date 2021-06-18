package backend;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectController {
	
	@GetMapping("/subject")
	public List<Subject> getAllSubjects() throws SQLException {
		List<Subject> subjects = new LinkedList<>();
		subjects = Subject.findAll();
		return subjects;
	}
	
	@GetMapping("/subject/{id}")
	public Subject getSubjectById(@PathVariable int id) throws SQLException {
		Subject subject = Subject.findById(id);
		return subject;
	}
	
//	@DeleteMapping("/subject/{id}")
//	public boolean deleteSubject(int id) {
//		
//	}

}
