package backend.user;

import backend.Database;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Service
public class TeacherDao {
    private final JdbcTemplate template;

    public TeacherDao(JdbcTemplate template) {
        this.template = template;
    }

    public List<Teacher> findAll() {
        return template.query("SELECT userID, username, vorname, nachname, rolle FROM user WHERE rolle='Lehrende'",
                (rs, rowNum) ->
                new Teacher(rs.getInt("userID"), rs.getString("username"), rs.getString("vorname"),
                        rs.getString("nachname"), rs.getString("rolle")));
    }

    public Teacher findById(int id) {
        return template.queryForObject("SELECT userID, username, vorname, nachname, rolle FROM user WHERE userID=" + id,
                (rs, rowNum) ->
                new Teacher(rs.getInt("userID"), rs.getString("username"), rs.getString("vorname"),
                        rs.getString("nachname"), rs.getString("rolle")));
    }

}
