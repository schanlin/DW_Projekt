package backend.message;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.sql.Date;

public class Message {
    private int messageID;
    private int sender;
    private int recipient;
    private Date timestamp;
    private String content;
    private boolean read;

    public Message(int messageID, int from, int to, Date timestamp, String content, boolean read) {
        this.messageID = messageID;
        this.sender = from;
        this.recipient = to;
        this.timestamp = timestamp;
        this.content = content;
        this.read = read;
    }

    public Message(int from, int to, Date timestamp, String content, boolean read) {
        this.sender = from;
        this.recipient = to;
        this.timestamp = timestamp;
        this.content = content;
        this.read = read;
    }

    @JsonCreator
    public Message(int from, int to, String content) {
        this.sender = from;
        this.recipient = to;
        this.timestamp = new Date(System.currentTimeMillis());
        this.content = content;
        this.read = false;
    }

    public int getSender() {
        return sender;
    }

    public int getRecipient() {
        return recipient;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return read;
    }
}
