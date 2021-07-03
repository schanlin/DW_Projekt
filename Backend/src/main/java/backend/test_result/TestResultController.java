package backend.test_result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestResultController {
	private final TestResultDao testResultDao;

	TestResultController(TestResultDao testResultDao) {
		this.testResultDao = testResultDao;
	}

	@Operation(summary = "Get all test results")
	@ApiResponse(responseCode = "200", description = "Returned all test results",
		content = {@Content(mediaType = "application/json",
		array = @ArraySchema(schema = @Schema(implementation = TestResult.class)))})
	@GetMapping("test/result")
	public List<TestResult> getAllTestResults() {
		List<TestResult> results = testResultDao.findAll();
		return results;
	}

}
