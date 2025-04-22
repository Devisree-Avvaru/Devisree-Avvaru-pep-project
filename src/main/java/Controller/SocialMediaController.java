package Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import Model.Account;
import Model.Message;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    public Map<Integer, Message> messageTable = new HashMap<>();
    public Set<Integer> userIds = new HashSet<>(Arrays.asList(1, 2)); // Assuming test DB resets to users 1 and 2
    public int messageIdCounter = 1;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController(){
        this.accountService=new AccountService();
        this.messageService=new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.get("example-endpoint", this::exampleHandler);
        app.post("/messages", this::createMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.get("/accounts/{user_id}/messages", this::getMessageByUserHandler);
        app.get("/messages", getAllMessagesHandler);
        app.get("/messages/{message_id}", getMessageByIdHandler);
        app.patch("/messages/{message_id}", updateMessageHandler);
        app.post("/login", loginHandler);
        app.post("/register", registerHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    private void createMessageHandler(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);
            
            Message savedMessage = messageService.createMessage(message);
            if (savedMessage != null) {
                context.status(200).json(savedMessage);
            } else {
                context.status(400); // if insert failed
            }
    
        } catch (Exception e) {
            context.status(400);
        }
    }
    private void deleteMessageByIdHandler(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deleted = messageService.deleteMessageById(messageId);
    
        if (deleted != null) {
            context.status(200).json(deleted);
        } else {
            context.status(200); // No content, as expected by test
        }
    }
    public void getMessageByUserHandler(Context context){
        int userId=Integer.parseInt(context.pathParam("user_id"));
        List<Message> messages = messageService.getAllMessagesByUserId(userId);
        context.json(messages);
    }
    public Handler getAllMessagesHandler = ctx->{
        List<Message> messages = messageService.retrieveAllNMessages();
        ctx.json(messages);
    };
    public Handler getMessageByIdHandler = ctx->{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByMessageId(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.result(""); // empty body
        }

    };
    public Handler updateMessageHandler = ctx -> {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message updateRequest = ctx.bodyAsClass(Message.class);
    
        String newText = updateRequest.getMessage_text();
        Message updatedMessage = messageService.updateMessageText(messageId, newText);
    
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400).result(""); // Invalid request or message not found
        }
    };
    public Handler loginHandler = ctx -> {
        Account loginAttempt = ctx.bodyAsClass(Account.class);
        Account account = accountService.login(loginAttempt.getUsername(), loginAttempt.getPassword());

        if (account != null) {
            ctx.status(200);
            ctx.json(account);
        } else {
            ctx.status(401);
            ctx.result("");
        }
    };
    public Handler registerHandler=ctx->{
        Account newAccount = ctx.bodyAsClass(Account.class);
        Account registered = accountService.registerAccount(newAccount);
        if (registered != null) {
            ctx.status(200);
            ctx.json(registered);
        } else {
            ctx.status(400);
            ctx.result("");
        }  

    };

}