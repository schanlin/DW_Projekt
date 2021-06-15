package backend;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResultController {
	
	@GetMapping("/result")
	public List<TestResult> getAllTestResults() throws SQLException {
		List<TestResult> results = new LinkedList<>();
		results = TestResult.findAllResults();
		return results;
	}

}
