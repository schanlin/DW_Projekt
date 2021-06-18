package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Test {
	private int testID;
	private String testName;
	private Date testDatum;
	private Subject fach;
	private Klasse klasse;
	
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
	
	public static void createTable() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS test("
					 + "testID int AUTO_INCREMENT NOT NULL,"
					 + "name varchar(256) NOT NULL,"
					 + "datum date NOT NULL,"
					 + "fachID int,"
					 + "PRIMARY KEY(testID),"
					 + "FOREIGN KEY(fachID) REFERENCES fach(fachID))";
		
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)) {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			throw e;
		}		
	}
	
	public void insert() throws SQLException {
		String query = "INSERT INTO test (name, datum, fachID) VALUES (?, ?, ?)";
		
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, this.testName);
			stmt.setDate(2, this.testDatum);
			stmt.setInt(3, x);
		}
	}
	
	public static void insertData() throws SQLException {
		Test[] tests = new Test[6];
		tests[0] = new Test("Verben", new Date(1483225200000L));
		tests[1] = new Test("Nomen", new Date(1483225200000L));
		tests[2] = new Test("Gedichtinterpretation", new Date(1483225200000L));
		tests[3] = new Test("");
		tests[4] = new Test("");
		tests[5] = new Test("");
	}

	
	public static List<Test> findAllTests() throws SQLException {
		List<Test> tests = new LinkedList<>();
		
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)) {
			String query = "SELECT * FROM test";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				tests.add(new Test(rs.getInt(1), rs.getString(2), rs.getDate(3)));
			}
			return tests;
		} catch (SQLException e) {
			throw e;
		}		
	}

	public static Test findById(int id) throws SQLException {
		try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)) {
			String query = "SELECT * FROM test WHERE testID=" + id;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				return new Test(rs.getInt(1), rs.getString(2), rs.getDate(3));
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		} catch (SQLException e) {
			throw e;
		}		
	}
}
