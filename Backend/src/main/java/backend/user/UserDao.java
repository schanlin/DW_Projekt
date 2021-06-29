package backend.user;

import backend.Database;
import backend.subject.Subject;
import backend.subject.SubjectDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class UserDao {
    private final JdbcTemplate template;
    private final SubjectDao subjectDao;
    private final PasswordEncoder encoder;

    public UserDao(JdbcTemplate template, SubjectDao subjectDao, PasswordEncoder encoder) {
        this.template = template;
        this.subjectDao = subjectDao;
        this.encoder = encoder;
    }

    public static void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS user("
                + "userID int AUTO_INCREMENT NOT NULL,"
                + "username varchar(50) NOT NULL,"
                + "passwort varchar(256) NOT NULL,"
                + "vorname varchar(256) NOT NULL,"
                + "nachname varchar(256) NOT NULL,"
                + "rolle varchar(10) NOT NULL,"
                + "klassenID int,"
                + "PRIMARY KEY(userID),"
                + "UNIQUE(username),"
                + "FOREIGN KEY(klassenID) REFERENCES klasse(klassenID))";
        Connection con = DriverManager.getConnection(Database.url, Database.user, Database.password);
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
    }

   public User insert(User user) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO user (username, passwort, vorname, nachname, rolle)" +
                                                                "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, encoder.encode(user.getPassword()));
            stmt.setString(3, user.getFirstname());
            stmt.setString(4, user.getLastname());
            stmt.setString(5, user.getRolle());
            return stmt;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(creator, holder);
        return new User(holder.getKey().intValue(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getRolle());
    }

    public List<User> findAll() {
        return template.query("SELECT userID, username, vorname, nachname, rolle FROM user", (rs, rowNum) ->
                new User(rs.getInt("userID"), rs.getString("username"), rs.getString("vorname"),
                        rs.getString("nachname"), rs.getString("rolle")));
    }

    public User findById(int id) {
        return template.queryForObject("SELECT userID, username, vorname, nachname, rolle FROM user" +
                " WHERE userID=" + id, (rs, rowNum) ->
                new User(rs.getInt("userID"), rs.getString("username"), rs.getString("vorname"),
                        rs.getString("nachname"), rs.getString("rolle")));
    }

    public User findByUsername(String username) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("SELECT userID, username, vorname, nachname, rolle" +
                    " FROM user WHERE username=?");
            stmt.setString(1, username);
            return stmt;
        };
        return template.query(creator, (rs, rowNum) ->
                new User(rs.getInt("userID"), rs.getString("username"), rs.getString("vorname"),
                        rs.getString("nachname"), rs.getString("rolle"))).get(0);
    }

    public List<String> findAllUsernames() {
        return template.query("SELECT username FROM user", (rs, rowNum) ->
                rs.getString("username"));
    }

    public int updatePassword(String newPw, int id) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("UPDATE user SET passwort = ? WHERE userID = ?");
            stmt.setString(1, encoder.encode(newPw));
            stmt.setInt(2, id);
            return stmt;
        };
        return  template.update(creator);
    }

    public int update(User user, int id) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("UPDATE user SET username = ?, vorname = ?, " +
                    " nachname = ? WHERE userID = ?");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFirstname());
            stmt.setString(3, user.getLastname());
            stmt.setInt(4, id);
            return stmt;
        };
        return template.update(creator);
    }

    public int delete(User user) {
        if (user.getRolle().equals("Lernende")) {
            return delete(new Student(user.getUserID(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getRolle()));
        }
        if (user.getRolle().equals("Lehrende")) {
            List<Subject> subjects = subjectDao.findByTeacher(user.getUserID());
            if (!subjects.isEmpty()) {
                return -1;
            }
        }
        return template.update("DELETE FROM user WHERE userID=" + user.getUserID());
    }

}
