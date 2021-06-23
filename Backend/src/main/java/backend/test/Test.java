package backend.test;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import backend.Database;
import backend.klasse.Klasse;
import backend.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonGetter;

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
