/**
 * ActionRequest class is the class utilized to send requests to the servers to complete player actions
 */
public class ActionRequest extends ServerRequest{
    /**MessageValue requestType is the variable that dictates the action for the ActionRequest*/
    private MessageValue requestType;
    /**String data1 is used to send information for specific MessageValues typically the String identifier for the person
     * who the action is happening too*/
    private String data1;
    /**String data2 is used to send information for specific MessageValues*/
    private String data2;
    /**String requesterName is used to specifically send the String Identifier for a player requesting data*/
    private String requesterName;
    /**String sender is used to specifically send the String Identifier for a player sending data*/
    private ServerClientHandler sender;

    /**
     * ActionRequest Class Constructor
     * @param requestType is the input for what action is taking place
     * @param data1 is the input for the first String of information required for the requestType
     * @param data2 is the input for the second String of information required for the requestType
     * @param data3 is a variable unused for curernt iteration of code but allows for more complex actions in future
     *              development
     * @param requesterName is a variable to store the String identifier of the player requesting data
     * @param sender is a variable to store the String identifier of the player sending data
     */
    ActionRequest(MessageValue requestType, String data1, String data2, String data3, String requesterName, ServerClientHandler sender){
        this.requestType = requestType;
        this.data1 = data1;
        this.data2 = data2;
        this.requesterName = requesterName;
        this.sender = sender;
    }

    /**Getters and Setters for the Class Down Below*/
    public String getData1() {
        return data1;
    }
    public MessageValue getRequestType() {
        return requestType;
    }
    public String getData2() {
        return data2;
    }
    public String getRequesterName() {
        return requesterName;
    }
    public ServerClientHandler getSender() {
        return sender;
    }
}
