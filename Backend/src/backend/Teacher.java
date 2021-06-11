package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Teacher extends User{
	
	public Teacher(int userID, String username, String vorname, String nachname) {
		super(userID, username, vorname, nachname);
	}
	
	public List<Subject> getSubjects() {
		List<Subject> subjects = Subject.findSubjectsByTeacher(this.getUserID());
		return subjects;
	}
	
	public static List<Teacher> findAllTeachers(){
		List<Teacher> teachers = new LinkedList<>();
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT userID, username, vorname, nachname FROM users WHERE rolle='Lehrende'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				teachers.add(new Teacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return teachers;
	}

}
