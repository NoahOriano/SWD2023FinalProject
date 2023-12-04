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

    /** A class called connect that takes in a Socket, and output stream, and an input stream and uses them
     * to initialize the class variables of ClientMessageRelay().**/
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
        NetworkMessage networkMessage = null;// Creat a new variable called networkMessage that is initialized to null.
        do // process messages sent from server
        {
            try
            {// Attempt the following.
                networkMessage = (NetworkMessage) input.readObject(); // read new message
                System.out.println("Message received");// print a message stating the message has been received successfully.
                SceneController controller = master.getController(); //Sets controller to the same as SceneMaster
                // If the controller class is equal to the class that controls the main game scene, create a new instance of that class.
                if(controller.getClass() == SceneControllerForActionScene.class){
                    SceneControllerForActionScene control = (SceneControllerForActionScene)controller;
                    // If the system receives a message stating that the game is over, end the game.
                    if(networkMessage.identifier() == MessageValue.GAMEOVER){
                        control.setGameOver(true);
                        control.chatLog.appendText("Server>>> "+networkMessage.dataA());
                    }
                    // If the message sent to the system is evidence, add the evidence to master and determine if the user was a cultist or an innocent.
                    if(networkMessage.identifier() == MessageValue.EVIDENCE){
                        PlayerIdentifier identity = PlayerIdentifier.INNOCENT;
                        if(networkMessage.dataB().equals("Cultist"))identity = PlayerIdentifier.CULTIST;
                        master.addEvidence(networkMessage.dataA(), identity);
                        //@TODO logic for handling evidence and adding information to gamestate
                    }
                    // If the system receives a message stating that the round is over, change the number of rounds left accordingly and get the game state.
                    if(networkMessage.identifier() == MessageValue.ROUNDOVER){
                        control.roundsLeft.setText("Rounds Left: "+networkMessage.dataB());
                        GameState state = master.getGameState();
                        for(int i = 0; i < state.getPlayerFiles().size(); i++){
                            if(state.getPlayerFiles().get(i).getDefense().equals(networkMessage.dataA())){
                                // This is the file on the player who was voted out
                                state.getPlayerFiles().get(i).setLife(false);
                                if(networkMessage.dataA().equals(master.getGameState().getUsername())){
                                    control.statusField.setText("You are dead");// Set the status to dead for the user who died.
                                }
                            }
                        }
                    }
                    // Add identifiers to cultists and innocents.
                    if(networkMessage.identifier() == MessageValue.INNOCENT){
                        master.getGameState().setIdentifier(  PlayerIdentifier.INNOCENT);
                    }
                    if(networkMessage.identifier() == MessageValue.CULTIST){
                        master.getGameState().setIdentifier( PlayerIdentifier.CULTIST);
                    }
                    // Give the users new voting options if the system receives a message saying that is time to vote.
                    if(networkMessage.identifier() == MessageValue.VOTE){
                        control.actionOptions.getItems().clear();
                        control.actionOptions.getItems().add("Vote");
                        control.actionOptions.getItems().add("Skip");
                    }
                    // Create a new game and allow players to join and vote to start the game.
                    if(networkMessage.identifier() == MessageValue.JOIN){
                        master.setGameState(new GameState(control.getUsername()));
                        master.getGameState().addPlayerFile(control.getUsername());
                        control.actionOptions.getItems().clear();
                        control.actionOptions.getItems().add("Vote");
                    }// If the system receives a message stating that there is a new player, set the game state to null and add a new player file for the player.
                    if(networkMessage.identifier() == MessageValue.NEWPLAYER){
                        if(master.getGameState()!=null){
                            if(master.getGameState().addPlayerFile(networkMessage.dataA())){
                                control.playerOptions.getItems().add(networkMessage.dataA());
                            }
                        }
                    }
                    // If the system receives a message that a user has sent a chat, append the message to the chat log.
                    if(networkMessage.identifier() == MessageValue.CHAT){
                        control.chatLog.appendText("\n"+networkMessage.dataB()+">>> "+networkMessage.dataA());
                    }
                    // If the player chooses to investigate someone, set the players to either an imposter or a cultist.
                    if(networkMessage.identifier() == MessageValue.INVESTIGATE){
                        control.actionOptions.getItems().clear();
                        // Innocents' options.
                        if(master.getGameState().getIdentifier() == PlayerIdentifier.INNOCENT){
                            control.actionOptions.getItems().add("Steal");
                            control.actionOptions.getItems().add("Pass");
                            control.actionOptions.getItems().add("Investigate");
                        }else{// Cultist's options.
                            control.actionOptions.getItems().add("Steal");
                            control.actionOptions.getItems().add("Pass");
                            control.actionOptions.getItems().add("Investigate");
                            control.actionOptions.getItems().add("Forge");
                        }
                    }
                }
                else if(controller.getClass() == SceneControllerForGameJoin.class){
                    SceneControllerForGameJoin control = (SceneControllerForGameJoin)controller;
                }// If the user is in the sign in stage and has enetered a valid username, sign them in and display a message to the screen.
                else if(controller.getClass() == SceneControllerForSignOn.class){
                    SceneControllerForSignOn control = (SceneControllerForSignOn)controller;
                    if(networkMessage.identifier()== MessageValue.SIGNIN) {
                        System.out.println("Client relay received signin");
                        control.usernameInput.setText("Accepted, submit again");
                        control.usernameInput.setPrefColumnCount(2);
                        control.signedIn = true;// Yes, the player is signed in.
                    }
                }
            }
            // Throw an exception if there is an error.
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
