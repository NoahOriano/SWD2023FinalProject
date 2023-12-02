import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

// Will be converted to a private class in GameServer eventually
// Separate to allow easier editing
public class ServerClientHandler implements Runnable{
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
    private boolean hasConnection = true;
    /**
     * Whether the Client has performed their action or vote for this turn
     */
    private boolean clientHasActed;

    /**
     * Gets streams to send and receive data to and from connected client
     */

    private ArrayList<ServerPlayerRepresentation> players = new ArrayList<ServerPlayerRepresentation>();

    ServerClientHandler(Socket connection){
        this.connection = connection;
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
        this.hasConnection = true;
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
                if(networkMessage.identifier() == MessageValues.SEARCH){

                }
            }
            catch (ClassNotFoundException classNotFoundException)
            {

            }
        } while (networkMessage != null && networkMessage.identifier() != MessageValues.TERMINATE);
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
        }
        catch (IOException ioException)
        {
            //
        }
    }

    /**
     * Sends information to the client
     */
    private void sendInformation(NetworkMessage message){
        try {
            output.writeObject(message);
            output.flush(); // flush data to output
        }
        catch(IOException e){

        }
    }

}
