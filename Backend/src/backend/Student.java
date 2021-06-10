package backend;

import java.util.List;

public class Student extends User{
	private String klasse;
	private List<TestResult> results;
	
	public Student(int userID, String username, String vorname, String nachname, String klasse) {
		super(userID, username, vorname, nachname);
		this.klasse = klasse;
		this.results = null;
	}
	public String getKlasse() {
		return klasse;
	}
	
	
}
