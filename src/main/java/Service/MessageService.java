package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.*;
public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
        return messageDAO.insertMessage(message);
    }
    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }
    public List<Message> getAllMessagesByUserId(int userId){
        return messageDAO.getAllMessagesByUserId(userId);
    }
    public List<Message> retrieveAllNMessages(){
        return messageDAO.getAllMessages();
    }
    public Message getMessageByMessageId(int messageId){
        return messageDAO.getMessageByMessageId(messageId);
    }
    public Message updateMessageText(int messageId, String newText) {
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            return null;
        }
        return messageDAO.updateMessageText(messageId, newText);
    }
    
}
