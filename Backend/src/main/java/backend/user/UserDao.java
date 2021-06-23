package backend.user;

import backend.Database;
import backend.subject.Subject;
import backend.subject.SubjectDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class UserDao {
    private final JdbcTemplate template;
    private final SubjectDao subjectDao;

    public UserDao(JdbcTemplate template, SubjectDao subjectDao) {
        this.template = template;
        this.subjectDao = subjectDao;
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
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstname());
            stmt.setString(4, user.getLastname());
            stmt.setString(5, user.getRolle());
            return stmt;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(creator, holder);
        return new User(holder.getKey().intValue(), user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getRolle());
    }

    public List<User> findAll() {
        return template.query("SELECT userID, username, vorname, nachname, rolle FROM user", (rs, rowNum) ->
                new User(rs.getInt("userID"), rs.getString("username"), rs.getString("vorname"),
                        rs.getString("nachname"), rs.getString("Nachname"), rs.getString("Rolle")));
    }

    public User findById(int id) {
        return template.queryForObject("SELECT userID, username, vorname, nachname, rolle FROM user" +
                "WHERE userID=" + id, (rs, rowNum) ->
                new User(rs.getInt("userID"), rs.getString("username"), rs.getString("vorname"),
                        rs.getString("nachname"), rs.getString("rolle")));
    }



//    public static User findByName(String name) throws SQLException {
//        String[] fullname = name.split(" ");
//
//        try (Connection con = DriverManager.getConnection(Database.url, Database.user, Database.password)) {
//            String query;
//            if (fullname.length == 2) {
//                query = "SELECT userID, username, vorname, nachname, rolle FROM user WHERE vorname=" + fullname[0] + " AND nachname=" + fullname[1];
//            } else {
//                query = "SELECT userID FROM user WHERE vorname LIKE " + (fullname[0] + "%")
//                        + "AND nachname LIKE " + ("%" + fullname[fullname.length-1]);
//            }
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(query);
//            if (rs.next()) {
//                return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
//            } else {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//            }
//        } catch (SQLException e) {
//            throw e;
//        }
//    }

    public int delete(User user) throws SQLException {
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
