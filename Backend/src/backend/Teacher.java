package backend;

import java.util.List;

public class Teacher extends User{
	private final int userID;
	private String username;
	private String vorname;
	private String nachname;
	private List<Subject> subjects;
	
	public Teacher(int userID, String username, String vorname, String nachname, List<Subject> subjects) {
		this.userID = userID;
		this.username = username;
		this.vorname = vorname;
		this.nachname = nachname;
		this.subjects = subjects;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public int getUserID() {
		return userID;
	}

}
