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
    private ArrayList<SubServer> subServers;
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
        /**
         * Whether the user has acted this round
         */
        boolean hasActed;

        private boolean isInGame;

        boolean isAlive;

        GameState gameState;

        int votesAgainst;

        public SubServer(ServerClientHandler handler) {
            this.handler = handler;
            this.isAlive = false;
            this.isInGame = false;
            this.hasActed = false;
            this.votesAgainst = 0;
        }

        public void init(String name) {
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
    public static final int DEFAULTROUNDS = 2;
    /**
     * Number of votes for skip or for starting lobby
     */
    private int votes;

    /**
     * Total action count during action or vote phase
     */
    private int totalActions;


    /**
     * Whether the game is currently active, otherwise, it is in lobby
     */
    private boolean gameActive;


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
        subServers = new ArrayList<>(); //Initializes the array of subServers
        playerCounter = 0;
        gameActive = false;
        controller = new ServerGameController();
        roundCounter = DEFAULTROUNDS;
        names = new ArrayList<>();
        service = Executors.newFixedThreadPool(17); //Initializes a fixed thread pool of 17
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
     *
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
     *
     * @param request Action request to handle all the data fom
     */
    public void handleActionRequest(ActionRequest request) {
        if (request.getRequestType() == MessageValue.CHAT) { //Checks the value of the message to see if it's a chat
            if (getSubServerByName(request.getRequesterName()) != null && getSubServerByName(request.getRequesterName()).isInGame) { //Checks to see that the person sending the message is real and also in the game
                displayMessage("Chat Request Received from " + request.getRequesterName() + " to " + request.getData2()); //Displays message to the Chat Log
                if (request.getData2() == null || request.getData2().equals("Global")) { //Checks to see that the Chat is supposed to go to global chat or defaults to global chat
                    sendMessageToAll(new NetworkMessage(MessageValue.CHAT, request.getData1(), request.getRequesterName(), null)); //Sends message for everyone connected to the game to see
                } else if (request.getData2().equals("Cult")) { //Implementation of a Cult chat that could not be fully implemented
                    for (int i = 0; i < subServers.size(); i++) {
                        if (subServers.get(i).gameState != null && subServers.get(i).gameState.getIdentifier() == PlayerIdentifier.CULTIST) {
                            subServers.get(i).handler.sendInformation(new NetworkMessage(MessageValue.CHAT, request.getData1(), request.getRequesterName(), null));
                        }
                    }
                }
            }
        } else {
            if (!gameActive) { // If the game is not active
                if (request.getRequestType() == MessageValue.SIGNIN) { //Checks to see if message reveals if a player is singing in
                    if (usernameIsAvailable(request.getData1())) { //If so grabs the userName to set a String Identifier to the player
                        for (int i = 0; i < subServers.size(); i++) { //Loops through the subServers
                            if (subServers.get(i).handler == request.getSender()) { //Checks which subServer the request is from
                                playerCounter++; //Increases overall playerCount
                                subServers.get(i).init(request.getData1());
                                displayMessage("User Connected:" + request.getData1()); //Displays message of the User Connectingg
                            }
                        }
                        request.getSender().sendInformation(new NetworkMessage(MessageValue.SIGNIN, null, null, null)); //Sets message out to the Client
                    }
                }
                if (request.getRequestType() == MessageValue.JOIN) { //Checks to see if message revealse if a player is joing
                    displayMessage("Join Requested");
                    getSubServerByName(request.getRequesterName()).isInGame = true; //Initializes booleans to true to shower player is active
                    getSubServerByName(request.getRequesterName()).isAlive = true;
                    getSubServerByName(request.getRequesterName()).gameState = new GameState(request.getRequesterName()); //Initializes a new GameState for the player
                    controller.addPlayerState(getSubServerByName(request.getRequesterName()).gameState); //Adds the player to the list of GameStates
                    request.getSender().sendInformation(new NetworkMessage(MessageValue.JOIN, null, null, null)); //Sends the join information back to the clien
                    sendMessageToAll(new NetworkMessage(MessageValue.CHAT, "Player Joined", "Server", null)); //Notifies the chat of a player joined
                    for (int i = 0; i < subServers.size(); i++) {
                        if (subServers.get(i).isInGame) {
                            sendMessageToAll(new NetworkMessage(MessageValue.NEWPLAYER, subServers.get(i).username, null, null)); //Notifies all subservers of the new player
                        }
                    }
                }
                if (request.getRequestType() == MessageValue.VOTE) { //Checks to see if message reveals if the player is voting to start
                    for (int i = 0; i < subServers.size(); i++) { //Navigates through all the subservers
                        if (subServers.get(i).handler == request.getSender() && !subServers.get(i).hasActed) { //Checks to see that requester is in that subServer and that player has not acted
                            subServers.get(i).hasActed = true; //Sets acted to true for no double voting
                            votes++; //Increases vote counters
                            if (votes > playerCounter / 2) {
                                startGame(); //starts game once more than half of player vote to start
                            }
                            sendMessageToAll(new NetworkMessage(MessageValue.CHAT, request.getRequesterName() + " has voted to start game", "Server", null));
                        } //Notifies the Chat that a player has voted to start their game
                    }
                }
            } else { // If the game is active
                if (request.getRequesterName() != null && getSubServerByName(request.getRequesterName()) != null) { //Checks to see that the player exists and that subServer exists as well
                    SubServer sub = getSubServerByName(request.getRequesterName());
                    if (sub.isInGame && sub.isAlive && !sub.hasActed) { //Checks to that the subServer is in Game and has not acted and player is alive
                        sub.hasActed = true; //Alerts that the player has made a move so they can't make another one
                        totalActions++; //tracks actions of all players
                        sendMessageToAll(new NetworkMessage(MessageValue.CHAT, sub.username + " has submitted their action", "Server", null)); //Sends a notification
                        if (request.getRequestType() == MessageValue.VOTE) {
                            if (isVoting) {
                                boolean flag = true;
                                for (int i = 0; i < subServers.size(); i++) {
                                    if (subServers.get(i).username != null && subServers.get(i).username.equals(request.getData1())) {
                                        subServers.get(i).votesAgainst++;
                                        flag = false;
                                    }
                                }
                                if (flag) votes++;
                            }
                        } else if (request.getRequestType() == MessageValue.STEAL) {
                            if (!isVoting) {
                                for (NetworkMessage message : controller.steal(request.getRequesterName(), request.getData1())) {
                                    sub.handler.sendInformation(message);
                                }
                            }
                        } else if (request.getRequestType() == MessageValue.PASS) {
                            if (!isVoting) {
                                for (NetworkMessage passMessage : controller.steal(request.getData1(), request.getRequesterName())){
                                    sub.handler.sendInformation(passMessage);
                                }
                            }
                        } else if (request.getRequestType() == MessageValue.INVESTIGATE) {
                            if (!isVoting) {
                                Evidence evidence = controller.investigate(request.getRequesterName(), request.getData1());
                                if (evidence != null) {
                                    sendEvidence(sub, evidence);
                                } else {
                                    displayMessage("Investigate returned null uh oh");
                                }
                            }
                        } else if (request.getRequestType() == MessageValue.FORGE) {
                            Evidence evidence = controller.forge(request.getRequesterName(), request.getData1());
                            if (evidence != null) {
                                sendEvidence(sub, evidence);
                            } else {
                                displayMessage("Investigate returned null uh oh");
                            }
                        }
                        if (totalActions == playerCounter) {
                            roundCounter--;
                            if (roundCounter <= 0) {
                                endGame();
                            } else {
                                endRound();
                                boolean hasImposters = false;
                                boolean hasInnocent = false;
                                for(int i = 0; i < subServers.size(); i++){
                                    if(subServers.get(i).gameState!=null &&
                                            subServers.get(i).gameState.getIdentifier() == PlayerIdentifier.CULTIST){
                                        hasImposters = true;
                                    }
                                    if(subServers.get(i).gameState!=null &&
                                            subServers.get(i).gameState.getIdentifier() == PlayerIdentifier.INNOCENT){
                                        hasInnocent = true;
                                    }
                                }
                                if(!hasImposters)endGame();
                                if(!hasInnocent)endGame();
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendEvidence(SubServer sub, Evidence evidence) {
        if (evidence.getIdentifier() == PlayerIdentifier.INNOCENT) {
            sub.handler.sendInformation(new NetworkMessage(MessageValue.EVIDENCE, evidence.getTarget(), "Innocent", null));
        }
        if (evidence.getIdentifier() == PlayerIdentifier.CULTIST) {
            sub.handler.sendInformation(new NetworkMessage(MessageValue.EVIDENCE, evidence.getTarget(), "Cultist", null));
        }
    }


    /**
     * Ends the round, indicating the round is over to all clients, and resets the game state for a new game
     * Removes all players as active players
     * Client side should return to signin page with username field prefilled with old username
     */
    private void endGame() {
        displayMessage("Game Over, starting new game");
        gameActive = false;
        names = new ArrayList<>();
        controller = new ServerGameController();
        // Clear disconnected players to prevent endless server generation
        for (int i = 0; i < subServers.size(); i++) {
            if (subServers.get(i).handler.isConnected()) {
                if (subServers.get(i).gameState != null && subServers.get(i).gameState.getIdentifier() != PlayerIdentifier.INNOCENT) {
                    subServers.get(i).handler.sendInformation(new NetworkMessage(MessageValue.GAMEOVER, "GAME LOST", null, null));
                } else {
                    subServers.get(i).handler.sendInformation(new NetworkMessage(MessageValue.GAMEOVER, "GAME WON", null, null));
                }
                subServers.get(i).username = null;
                subServers.get(i).isInGame = false;
                subServers.get(i).isAlive = false;
            } else {
                subServers.remove(i);
                i--; // Shift index again since subServer indexes have shifted
            }
        }
        sendMessageToAll(new NetworkMessage(MessageValue.CHAT, "Game is finished, submit chat or action to return to sign on and start new game", "Server", null));
        resetSubServers(); // Reset all subServers
    }

    /**
     * Starts the game, assigning clients as innocents or guilty
     */
    private void startGame() {
        gameActive = true;
        votes = 0;
        roundCounter = DEFAULTROUNDS + playerCounter / 2;
        resetActionsAndVotes();
        displayMessage("Starting Game");
        ArrayList<SubServer> servers = new ArrayList<>();
        for (int i = 0; i < subServers.size(); i++) {
            if (subServers.get(i).isInGame) servers.add(subServers.get(i));
        }
        for (int i = 0; i < playerCounter / 3; i++) {
            int rand = (int) (Math.random() * servers.size());
            servers.get(rand).gameState.setIdentifier(PlayerIdentifier.CULTIST);
            servers.remove(rand).handler.sendInformation(
                    new NetworkMessage(MessageValue.CULTIST, null, null, null));
        }
        while (!servers.isEmpty()) {
            servers.get(0).gameState.setIdentifier(PlayerIdentifier.INNOCENT);
            servers.remove(0).handler.sendInformation(
                    new NetworkMessage(MessageValue.INNOCENT, null, null, null));
        }
        for (int i = 0; i < controller.playerStates.size(); i++) {
            for (int j = 0; j < controller.playerStates.size(); j++) {
                controller.playerStates.get(i).addPlayerFile(controller.playerStates.get(j).getUsername());
            }
        }
        sendMessageToAll(new NetworkMessage(MessageValue.INVESTIGATE, null, null, null));
        sendMessageToAll(new NetworkMessage(MessageValue.CHAT, "Game Started", "Server", null));
    }

    /**
     * Ends the round, indicating the round is over to all clients
     */
    private void endRound() {
        displayMessage("Ending Round");
        sendMessageToAll(new NetworkMessage(MessageValue.CHAT, "ENDING ROUND", "Server", null));
        String playerVotedOut = getPlayerVotedOut();
        removePlayerByName(playerVotedOut);
        resetActionsAndVotes();
        totalActions = 0;
        roundCounter--;
        timerCounter = 0;
        if(isVoting) {
            if (playerVotedOut == null) {
                sendMessageToAll(new NetworkMessage(MessageValue.CHAT, "Voting skipped", "Server", null));
            } else {
                playerCounter--;
                sendMessageToAll(new NetworkMessage(MessageValue.CHAT, playerVotedOut + " was life deleted", "Server", null));
            }
        }
        resetActionsAndVotes();
        sendMessageToAll(new NetworkMessage(MessageValue.ROUNDOVER, playerVotedOut, String.valueOf(roundCounter), null));
        if (isVoting) {
            sendMessageToAll(new NetworkMessage(MessageValue.INVESTIGATE, null, null, null));
            isVoting = !isVoting;
        } else {
            sendMessageToAll(new NetworkMessage(MessageValue.VOTE, null, null, null));
            isVoting = !isVoting;
        }
    }

    /**
     * Remove a player from the active servers, meaning they can no longer play :(
     *
     * @param playerVotedOut player who was voted out
     */
    private void removePlayerByName(String playerVotedOut) {
        for (int i = 0; i < subServers.size(); i++) {
            if (subServers.get(i).isInGame && subServers.get(i).username.equals(playerVotedOut)) {
                subServers.get(i).isAlive = false;
            }
        }
    }

    /**
     * Gets the player who was voted out, if any
     *
     * @return player name or null if nobody was voted out
     */
    private String getPlayerVotedOut() {
        int max = votes;
        String player = null;
        for (int i = 0; i < subServers.size(); i++) {
            if (subServers.get(i).isInGame && subServers.get(i).votesAgainst > votes) {
                player = subServers.get(i).username;
                max = subServers.get(i).votesAgainst;
            }
        }
        return player;
    }

    /**
     * Send a warning that the round is almost up to all players
     */
    private void sendTimeWarning() {
        displayMessage("Time Warning Sent");
        sendMessageToAll(new NetworkMessage(MessageValue.CHAT, "ROUND ALMOST OVER", "Server", null));
    }

    /**
     * Send a message to all clients connected or not
     *
     * @param message message to send
     */
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

    /**
     * Return subserver connected to specific username
     *
     * @param name name of user of subserver
     * @return subserver for client
     */
    private SubServer getSubServerByName(String name) {
        for (int i = 0; i < subServers.size(); i++) {
            if (subServers.get(i).username != null && subServers.get(i).username.equals(name)) {
                return subServers.get(i);
            }
        }
        return null;
    }

    /**
     * Reset actions and votes, done between rounds
     */
    private void resetActionsAndVotes() {
        for (int i = 0; i < subServers.size(); i++) {
            subServers.get(i).votesAgainst = 0;
            subServers.get(i).hasActed = false;
        }
    }

    /**
     * Reset subservers for use between games
     */
    private void resetSubServers() {
        for (SubServer subServer : subServers) {
            subServer.votesAgainst = 0;
            subServer.hasActed = false;
            subServer.isInGame = false;
            subServer.isAlive = false;
            subServer.gameState = null;
            subServer.username = null;
        }
    }
}
