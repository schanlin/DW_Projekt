package backend.klasse;

import backend.Datenbank;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.List;

@Service
public class KlasseDao {
    private final JdbcTemplate template;
    KlasseDao(JdbcTemplate template) {
        this.template = template;
    }

    public static void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS klasse("
                + "klassenID int AUTO_INCREMENT NOT NULL,"
                + "name varchar(256) NOT NULL,"
                + "PRIMARY KEY(klassenID),"
                + "UNIQUE(name))";
        try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        }
    }

    public void insertData() throws SQLException {
        List<Klasse> current = findAll();
        if (current.isEmpty()) {
            String klasse1 = "INSERT INTO klasse (name) VALUES ('2016a')";
            String klasse2 = "INSERT INTO klasse (name) VALUES ('2016b')";

            try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
                Statement stmt = con.createStatement();
                stmt.executeUpdate(klasse1);
                stmt.executeUpdate(klasse2);
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    public List<Klasse> findAll() {
        return template.query("SELECT * FROM klasse", (rs, rowNum) ->
                        new Klasse(rs.getInt("klassenID"), rs.getString("name")));
    }

    public static Klasse findByStudent(int studentID) throws SQLException{
        try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
            String query = "SELECT * FROM klasse JOIN user ON user.klassenID = klasse.klassenID WHERE userID=" + studentID;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return new Klasse(rs.getInt(1), rs.getString(2));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static Klasse findKlasseById(int id) throws SQLException{
        try (Connection con = DriverManager.getConnection(Datenbank.url, Datenbank.user, Datenbank.password)){
            String query = "SELECT * FROM klasse WHERE klassenID=" + id;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return new Klasse(rs.getInt(1), rs.getString(2));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}
