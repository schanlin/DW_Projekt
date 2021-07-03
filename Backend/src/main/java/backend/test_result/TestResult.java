package backend.test_result;

import com.fasterxml.jackson.annotation.JsonGetter;

public class TestResult {
	private Integer testID;
	private Integer studentID;
	private Integer mark;
	
	public TestResult(int testID, int studentID, int mark) {
		this.testID = testID;
		this.studentID = studentID;
		this.mark = mark;
	}

	@JsonGetter
	public int getTestID() {
		return testID;
	}

	@JsonGetter
	public int getStudentID() {
		return studentID;
	}

	@JsonGetter
	public int getMark() {
		return mark;
	}
	

}
