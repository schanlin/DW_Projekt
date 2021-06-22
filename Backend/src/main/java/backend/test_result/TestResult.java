package backend.test_result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import backend.Database;
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
