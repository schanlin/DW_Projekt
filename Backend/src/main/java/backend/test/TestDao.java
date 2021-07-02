package backend.test;

import backend.Database;
import backend.test_result.TestResultDao;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Service
public class TestDao {
    private final JdbcTemplate template;
    private final TestResultDao testResultDao;

    public TestDao(JdbcTemplate template, TestResultDao testResultDao) {
        this.template = template;
        this.testResultDao = testResultDao;
    }

    public static void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS test("
                + "testID int AUTO_INCREMENT NOT NULL,"
                + "name varchar(256) NOT NULL,"
                + "datum date NOT NULL,"
                + "fachID int,"
                + "PRIMARY KEY(testID),"
                + "FOREIGN KEY(fachID) REFERENCES fach(fachID))";

        try (Connection con = DriverManager.getConnection(Database.url, Database.user, Database.password)) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        }
    }

    public Test insert(Test test) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO test (name, datum, fachID) " +
                    "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, test.getTestName());
            stmt.setDate(2, test.getTestDatum());
            stmt.setInt(3, test.getSubject());
            return stmt;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(creator, holder);
        return new Test(holder.getKey().intValue(), test.getTestName(), test.getTestDatum(), test.getSubject());
    }

    public List<Test> findAll() {
        return template.query("SELECT testID, name, datum, fachID FROM test", (rs, rowNum) ->
                new Test(rs.getInt("testID"), rs.getString("name"), rs.getDate("datum"),
                        rs.getInt("fachID")));
    }

    public Test findById(int id) {
        return template.queryForObject("SELECT testID, name, datum, fachID FROM test WHERE testID=" + id,
                (rs, rowNum) ->
                new Test(rs.getInt("testID"), rs.getString("name"), rs.getDate("datum"),
                        rs.getInt("fachID")));
    }

    public List<Test> findBySubject(int subjectId) {
        return template.query("SELECT testID, name, datum, fachID FROM test WHERE fachID=" + subjectId,
                (rs, rowNum) ->
                new Test(rs.getInt("testID"), rs.getString("name"), rs.getDate("datum"),
                        rs.getInt("fachID")));
    }

    public int countBySubject(int subjectId) {
        return template.queryForObject("SELECT count(*) FROM test WHERE fachID=" + subjectId, Integer.class);
    }

    public int update(Test toUpdate, int id) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("UPDATE test SET name = ?, datum = ?, fachID = ?" +
                    " WHERE testID = ?");
            stmt.setString(1, toUpdate.getTestName());
            stmt.setDate(2, toUpdate.getTestDatum());
            stmt.setInt(3, toUpdate.getSubject());
            stmt.setInt(4, id);
            return stmt;
        };
        return template.update(creator);
    }

    public int delete(int id) {
        testResultDao.deleteByTest(id);
        return template.update("DELETE FROM test WHERE testID=" + id);
    }

    public int deleteEmpty() {
        return template.update("DELETE FROM test" +
                " WHERE (SELECT archiviert FROM fach INNER JOIN test ON fach.fachID = test.fachID WHERE fach.fachID = test.fachID) = TRUE" +
                " AND (SELECT count(*) FROM ergebnis WHERE test.testID = ergebnis.testID) = 0");
    }
}
