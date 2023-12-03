import javax.swing.*;
import java.awt.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class GameServer extends JFrame {
    /**
     * Maximum number of players which a game can run at once
     */
    public static final int MAXPLAYERCOUNT = 10;
    /**
     * List of sub servers, generated to handle connections
     */
    private ArrayList<SubServer> subServers = new ArrayList<SubServer>();
    /**
     * Connection service which allows connection to clients to be retrieved
     */
    private ServerClientConnectionService connectionService;
    /**
     * Text area used to display information to used
     */
    private final JTextArea displayArea;
    /**
     * Executor service used to handle threads
     */
    private ExecutorService service;
    /**
     * Connections to give to client handlers
     */
    private ArrayBlockingQueue<ServerRequest> requests;
    /**
     * Counter of number of connections
     */
    private int playerCounter;

    /**
     * ServerClockSignaler used to periodically signal to the server as a way of keeping time in an event based way
     */
    private ServerClockSignaler roundTimer;

    /**
     * List of names of players currently connected to the lobby or game
     */
    private ArrayList<String> names;

    /**
     * Sub server class, handles communications between each individual client
     */
    private class SubServer {
        /**
         * Handler responsible for incoming communications from client and holding Socket and IO
         */
        ServerClientHandler handler; // ServerClientHandler connected to client
        /**
         * Username of connected client
         */
        String username; // Username of connected player, null identifies a player not signed in

        GameState gameState;

        public SubServer(ServerClientHandler handler) {
            this.handler = handler;
        }
        public void init(String name){
            this.username = name;
            handler.setUsername(name);
            this.gameState = new GameState(name);
        }
    }

    /**
     * Port server is hosted on
     */
    private int port;
    /**
     * Size of server backlog
     */
    private int backlog;

    private ServerGameController controller;

    /**
     * Number of timer responses since last round progression
     */
    private int timerCounter;

    /**
     * Delay of the time response in seconds
     */
    private int timerDelay;

    /**
     * Whether the game is in voting phase or not
     */
    private boolean isVoting;

    /**
     * Whether the game is currently playing, and other players can no longer join
     */
    private boolean isGameActive;

    /**
     * Total time per round in seconds
     */
    private int roundTime;

    /**
     * Number of rounds remaining
     */
    private int roundCounter;

    /**
     * Default number of rounds in total
     */
    public static final int DEFAULTROUNDS = 10;


    /**
     * Whether the game is currently active, otherwise, it is in lobby
     */
    public boolean gameActive;
    /**
     * Constructor, which sets up a GUI
     */
    public GameServer(int port, int backlog) {
        super("Server");
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(300, 150); // set size of window
        setVisible(true); // show window
        this.port = port;
        this.backlog = backlog;
    }

    /**
     * Constructor, which sets up a GUI
     */
    public GameServer() {
        this(23791, 100);
    }

    /**
     * Set up and run server
     */
    public void runServer() {
        playerCounter = 0;
        gameActive = false;
        controller = new ServerGameController();
        roundCounter = DEFAULTROUNDS;
        names = new ArrayList<>();
        service = Executors.newFixedThreadPool(17);
        requests = new ArrayBlockingQueue<ServerRequest>(100);
        connectionService = new ServerClientConnectionService(port, backlog, requests);
        service.execute(connectionService);
        establishTimer();
        try {
            displayMessage("IP:" + Inet4Address.getLocalHost().getHostAddress() + ", PORT: " + port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                ServerRequest request = requests.poll(1000, TimeUnit.MILLISECONDS);
                if (request != null) {
                    if (request.getClass() == ActionRequest.class) {
                        handleActionRequest(((ActionRequest) request));
                    }
                    if (request.getClass() == ConnectionRequest.class) {
                        sendConnectionToClientHandler(((ConnectionRequest) request).getConnection());
                    }
                    if (request.getClass() == MessageRequest.class) {
                        displayMessage(((MessageRequest) request).getMessage());
                    }
                    // Needs to check game state and have extra logic here for round progression
                    // Also check timer to ensure the round has time remaining, if not, automatically progress it
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sends a connection to a sub-server
     * @param connection connection to send to the client handler
     */
    public void sendConnectionToClientHandler(Socket connection) {
        SubServer newServer = new SubServer(new ServerClientHandler(connection, this.requests));
        subServers.add(newServer);
        service.execute(newServer.handler);
    }

    /**
     * Generates a clock used to control game flow
     */
    public void establishTimer() {
        timerDelay = 10;// Ten-second delay
        roundTime = 100; // Ten counter per round, 100 second rounds
        roundTimer = new ServerClockSignaler(timerDelay, requests);
        service.execute(roundTimer);
    }

    /**
     * Handles the given action request, if the request is not valid, it will be ignored, for example, a player
     * Tries acting twice. Also sends data back to the client on action completion when necessary
     * @param request Action request to handle all the data fom
     */
    public void handleActionRequest(ActionRequest request) {
        if (!gameActive) { // If the game is not active
            if (request.getRequestType() == MessageValue.SIGNIN) {
                if (usernameIsAvailable(request.getData1())) {
                    for (int i = 0; i < subServers.size(); i++) {
                        if (subServers.get(i).handler == request.getSender()) {
                            playerCounter++;
                            subServers.get(i).init(request.getData1());
                            System.out.println(subServers.get(i).username);
                            displayMessage("User Connected:"+request.getData1());
                        }
                    }
                    request.getSender().sendInformation(new NetworkMessage(MessageValue.SIGNIN, null, null, null));
                }
            }
            if(request.getRequestType() == MessageValue.VOTE){
                for(int i = 0; i < subServers.size(); i++){
                    if(subServers.get(i).handler == request.getSender()){
                        // @TODO Handle game lobby start vote
                    }
                }
            }
        } else { // If the game is active
            if (request.getRequestType() == MessageValue.TIMER) {
                timerCounter++;
                if (timerCounter > roundTime / timerDelay) {
                    if (roundCounter <= 0) {
                        endGame();
                    } else if (roundTime / timerDelay - roundCounter == 3) {
                        roundCounter--;
                        sendTimeWarning();
                    } else {
                        roundCounter--;
                        endRound();
                    }
                }
            }
            else if (request.getRequestType() == MessageValue.VOTE){
                if(isVoting){
                    // @TODO handle incoming votes
                }
            }
            else if (request.getRequestType() == MessageValue.STEAL){
                if(!isVoting){
                    // @TODO handle incoming steal request
                }
            }
            else if (request.getRequestType() == MessageValue.PASS){
                if(!isVoting){
                    // @TODO handle incoming pass request
                }
            }
            else if (request.getRequestType() == MessageValue.INVESTIGATE){
                if(!isVoting){
                    // @TODO handle incoming investigate request
                }
            }
            else if (request.getRequestType()== MessageValue.FORGE){
                // @TODO handle incoming forge request
            }
            else if (request.getRequestType() == MessageValue.CHAT){
                // @TODO handle incoming chat request
            }
        }
    }

    /**
     * Ends the round, indicating the round is over to all clients, and resets the game state for a new game
     * Removes all players as active players
     * Client side should return to signin page with username field prefilled with old username
     */
    private void endGame() {
        displayMessage("Game Over, starting new game");
        names = new ArrayList<>();
        controller = new ServerGameController();
        // Clear disconnected players to prevent endless server generation
        for (int i = 0; i < subServers.size(); i++) {
            if(subServers.get(i).handler.isConnected()){
                // @TODO add signal to client to go back to game signin
            }
            else{
                subServers.remove(i);
                i--; // Shift index again since subServer indexes have shifted
            }
        }
    }

    private void startGame(){
        gameActive = true;
        // @TODO send game start message
        // @TODO game start logic
    }

    /**
     * Ends the game, indicating the round is over to all clients, and resets the game state for a new game
     * Removes all players as active players
     * Client side should return to signin page with username field prefilled with old username
     */
    private void endRound() {
        displayMessage("Ending Round");
        roundCounter--;
        sendMessageToAll(new NetworkMessage(MessageValue.ROUNDOVER, null, null, null));
    }

    /**
     * Send a warning that the round is almost up to all players
     */
    private void sendTimeWarning() {
        displayMessage("Time Warning Sent");
        sendMessageToAll(new NetworkMessage(MessageValue.CHAT, "ROUND ALMOST OVER", "Server", null));
    }

    private void sendMessageToAll(NetworkMessage message) {
        for (int i = 0; i < subServers.size(); i++) {
            subServers.get(i).handler.sendInformation(message);
        }
    }

    /**
     * Determines whether the given username is available
     *
     * @param username username to test
     * @return true if the username is available, false otherwise
     */
    private boolean usernameIsAvailable(String username) {
        if (username == null) return false;
        for (int i = 0; i < subServers.size(); i++) {
            if (subServers.get(i).username != null) {
                if (username.equals(subServers.get(i).username)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Displays a message, adding it to the log
     *
     * @param messageToDisplay the message to display
     */
    private void displayMessage(final String messageToDisplay) {
        displayArea.append("\n" + messageToDisplay);
    }

}
