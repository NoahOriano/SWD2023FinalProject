public class MessageRequest extends ServerRequest{
    private String message;
    public MessageRequest(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
