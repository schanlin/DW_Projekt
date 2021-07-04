package backend.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.sql.Date;

public class Test {
	private int testID;
	private String testName;
	private Date testDatum;
	private Integer subject;
	
	public Test(int testID, String testName, Date testDatum, int subject) {
		this.testID = testID;
		this.testName = testName;
		this.testDatum = testDatum;
		this.subject = subject;
	}

	@JsonCreator
	public Test(String testName, Date testDatum, int subject) {
		this.testID = testID;
		this.testName = testName;
		this.testDatum = testDatum;
		this.subject = subject;
	}
	
	@JsonGetter
	public String getTestName() {
		return testName;
	}
	
	@JsonGetter
	public Date getTestDatum() {
		return testDatum;
	}
	
	@JsonGetter
	public int getTestID() {
		return testID;
	}

	public int getSubject() {
		return subject;
	}
	

}
