package backend.test_result;

import backend.Database;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Service
public class TestResultDao {
    private final JdbcTemplate template;

    public TestResultDao(JdbcTemplate template) {
        this.template = template;
    }

    public static void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS ergebnis("
                + "testID int NOT NULL,"
                + "lernID int NOT NULL,"
                + "note int NOT NULL,"
                + "PRIMARY KEY(testID, lernID),"
                + "FOREIGN KEY(testID) REFERENCES test(testID),"
                + "FOREIGN KEY(lernID) REFERENCES user(userID))";

        try (Connection con = DriverManager.getConnection(Database.url, Database.user, Database.password)) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        }
    }

    public TestResult insert(TestResult result) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO ergebnis VALUES(?,?,?)");
            stmt.setInt(1, result.getTestID());
            stmt.setInt(2, result.getStudentID());
            stmt.setInt(3, result.getMark());
            return stmt;
        };

        template.update(creator);
        return result;
    }

    public List<TestResult> findAll() {
        return template.query("SELECT testID, lernID, note FROM ergebnis", (rs, rowNum) ->
                new TestResult(rs.getInt("testID"), rs.getInt("lernID"), rs.getInt("note")));
    }

    public List<TestResult> findByStudent(int studentId) {
        return template.query("SELECT testID, lernID, note FROM ergebnis WHERE lernID=" + studentId, (rs, rowNum) ->
                new TestResult(rs.getInt("testID"), rs.getInt("lernID"), rs.getInt("note")));
    }

    public List<TestResult> findByTest(int testId) {
        return template.query("SELECT testID, lernID, note FROM ergebnis WHERE testID=" + testId, (rs, rowNum) ->
                new TestResult(rs.getInt("testID"), rs.getInt("lernID"), rs.getInt("note")));
    }

    public int deleteByStudent(int studentId) {
        return template.update("DELETE FROM ergebnis WHERE lernID=" + studentId);
    }

}
