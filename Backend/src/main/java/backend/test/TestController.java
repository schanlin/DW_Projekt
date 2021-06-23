package backend.test;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TestController {
	private final TestDao testDao;

	public TestController(TestDao testDao) {
		this.testDao = testDao;
	}

	@PostMapping("/test")
	public Test postTest(@RequestBody Test test) {
		return testDao.insert(test);
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

	@PutMapping("/test/{id}")
	public void putTest(@RequestBody Test test, @PathVariable int id) {
		int status = testDao.update(test, id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/test/{id}")
	public void deleteTest(@PathVariable int id) {
		int status = testDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
