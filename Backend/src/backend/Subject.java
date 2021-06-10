package backend;

import java.util.List;

public class Subject {
	private final int subjectID;
	private String subjectName;
	private Klasse klasse;
	private Teacher teacher;
	private List<Test> tests;
	
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Klasse getKlasse() {
		return klasse;
	}

	public void setKlasse(Klasse klasse) {
		this.klasse = klasse;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public List<Test> getTests() {
		return tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

	public int getSubjectID() {
		return subjectID;
	}

	public Subject(int subjectID, String subjectName, Klasse klasse, Teacher teacher, List<Test> tests) {
		super();
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.klasse = klasse;
		this.teacher = teacher;
		this.tests = tests;
	}
}
