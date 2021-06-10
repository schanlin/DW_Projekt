package backend;

import java.util.List;

public class Klasse {
	private final int klassenID;
	private String klassenName;
	private List<Student> students;
	
	public Klasse(int klassenID, String klassenName, List<Student> students) {
		this.klassenID = klassenID;
		this.klassenName = klassenName;
		this.students = students;
	}

	public String getKlassenName() {
		return klassenName;
	}

	public void setKlassenName(String klassenName) {
		this.klassenName = klassenName;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public int getKlassenID() {
		return klassenID;
	}
	
}
