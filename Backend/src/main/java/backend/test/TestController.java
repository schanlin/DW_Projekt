package backend.test;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TestController {
	private final TestDao testDao;

	public TestController(TestDao testDao) {
		this.testDao = testDao;
	}

	@GetMapping("/test")
	public List<Test> getAllTests() {
		List<Test> tests = testDao.findAll();
		return tests;
	}
	
	@GetMapping("/test/{id}")
	public Test getTestById(@PathVariable int id) {
		Test test = testDao.findById(id);
		if (test==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return test;
	}

}
