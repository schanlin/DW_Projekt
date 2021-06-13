package backend;

import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectController {
	
	@GetMapping("/subject")
	public List<Subject> getAllSubjects(){
		List<Subject> subjects = new LinkedList<>();
		subjects = Subject.findAllSubjects();
		return subjects;
	}
	
	@GetMapping("/subject/{id}")
	public Subject getSubjectById(@PathVariable int id) {
		Subject subject = Subject.findById(id);
		return subject;
	}

}
