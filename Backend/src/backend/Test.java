package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Test {
	private int testID;
	private String testName;
	private Date testDatum;
	
	public Test(int testID, String testName, Date testDatum) {
		this.testID = testID;
		this.testName = testName;
		this.testDatum = testDatum;
	}
	
	public String getTestName() {
		return testName;
	}
	
	public Date getTestDatum() {
		return testDatum;
	}
	
	public int getTestID() {
		return testID;
	}
	
	public static List<Test> findAllTests(){
		List<Test> tests = new LinkedList<>();
		try(Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			String query = "SELECT * FROM test";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				tests.add(new Test(rs.getInt(1), rs.getString(2), rs.getDate(3)));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return tests;
	}
}
