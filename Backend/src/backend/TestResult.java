package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

public class TestResult {
	private int testID;
	private int studentID;
	private int mark;
	
	public TestResult(int testID, int studentID, int mark) {
		this.testID = testID;
		this.studentID = studentID;
		this.mark = mark;
	}

	@JsonGetter
	public int getTestID() {
		return testID;
	}

	@JsonGetter
	public int getStudentID() {
		return studentID;
	}

	@JsonGetter
	public int getMark() {
		return mark;
	}
	
	public static boolean createTable() {
		String query = "CREATE TABLE ergebnis("
					 + "testID int NOT NULL,"
					 + "studentID int NOT NULL,"
					 + "note int NOT NULL,"
					 + "PRIMARY KEY(testID, studentID),"
					 + "FOREIGN KEY(testID) REFERENCES test(testID),"
					 + "FOREIGN KEY(studentID) REFERENCES user(userID))";
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	
	public static List<TestResult> findAllResults(){
		List<TestResult> testResults = new LinkedList<>();
		try(Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM ergebnis";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				testResults.add(new TestResult(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return testResults;
	}
}
