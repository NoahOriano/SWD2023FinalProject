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
                        control.setGameOver(true);
                        control.chatLog.appendText("Server>>> "+networkMessage.dataA());
                    }
                    if(networkMessage.identifier() == MessageValue.EVIDENCE){
                        PlayerIdentifier identity = PlayerIdentifier.INNOCENT;
                        if(networkMessage.dataB().equals("Cultist"))identity = PlayerIdentifier.CULTIST;
                        master.addEvidence(networkMessage.dataA(), identity);
                        //@TODO logic for handling evidence and adding information to gamestate
                    }
                    if(networkMessage.identifier() == MessageValue.ROUNDOVER){
                        control.roundsLeft.setText("Rounds Left: "+networkMessage.dataB());
                        GameState state = master.getGameState();
                        for(int i = 0; i < state.getPlayerFiles().size(); i++){
                            if(state.getPlayerFiles().get(i).getDefense().equals(networkMessage.dataA())){
                                // This is the file on the player who was voted out
                                state.getPlayerFiles().get(i).setLife(false);
                            }
                        }
                    }
                    if(networkMessage.identifier() == MessageValue.INNOCENT){
                        master.getGameState().setIdentifier(  PlayerIdentifier.INNOCENT);
                    }
                    if(networkMessage.identifier() == MessageValue.CULTIST){
                        master.getGameState().setIdentifier( PlayerIdentifier.CULTIST);
                    }
                    if(networkMessage.identifier() == MessageValue.VOTE){
                        control.actionOptions.getItems().clear();
                        control.actionOptions.getItems().add("Vote");
                    }
                    if(networkMessage.identifier() == MessageValue.JOIN){
                        master.setGameState(new GameState(control.getUsername()));
                        master.getGameState().addPlayerFile(control.getUsername());
                        control.actionOptions.getItems().clear();
                        control.actionOptions.getItems().add("Vote");
                    }
                    if(networkMessage.identifier() == MessageValue.NEWPLAYER){
                        if(master.getGameState()!=null){
                            if(master.getGameState().addPlayerFile(networkMessage.dataA())){
                                control.playerOptions.getItems().add(networkMessage.dataA());
                            }
                        }
                    }
                    if(networkMessage.identifier() == MessageValue.CHAT){
                        control.chatLog.appendText("\n"+networkMessage.dataB()+">>> "+networkMessage.dataA());
                    }
                    if(networkMessage.identifier() == MessageValue.INVESTIGATE){
                        control.actionOptions.getItems().clear();
                        if(master.getGameState().getIdentifier() == PlayerIdentifier.INNOCENT){
                            control.actionOptions.getItems().add("Steal");
                            control.actionOptions.getItems().add("Pass");
                            control.actionOptions.getItems().add("Investigate");
                        }else{
                            control.actionOptions.getItems().add("Steal");
                            control.actionOptions.getItems().add("Pass");
                            control.actionOptions.getItems().add("Investigate");
                            control.actionOptions.getItems().add("Forge");
                        }
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
