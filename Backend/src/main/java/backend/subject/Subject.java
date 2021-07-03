package backend.subject;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Subject {
	private Integer subjectID;
	private String subjectName;
	private Integer klasse;
	private Integer teacher;
	private Boolean archived;
	
	@JsonGetter
	public String getSubjectName() {
		return subjectName;
	}

	@JsonGetter
	public int getKlasse() {
		return klasse;
	}

	@JsonGetter
	public int getTeacher() {
		return teacher;
	}

	@JsonGetter
	public int getSubjectID() {
		return subjectID;
	}
	
	@JsonGetter
	public boolean isArchived() {
		return archived;
	}

	public Subject(int subjectID, String subjectName, int klasse, int teacher) {
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.klasse = klasse;
		this.teacher = teacher;
		this.archived = false;
	}
	
	public Subject(int subjectID, String subjectName, int klasse, int teacher, boolean archived) {
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.klasse = klasse;
		this.teacher = teacher;
		this.archived = archived;
	}
	
	public Subject(String name, int klasse, int teacher, boolean archived) {
		this.subjectName = name;
		this.klasse = klasse;
		this.teacher = teacher;
		this.archived = archived;
	}

}
