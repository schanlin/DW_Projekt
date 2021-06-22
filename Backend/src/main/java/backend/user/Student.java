package backend.user;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Student extends User {
	private Integer klasse;
	
	public Student(int userID, String username, String vorname, String nachname, String rolle, int klasse) {
		super(userID, username, vorname, nachname, rolle);
		this.klasse = klasse;
	}

	public Student(int userID, String username, String vorname, String nachname, String rolle) {
		super(userID, username, vorname, nachname, rolle);
	}
	
	public Student(String username, String passwort, String vorname, String nachname, String rolle, int klasse) {
		super(username, passwort, vorname, nachname, rolle);
		this.klasse = klasse;
	}

	public Student(String username, String passwort, String vorname, String nachname, String rolle) {
		super(username, passwort, vorname, nachname, rolle);
	}
	
	@JsonGetter
	public int getKlasse() {
		return klasse;
	}
	

	
}
