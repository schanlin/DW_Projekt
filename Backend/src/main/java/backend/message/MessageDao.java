package backend.message;

import backend.Database;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;

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
                " timestamp date NOT NULL," +
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
                    " timestamp, inhalt, gelesen) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, message.getSender());
            stmt.setInt(2, message.getRecipient());
            stmt.setDate(3, new Date(System.currentTimeMillis()));
            stmt.setString(4, message.getContent());
            stmt.setBoolean(5, message.isRead());
            return stmt;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(creator, holder);
        return new Message(holder.getKey().intValue(), message.getSender(), message.getRecipient(), message.getTimestamp(), message.getContent(), message.isRead());
    }
}
