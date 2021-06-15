package backend;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/test")
	public List<Test> getAllTests() throws SQLException {
		List<Test> tests = new LinkedList<>();
		tests = Test.findAllTests();
		return tests;
	}
	
	@GetMapping("/test/{id}")
	public Test getTestById(@PathVariable int id) throws SQLException {
		Test test = Test.findById(id);
		return test;
	}

}
