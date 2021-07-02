package backend.user;

import backend.subject.SubjectDao;
import backend.test.TestDao;
import backend.test_result.TestResultDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class StudentDao {
    private final JdbcTemplate template;
    private final TestResultDao testResultDao;
    private final TestDao testDao;
    private final SubjectDao subjectDao;
    private final PasswordEncoder encoder;

    public StudentDao(JdbcTemplate template, TestResultDao testResultDao, TestDao testDao, SubjectDao subjectDao, PasswordEncoder encoder) {
        this.template = template;
        this.testResultDao = testResultDao;
        this.testDao = testDao;
        this.subjectDao = subjectDao;
        this.encoder = encoder;
    }

    public Student insert(Student student) {
       PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO user (username, passwort, email, " +
                    "vorname, nachname, rolle, klassenID) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, student.getUsername());
            stmt.setString(2, encoder.encode(student.getPassword()));
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getFirstname());
            stmt.setString(5, student.getLastname());
            stmt.setString(6, student.getRolle());
            stmt.setInt(7, student.getKlasse());
            return stmt;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(creator, holder);
        return new Student(holder.getKey().intValue(),
                student.getUsername(), student.getEmail(), student.getFirstname(), student.getLastname(), student.getRolle(), student.getKlasse());
    }

    public List<Student> findAll() {
        return template.query("SELECT userID, username, email, vorname, nachname, rolle, klassenID FROM user", (rs, rowNum) ->
            new Student(rs.getInt("userID"), rs.getString("username"), rs.getString("email"),
                    rs.getString("vorname"), rs.getString("nachname"), rs.getString("rolle"),
                    rs.getInt("klassenID")));
    }

    public Student findById(int id) {
        return template.queryForObject("SELECT userID, username, vorname, nachname, rolle, klassenID FROM user" +
                " WHERE userID=" + id, (rs, rowNum) ->
            new Student(rs.getInt("userID"), rs.getString("username"), rs.getString("email"),
                    rs.getString("vorname"), rs.getString("nachname"), rs.getString("rolle"),
                    rs.getInt("klassenID")));
    }

    public int deassign(int id) {
        return template.update("UPDATE user SET klassenID=null WHERE userID=" + id);
    }

    public int assign(int klassenId, int studentId) {
        return template.update("UPDATE user SET klassenID=" + klassenId + " WHERE userID=" +studentId);
    }

    public int deassignAll(int klasse) {
        return template.update("UPDATE user SET klassenID=null WHERE klassenID=" + klasse);
    }

    public int delete(int id) {
        int status = testResultDao.deleteByStudent(id);
        if (status>0) {
            status = testDao.deleteEmpty();
        }
        if (status>0) {
            subjectDao.deleteEmpty();
        }
        return template.update("DELETE FROM user WHERE userID=" + id);
    }
}
