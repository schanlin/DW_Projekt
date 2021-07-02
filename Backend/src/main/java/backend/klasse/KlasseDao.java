package backend.klasse;

import backend.Database;
import backend.subject.*;
import backend.subject.SubjectDao;
import backend.user.Student;
import backend.user.StudentDao;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.List;

@Service
public class KlasseDao {
    private final JdbcTemplate template;
    private final StudentDao studentDao;
    private final SubjectDao subjectDao;

    public KlasseDao(JdbcTemplate template, StudentDao studentDao, SubjectDao subjectDao) {
        this.template = template;
        this.studentDao = studentDao;
        this.subjectDao = subjectDao;
    }

    public static void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS klasse("
                + "klassenID int AUTO_INCREMENT NOT NULL,"
                + "name varchar(256) NOT NULL,"
                + "PRIMARY KEY(klassenID),"
                + "UNIQUE(name))";
        try (Connection con = DriverManager.getConnection(Database.url, Database.user, Database.password)){
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        }
    }
    
    public Klasse insert(Klasse klasse) {
    	PreparedStatementCreator creator = (connection) -> {
    		PreparedStatement stmnt = connection.prepareStatement("INSERT INTO klasse (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
    		stmnt.setString(1, klasse.getKlassenName());
    		return stmnt;
    	};
    	
    	KeyHolder holder = new GeneratedKeyHolder();
    	template.update(creator, holder);
    	return new Klasse(holder.getKey().intValue(), klasse.getKlassenName());
    	
    }

    public List<Klasse> findAll() {
        return template.query("SELECT * FROM klasse", (rs, rowNum) ->
                        new Klasse(rs.getInt("klassenID"), rs.getString("name")));
    }

    public Klasse findByStudent(Student student) {
        return template.queryForObject("SELECT klassenID, name FROM klasse JOIN user ON user.klassenID = klasse.klassenID " +
                           "WHERE userID = " + student.getUserID(), (rs, rowNum) ->
                 new Klasse(rs.getInt("klassenID"), rs.getString("name")));
    }

    public Klasse findById(int id) {
    	return template.queryForObject("SELECT klassenID, name FROM klasse WHERE klassenID=" + id, (rs, rowNum) ->
    							new Klasse(rs.getInt("klassenID"), rs.getString("name")));
    }

	public int count() {
		return template.queryForObject("SELECT count(*) FROM klasse", Integer.class);
	}

    public List<String> findAllClassnames() {
        return template.query("SELECT name FROM klasse", (rs, rowNum) ->
                rs.getString("name"));
    }

    public int update(Klasse toUpdate, int id) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("UPDATE klasse SET name = ? WHERE klassenID = ?");
            stmt.setString(1, toUpdate.getKlassenName());
            stmt.setInt(2, id);
            return stmt;
        };
        return template.update(creator);
    }

	public int delete(int id) {
        studentDao.deassignAll(id);
        List<Subject> toDelete = subjectDao.findByKlasse(id);
        for (Subject subject: toDelete){
           if (subjectDao.delete(subject.getSubjectID())==-1){
               subjectDao.archive(subject.getSubjectID());
           }
        }
        return template.update("DELETE FROM klasse WHERE klassenID=" + id);
    }

}
