/**
 * MessageRequest is the class utilized for the Chat feature of the game extending ServerRequest to fit in the same arrayList
 * as other requests
 */
public class MessageRequest extends ServerRequest{
    /**
     * The message written by the player
     */
    private String message;
    /**
     * ServerClientHandler is utilized to give a source of the message but that constructor and variable is unused in the clas
     */
    private ServerClientHandler sender;

    /**
     * Simple Constructor for Message Request that simply stores the String data of the message being deliverd
     * @param message is the input of the message to be delivered across the NetWork
     */
    public MessageRequest(String message){
        this.message = message;
    }

    /**
     * Unused constructor that gives the String message along with information of the Sender
     * @param message is the input for the message to be delivered across the Network
     * @param sender is the input for the source data
     */
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
