package backend;

import java.util.List;

public class Student {
	private final int userID;
	private String username;
	private String vorname;
	private String nachname;
	private String klasse;
	private List<TestResult> results;
	
	public Student(int userID, String username, String vorname, String nachname, String klasse) {
		this.userID = userID;
		this.username = username;
		this.vorname = vorname;
		this.nachname = nachname;
		this.klasse = klasse;
		this.results = null;
	}

	public String getUsername() {
		return username;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public String getKlasse() {
		return klasse;
	}

	public int getUserID() {
		return userID;
	}
	
	
}
