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
                    if(networkMessage.identifier() == MessageValue.GAMEOVER){
                        //@TODO gameover logic nad setup for scene switching
                        //@TODO voting again should bring the player back to signin scene
                    }
                    if(networkMessage.identifier() == MessageValue.EVIDENCE){
                        //@TODO logic for handling evidence and adding information to gamestate
                    }
                    if(networkMessage.identifier() == MessageValue.ROUNDOVER){
                        //@TODO logic for roundover
                    }
                    if(networkMessage.identifier() == MessageValue.VOTE){
                        //@TODO logic for setting game state to voting
                    }
                    if(networkMessage.identifier() == MessageValue.CHAT){
                        //@TODO logic for adding chat to log
                    }
                    if(networkMessage.identifier() == MessageValue.INVESTIGATE){
                        //REuse of message type
                        //@TODO logic for setting game state to action state
                    }
                }
                else if(controller.getClass() == SceneControllerForGameJoin.class){
                    SceneControllerForGameJoin control = (SceneControllerForGameJoin)controller;
                }
                else if(controller.getClass() == SceneControllerForSignOn.class){
                    SceneControllerForSignOn control = (SceneControllerForSignOn)controller;
                    if(networkMessage.identifier()== MessageValue.SIGNIN) {
                        System.out.println("Client relay received signin");
                        control.usernameInput.setText("Accepted, submit again");
                        control.usernameInput.setPrefColumnCount(2);
                        control.signedIn = true;
                    }
                }
            }
            catch (ClassNotFoundException | IOException e)
            {
                e.printStackTrace();
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
