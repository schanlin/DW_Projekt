package backend.test_result;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import backend.test_result.TestResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResultController {
	private final TestResultDao testResultDao;

	TestResultController(TestResultDao testResultDao) {
		this.testResultDao = testResultDao;
	}
	
	@GetMapping("test/result")
	public List<TestResult> getAllTestResults() {
		List<TestResult> results = testResultDao.findAll();
		return results;
	}

	@GetMapping("test/{id}/result")
	public List<TestResult> getTestResultsByTest(@PathVariable int id) {
		List<TestResult> results = testResultDao.findByTest(id);
		return results;
	}

}
