/**
 * MessageRequest is an unused class that was later replaced by ActionRequest
 */
public class MessageRequest extends ServerRequest{
    private String message;
    private ServerClientHandler sender;
    public MessageRequest(String message){
        this.message = message;
    }

    public MessageRequest(String message, ServerClientHandler sender){
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public ServerClientHandler getSender() {
        return sender;
    }
}
