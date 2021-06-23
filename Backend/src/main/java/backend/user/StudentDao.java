package backend.user;

import backend.Database;
import backend.klasse.Klasse;
import backend.klasse.KlasseDao;
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

import backend.klasse.*;

@Service
public class StudentDao {
    private final JdbcTemplate template;
    private final TestResultDao testResultDao;

    public StudentDao(JdbcTemplate template, TestResultDao testResultDao) {
        this.template = template;
        this.testResultDao = testResultDao;
    }

    public Student insert(Student student) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO user (username, passwort," +
                    "vorname, nachname, rolle, klassenID) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getPassword());
            stmt.setString(3, student.getFirstname());
            stmt.setString(4, student.getLastname());
            stmt.setString(5, student.getRolle());
            stmt.setInt(6, student.getKlasse());
            return stmt;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(creator, holder);
        return new Student(holder.getKey().intValue(),
                student.getUsername(), student.getFirstname(), student.getLastname(), student.getRolle(), student.getKlasse());
    }

    public List<Student> findAll() {
        return template.query("SELECT userID, username, vorname, nachname, rolle, klassenID FROM user", (rs, rowNum) ->
            new Student(rs.getInt("userID"), rs.getString("username"), rs.getString("vorname"),
                    rs.getString("nachname"), rs.getString("rolle"), rs.getInt("klassenID")));
    }

    public Student findById(int id) {
        return template.queryForObject("SELECT userID, username, vorname, nachname, rolle, klassenID FROM user" +
                " WHERE userID=" + id, (rs, rowNum) ->
            new Student(rs.getInt("userID"), rs.getString("username"), rs.getString("vorname"),
                    rs.getString("nachname"), rs.getString("rolle"), rs.getInt("klassenID")));
    }

    public int deassign(int id) {
        return template.update("UPDATE user SET klassenID=null WHERE userID=" + id);
    }

    public int deassignAll(int klasse) {
        return template.update("UPDATE user SET klassenID=null WHERE klassenID=" + klasse);
    }

    public int delete(int id) {
        testResultDao.deleteByStudent(id);
        return template.update("DELETE FROM user WHERE userID=" + id);
    }
}
