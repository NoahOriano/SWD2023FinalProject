import Values.MessageValue;

public class ActionRequest extends ServerRequest{
    private MessageValue requestType;
    private String data1;
    private String data2;
    private String requesterName;

    private ServerClientHandler sender;

    ActionRequest(MessageValue requestType, String data1, String data2, String requesterName, ServerClientHandler sender){
        this.requestType = requestType;
        this.data1 = data1;
        this.data2 = data2;
        this.requesterName = requesterName;
        this.sender = sender;
    }

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
