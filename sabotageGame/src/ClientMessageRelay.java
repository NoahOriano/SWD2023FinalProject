import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Runnable for handling incoming information on connection
 */
public class ClientMessageRelay implements Runnable{
    /**SceneMaster which contains a SceneController which changes with scene changes*/
    SceneMaster master;
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

    public void connect(Socket connection, ObjectOutputStream output, ObjectInputStream input){
        this.connection = connection;
        this.output = output;
        this.input = input;
    }


    /**
     * Processes connection to client, handling incoming information
     */
    private void processConnection()
    {
        NetworkMessage networkMessage = null;
        do // process messages sent from server
        {
            try
            {
                networkMessage = (NetworkMessage) input.readObject(); // read new message
                System.out.println("Message received");
                SceneController controller = master.getController();
                if(controller.getClass() == SceneControllerForActionScene.class){
                    SceneControllerForActionScene control = (SceneControllerForActionScene)controller;
                }
                else if(controller.getClass() == SceneControllerForGameJoin.class){
                    SceneControllerForGameJoin control = (SceneControllerForGameJoin)controller;
                }
                else if(controller.getClass() == SceneControllerForSignOn.class){
                    SceneControllerForSignOn control = (SceneControllerForSignOn)controller;
                    if(networkMessage.identifier()==MessageValues.SIGNIN) {
                        System.out.println("Client relay received signin");
                        control.usernameInput.setText("Sign On Accepted, click submit again");
                        control.signedIn = true;
                    }
                }
            }
            catch (ClassNotFoundException | IOException e)
            {
                e.printStackTrace();
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
    public ClientMessageRelay(SceneMaster master){
        this.master = master;
    }
    @Override
    public void run() {
        while(true){
            try // connect to server, get streams, process connection
            {
                processConnection(); // process connection
            } finally
            {
                closeConnection(); // close connection
            }
        }
    }
}
