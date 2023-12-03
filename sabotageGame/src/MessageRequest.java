public class MessageRequest extends ServerRequest{
    private String message;
    private ServerClientHandler sender;
    public MessageRequest(String message){
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
