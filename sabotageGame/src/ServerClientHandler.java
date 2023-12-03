import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

// Will be converted to a private class in GameServer eventually
// Separate to allow easier editing
public class ServerClientHandler implements Runnable{
    /**
     * Queue of requests sent to server to handle
     */
    private final ArrayBlockingQueue<ServerRequest> requests;
    /**
     * Connection to the client
     */
    private Socket connection;
    /**
     * Output Stream on connection
     */
    private ObjectOutputStream output;
    /**
     * Input Stream on connection
     */
    private ObjectInputStream input;

    /**
     * Whether the ServerClientHandler has a connection still
     */
    private boolean hasConnection;
    /**
     * Whether the Client has performed their action or vote for this turn
     */
    private boolean clientHasActed;

    /**
     * Gets streams to send and receive data to and from connected client
     */

    private ArrayList<ServerPlayerRepresentation> players = new ArrayList<ServerPlayerRepresentation>();

    /**
     * Username of connected client
     */
    private String username;
    
    public String getUsername(){
        return username;
    }
    
    public void setUsername(String username){
        this.username = username;
    }

    ServerClientHandler(Socket connection, ArrayBlockingQueue<ServerRequest> requests){
        this.connection = connection;
        this.requests = requests;
    }
    private void getStreams() throws IOException
    {
        // set up output stream for NetworkMessages
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        // set up input stream for NetworkMessages
        input = new ObjectInputStream(connection.getInputStream());
    }

    /**
     * Uses connection to get the IO streams, and processes the connection to the client until terminated
     */
    @Override
    public void run()
    {
        username = null;
        clientHasActed = false;
        hasConnection = true;
        try // connect to server, get streams, process connection
        {
            getStreams(); // get the input and output streams
            processConnection(); // process connection
        }
        catch (EOFException eofException)
        {

        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            closeConnection(); // close connection
            hasConnection = false;
        }
    }

    /**
     * Processes connection to client, handling incoming information
     */
    private void processConnection() throws IOException
    {
        NetworkMessage networkMessage = null;
        do // process messages sent from server
        {
            try
            {
                networkMessage = (NetworkMessage) input.readObject(); // read new message
                if(networkMessage.identifier() == MessageValue.MESSAGE){
                    sendRequestToServer(new MessageRequest(networkMessage.dataA()));
                }
                else if(networkMessage.identifier() == MessageValue.SIGNIN){
                    sendRequestToServer(new ActionRequest(MessageValue.SIGNIN, networkMessage.dataA(), null, null, username, this));
                }
                else if(networkMessage.identifier() == MessageValue.VOTE){
                    sendRequestToServer(new ActionRequest(MessageValue.VOTE, networkMessage.dataA(), null, null, username, this));
                }
                else if(networkMessage.identifier() == MessageValue.STEAL){
                    sendRequestToServer(new ActionRequest(MessageValue.STEAL, networkMessage.dataA(), null, null, username, this));
                }
                else if(networkMessage.identifier() == MessageValue.FORGE){
                    sendRequestToServer(new ActionRequest(MessageValue.FORGE, networkMessage.dataA(), networkMessage.dataB(), null, username, this));
                }
                else if(networkMessage.identifier() == MessageValue.PASS){
                    sendRequestToServer(new ActionRequest(MessageValue.PASS, networkMessage.dataA(), networkMessage.dataB(), null, username, this));
                }
                else if(networkMessage.identifier() == MessageValue.INVESTIGATE){
                    sendRequestToServer(new ActionRequest(MessageValue.INVESTIGATE, networkMessage.dataA(), networkMessage.dataB(), null, username, this));
                }
                else if(networkMessage.identifier() == MessageValue.CHAT){
                    sendRequestToServer(new ActionRequest(MessageValue.CHAT, networkMessage.dataA(), networkMessage.dataB(), null, username, this));
                }
            }
            catch (ClassNotFoundException classNotFoundException)
            {

            }
        } while (networkMessage != null && networkMessage.identifier() != MessageValue.TERMINATE);
    }

    /**
     * Closes IO streams and socket connection for use at program termination
     */
    private void closeConnection()
    {
        try
        {
            output.close(); // close output stream
            input.close(); // close input stream
            connection.close(); // close socket
            hasConnection = false;
        }
        catch (IOException ioException)
        {
            //
        }
    }
    public boolean isConnected(){
        return hasConnection;
    }

    public boolean hasActed(){
        return clientHasActed;
    }

    private void sendRequestToServer(ServerRequest request){
        requests.add(request);
    }

    /**
     * Sends information to the client
     */
    public synchronized void sendInformation(NetworkMessage message){
        try {
            output.writeObject(message);
            output.flush(); // flush data to output
        }
        catch(IOException e){

        }
    }

}
