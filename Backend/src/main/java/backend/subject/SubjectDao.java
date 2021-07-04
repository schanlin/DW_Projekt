package backend.subject;

import backend.Database;
import backend.test.TestDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class SubjectDao {
    private final JdbcTemplate template;
    private final TestDao testDao;

    public SubjectDao(JdbcTemplate template, TestDao testDao) {
        this.template = template;
        this.testDao = testDao;
    }

    public static void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS fach("
                + "fachID int AUTO_INCREMENT NOT NULL,"
                + "name varchar(256) NOT NULL,"
                + "klassenID int,"
                + "lehrID int,"
                + "archiviert bool NOT NULL,"
                + "PRIMARY KEY(fachID),"
                + "FOREIGN KEY(klassenID) REFERENCES klasse(klassenID),"
                + "FOREIGN KEY(lehrID) REFERENCES user(userID))";
        try (Connection con = DriverManager.getConnection(Database.url, Database.user, Database.password)){
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        }
    }

    public Subject insert(Subject subject) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO fach (name, klassenID, lehrID, archiviert) " +
                    "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, subject.getSubjectName());
            if (subject.getKlasse()==0){
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, subject.getKlasse());
            }
            if (subject.getTeacher()==0) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, subject.getTeacher());
            }
            stmt.setBoolean(4, subject.isArchived());
            return stmt;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(creator, holder);
        return new Subject(holder.getKey().intValue(), subject.getSubjectName(), subject.getKlasse(), subject.getTeacher(), subject.isArchived());
    }

    public List<Subject> findAll() {
        return template.query("SELECT fachID, name, klassenID, lehrID, archiviert FROM fach", (rs, rowNum) ->
                new Subject(rs.getInt("fachID"), rs.getString("name"), rs.getInt("klassenID"),
                        rs.getInt("lehrID"), rs.getBoolean("archiviert")));
    }

    public Subject findById(int id) {
        return template.queryForObject("SELECT fachID, name, klassenID, lehrID, archiviert FROM fach WHERE fachID=" +id,
                (rs, rowNum) ->
                new Subject(rs.getInt("fachID"), rs.getString("name"), rs.getInt("klassenID"),
                        rs.getInt("lehrID"), rs.getBoolean("archiviert")));
    }

    public List<Subject> findByTeacher(int teacherID) {
        return template.query("SELECT fachID, name, klassenID, lehrID, archiviert FROM fach WHERE lehrID=" + teacherID, (rs, rowNum) ->
                new Subject(rs.getInt("fachID"), rs.getString("name"), rs.getInt("klassenID"),
                        rs.getInt("lehrID"), rs.getBoolean("archiviert")));
    }

    public List<Subject> findByKlasse(int klassenID) {
        return template.query("SELECT fachID, name, klassenID, lehrID, archiviert FROM fach WHERE klassenID=" + klassenID, (rs, rowNum) ->
                new Subject(rs.getInt("fachID"), rs.getString("name"), rs.getInt("klassenID"),
                        rs.getInt("lehrID"), rs.getBoolean("archiviert")));
    }

    public List<Integer> findIdByUserStudent(int userId) {
        return template.query("SELECT fachID FROM fach INNER JOIN user WHERE fach.klassenID=user.klassenID " +
                "AND archiviert=FALSE AND userID=" + userId, (rs, rowNum) ->
                new Integer(rs.getInt(1)));
    }

    public List<Integer> findIdByUserTeacher(int userId) {
        return template.query("SELECT fachID FROM fach WHERE lehrID=" + userId, (rs, rowNum) ->
                rs.getInt(1));
    }

    public List<String> findAllSubjectnamesByClass(int klassenId) {
        return template.query("SELECT name FROM fach WHERE klassenID =" + klassenId, (rs, rowNum) ->
                rs.getString(1));
    }

    public int update(Subject toUpdate, int id) {
        Subject current = findById(id);
        int status;
        if (current.isArchived()) {
            return -1;
        }

        if (toUpdate.isArchived() && testDao.countBySubject(id)==0) {
            return -2;
        }


        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("UPDATE fach SET name = ?, klassenID = ?," +
                    " lehrID = ?, archiviert = ? WHERE fachID = ?");
            stmt.setString(1, toUpdate.getSubjectName());

            if (toUpdate.isArchived()) {
                stmt.setNull(2, Types.INTEGER);
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(2, toUpdate.getKlasse());
                stmt.setInt(3, toUpdate.getTeacher());
            }

            stmt.setBoolean(4, toUpdate.isArchived());
            stmt.setInt(5, id);
            return stmt;
        };
        status = template.update(creator);

        if (findById(toUpdate.getSubjectID()).isArchived()) {
            status = -3;
        }

        return status;
    }

    public int delete(int id) {
        if (testDao.countBySubject(id)>0) {
            return -1;
        } else {
            return template.update("DELETE FROM fach WHERE fachID=" + id);
        }
    }

    public int archive(int id) {
        if (testDao.countBySubject(id)==0) {
            return -1;
        }
        return template.update("UPDATE fach SET lehrID = null, klassenID = null, archiviert = TRUE WHERE fachID=" + id);
    }

    public int deleteEmpty() {
        return template.update("DELETE FROM fach WHERE archiviert=TRUE AND" +
                " (SELECT count(*) FROM test WHERE test.fachID = fach.fachID) = 0");
    }

}
