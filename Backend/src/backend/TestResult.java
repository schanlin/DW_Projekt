package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class TestResult {
	private int testID;
	private int studentID;
	private int mark;
	
	public TestResult(int testID, int studentID, int mark) {
		this.testID = testID;
		this.studentID = studentID;
		this.mark = mark;
	}

	public int getTestID() {
		return testID;
	}

	public int getStudentID() {
		return studentID;
	}

	public int getMark() {
		return mark;
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
