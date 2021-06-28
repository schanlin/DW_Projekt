package backend.message;

import backend.subject.SubjectDao;
import backend.user.User;
import backend.user.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("/message")
    public Message postMessage(@RequestBody Message newMessage) {
        User sender = userDao.findById(newMessage.getSender());
        User recipient = userDao.findById(newMessage.getRecipient());

        if (newMessage.getRecipient()==newMessage.getSender()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot send messages to oneself.");
        }

        if (sender.getRolle().equals("Lernende")) {
            if (subjectDao.findIdByUserStudent(newMessage.getSender()).retainAll(subjectDao.findIdByUserStudent(newMessage.getRecipient()))) {
                return messageDao.insert(newMessage);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Students may only send messages to people from their active subjects.");
            }
        }

        if (sender.getRolle().equals("Lehrende")) {
            if (recipient.getRolle().equals("Lernende")
                    && !subjectDao.findIdByUserTeacher(sender.getUserID()).retainAll(subjectDao.findIdByUserStudent(recipient.getUserID()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teachers may not send messages to students from foreign subjects.");
            } else {
                return messageDao.insert(newMessage);
            }
        }
        return messageDao.insert(newMessage);
    }
}
