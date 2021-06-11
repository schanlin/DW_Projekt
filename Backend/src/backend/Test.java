package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Test {
	private int testID;
	private String testName;
	private Date testDatum;
	
	public Test(int testID, String testName, Date testDatum) {
		this.testID = testID;
		this.testName = testName;
		this.testDatum = testDatum;
	}
	
	@JsonGetter
	public String getTestName() {
		return testName;
	}
	
	@JsonGetter
	public Date getTestDatum() {
		return testDatum;
	}
	
	@JsonGetter
	public int getTestID() {
		return testID;
	}
	
	public static boolean createTable() {
		String query = "CREATE TABLE test("
					 + "testID int AUTO_INCREMENT NOT NULL,"
					 + "name varchar(256) NOT NULL,"
					 + "datum date NOT NULL,"
					 + "PRIMARY KEY(testID))";
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
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
