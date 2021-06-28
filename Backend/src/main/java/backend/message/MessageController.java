package backend.message;

import backend.subject.SubjectDao;
import backend.user.User;
import backend.user.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.CREATED)
    public Message postMessage(@RequestBody Message newMessage) {
        User sender = userDao.findById(newMessage.getSender());
        User recipient = userDao.findById(newMessage.getRecipient());

        if (sender==null || recipient==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (newMessage.getRecipient()==newMessage.getSender()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot send messages to oneself.");
        }

        if (sender.getRolle().equals("Lernende")) {
            List<Integer> senderSubjects = subjectDao.findIdByUserStudent(newMessage.getSender());
            List<Integer> recipientSubjects = null;

            if (recipient.getRolle().equals("Admin")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Students may only send messages to people from their active subjects.");
            } else if (recipient.getRolle().equals("Lernende")) {
                recipientSubjects = subjectDao.findIdByUserStudent(newMessage.getRecipient());
            } else if (recipient.getRolle().equals("Lehrende")) {
                recipientSubjects = subjectDao.findIdByUserTeacher(newMessage.getRecipient());
            }

            if (recipientSubjects==null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Students may only send messages to people from their active subjects.");
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
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teachers may not send messages to students from foreign subjects.");
                }

            } else {
                return messageDao.insert(newMessage);
            }
        }
        return messageDao.insert(newMessage);
    }
}
