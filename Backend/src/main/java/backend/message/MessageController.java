package backend.message;

import backend.subject.SubjectDao;
import backend.user.User;
import backend.user.UserDao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class MessageController {
    private final MessageDao messageDao;
    private final UserDao userDao;
    private final SubjectDao subjectDao;


    public MessageController(MessageDao messageDao, UserDao userDao, SubjectDao subjectDao) {
        this.messageDao = messageDao;
        this.userDao = userDao;
        this.subjectDao = subjectDao;
    }

    @Operation(summary = "Create a new message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message created successfully",
                content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed to send messages to this user"),
            @ApiResponse(responseCode = "404", description = "User not found")

    })
    @PostMapping("/message")
    @ResponseStatus(HttpStatus.CREATED)
    public Message postMessage(@RequestBody Message newMessage) {
        User sender = userDao.findById(newMessage.getSender());
        User recipient = userDao.findById(newMessage.getRecipient());

        if (sender==null || recipient==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (newMessage.getRecipient()==newMessage.getSender()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot send messages to oneself.");
        }

        if (sender.getRolle().equals("Lernende")) {
            List<Integer> recipientSubjects = null;

            if (recipient.getRolle().equals("Admin")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Students may only send messages to people from their active subjects.");
            } else if (recipient.getRolle().equals("Lernende")) {
                recipientSubjects = subjectDao.findIdByUserStudent(newMessage.getRecipient());
            } else if (recipient.getRolle().equals("Lehrende")) {
                recipientSubjects = subjectDao.findIdByUserTeacher(newMessage.getRecipient());
            }

            boolean commonSubjects = false;
            List<Integer> senderSubjects = subjectDao.findIdByUserStudent(newMessage.getSender());

            for (Integer val: senderSubjects) {
                if (recipientSubjects.contains(val)) {
                    commonSubjects = true;
                    break;
                }
            }

            if (commonSubjects) {
                return messageDao.insert(newMessage);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Students may only send messages to people from their active subjects.");
            }
        }

        if (sender.getRolle().equals("Lehrende")) {
            if (recipient.getRolle().equals("Lernende")) {
                List<Integer> senderSubjects = subjectDao.findIdByUserTeacher(newMessage.getSender());
                List<Integer> recipientSubjects = subjectDao.findIdByUserStudent(newMessage.getRecipient());
                boolean commonSubjects = false;

                for (Integer val: senderSubjects) {
                    if (recipientSubjects.contains(val)) {
                        commonSubjects = true;
                        break;
                    }
                }

                if (commonSubjects) {
                    return messageDao.insert(newMessage);
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Teachers may not send messages to students from foreign subjects.");
                }

            } else {
                return messageDao.insert(newMessage);
            }
        }
        return messageDao.insert(newMessage);
    }

    @Operation(summary = "Get all messages sent to someone by the recipient's userId")
    @ApiResponse(responseCode = "200", description = "Returned all messages found",
        content = {@Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Message.class)))})
    @GetMapping("/message/{userId}")
    public List<Message> getMessageByUser(@Parameter(description = "The userId of the recipient") @PathVariable int userId) {
        return messageDao.findByRecipient(userId);
    }

    @Operation(summary = "Get a message by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found",
                content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "404", description = "Message not found")
    })
    @GetMapping("/message/m/{messageId}")
    public Message getMessage(@Parameter(description = "The id of the message to be searched") @PathVariable int messageId) {
        Message m = messageDao.findById(messageId);
        if (m==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return m;
    }

    @Operation(summary = "Get the amount of unread messages by the recipient's userId")
    @ApiResponse(responseCode = "200", description = "Returned amount of unread messages",
        content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = Integer.class))})
    @GetMapping("/message/{userId}/unread")
    public int getAmountUnread(@Parameter(description = "The userId of the recipient") @PathVariable int userId) {
        return messageDao.countUnreadByUser(userId);
    }

    @Operation(summary = "Mark a message as read")
    @ApiResponse(responseCode = "204", description = "Changed message status to 'read'")
    @PutMapping("/message/m/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@Parameter(description = "The idea of the message to be updated") @PathVariable int messageId) {
        messageDao.updateRead(messageId);
    }
}
