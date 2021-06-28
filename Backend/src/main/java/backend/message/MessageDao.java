package backend.message;

import backend.Database;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class MessageDao {
    private final JdbcTemplate template;

    public MessageDao(JdbcTemplate template) {
        this.template = template;
    }

    public static void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS nachricht(" +
                " nachrichtID int AUTO_INCREMENT NOT NULL," +
                " absender int NOT NULL," +
                " empfaenger int NOT NULL," +
                " gesendet date NOT NULL," +
                " inhalt text," +
                " gelesen bool," +
                " PRIMARY KEY(nachrichtID)," +
                " FOREIGN KEY(absender) REFERENCES user(userID)," +
                " FOREIGN KEY(empfaenger) REFERENCES user(userID))";

        try (Connection con = DriverManager.getConnection(Database.url, Database.user, Database.password)) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        }
    }

    public Message insert(Message message) {
        PreparedStatementCreator creator = (connection) -> {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO nachricht (absender, empfaenger," +
                    " gesendet, inhalt, gelesen) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, message.getSender());
            stmt.setInt(2, message.getRecipient());
            stmt.setDate(3, message.getTimestamp());
            stmt.setString(4, message.getContent());
            stmt.setBoolean(5, message.isRead());
            return stmt;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(creator, holder);
        return new Message(holder.getKey().intValue(), message.getSender(), message.getRecipient(), message.getTimestamp(), message.getContent(), message.isRead());
    }

    public Message findById(int messageId) {
        return template.queryForObject("SELECT nachrichtID, absender, empfaenger, gesendet, inhalt, gelesen FROM nachricht WHERE nachrichtID=" + messageId,
                (rs, rowNum) ->
                        new Message(rs.getInt("nachrichtID"), rs.getInt("absender"), rs.getInt("empfaenger"),
                                rs.getDate("gesendet"), rs.getString("inhalt"), rs.getBoolean("gelesen")));
    }

    public List<Message> findByRecipient(int userId) {
        return template.query("SELECT nachrichtID, absender, empfaenger, gesendet, inhalt, gelesen FROM nachricht WHERE empfaenger=" + userId,
                (rs, rowNum) ->
                new Message(rs.getInt("nachrichtID"), rs.getInt("absender"), rs.getInt("empfaenger"),
                        rs.getDate("gesendet"), rs.getString("inhalt"), rs.getBoolean("gelesen")));
    }

    public int countUnreadByUser(int userId) {
        return template.queryForObject("SELECT count(*) FROM nachricht WHERE empfaenger=" + userId + " AND gelesen=FALSE", Integer.class);
    }

    public int updateRead(int messageId) {
        return template.update("UPDATE nachricht SET gelesen=TRUE WHERE nachrichtID=" + messageId);
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void deleteMessages() {
        int status = template.update("DELETE FROM nachricht WHERE DATEDIFF(CURDATE(),  gesendet)>7");
        System.out.println("Deleted " + status + " Messages.");
    }
}
