package backend;

import java.sql.Date;
import java.util.List;

public class Test {
	private final int testID;
	private String testName;
	private Date testDatum;
	private List<TestResult> results;
	
	public Test(int testID, String testName, Date testDatum) {
		this.testID = testID;
		this.testName = testName;
		this.testDatum = testDatum;
	}
	
	public String getTestName() {
		return testName;
	}
	
	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	public Date getTestDatum() {
		return testDatum;
	}
	
	public void setTestDatum(Date testDatum) {
		this.testDatum = testDatum;
	}
	
	public int getTestID() {
		return testID;
	}
}
