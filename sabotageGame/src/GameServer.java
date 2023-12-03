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


public class GameServer extends JFrame{
    /**Maximum number of players which a game can run at once*/
    public static final int MAXPLAYERCOUNT = 10;
    /**List of sub servers, generated to handle connections*/
    private ArrayList<SubServer> subServers = new ArrayList<SubServer>();
    /**Connection service which allows connection to clients to be retrieved*/
    private ServerClientConnectionService connectionService;
    /**Text area used to display information to used*/
    private final JTextArea displayArea;
    /**Executor service used to handle threads*/
    private ExecutorService service;
    /**Connections to give to client handlers*/
    private ArrayBlockingQueue<ServerRequest> requests;
    /**Counter of number of connections*/
    private int counter = 1;

    private ServerClockSignaler roundTimer;

    private ArrayList<String> names;

    private class SubServer{
        private ServerClientHandler handler; // ServerClientHandler connected to client
        private String username; // Username of connected player, null identifies a player not signed in
        private boolean hasActed; // Whether the connected player has acted this turn

        private GameState gameState;
        public SubServer(ServerClientHandler handler, String username, boolean hasActed){
            this.handler = handler;
            this.username = username;
            this.hasActed = hasActed;
        }
    }
    private int port;
    private int backlog;

    /**Constructor, which sets up a GUI*/
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
    /**Constructor, which sets up a GUI*/
    public GameServer() {
        this(23791, 100);
    }
    /**Set up and run server*/
    public void runServer() {
        names = new ArrayList<>();
        service = Executors.newFixedThreadPool(17);
        requests = new ArrayBlockingQueue<ServerRequest>(100);
        connectionService = new ServerClientConnectionService(port, backlog, requests);
        service.execute(connectionService);
        try {
            displayMessage("IP:"+ Inet4Address.getLocalHost().getHostAddress()+", PORT: "+port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                ServerRequest request = requests.poll(1000, TimeUnit.MILLISECONDS);
                if(request!= null) {
                    if (request.getClass() == ActionRequest.class) {
                        handleActionRequest(((ActionRequest)request));
                    }
                    if (request.getClass() == ConnectionRequest.class) {
                        sendConnectionToClientHandler(((ConnectionRequest)request).getConnection());
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
    public void sendConnectionToClientHandler(Socket connection){
        SubServer newServer = new SubServer(new ServerClientHandler(connection, this.requests), null, false);
        subServers.add( newServer);
        service.execute(newServer.handler);
    }

    /**
     * Handles the given action request, if the request is not valid, it will be ignored, for example, a player
     * Tries acting twice. Also sends data back to the client handler to send to the client on action completion
     * @param request Action request to handle all the data fom
     */
    public void handleActionRequest(ActionRequest request){
        if(request.getRequestType() == MessageValues.SIGNIN){
            displayMessage("Username Request Accepted");
            if(usernameIsAvailable(request.getData1())){
                for(int i = 0; i < subServers.size(); i++){
                    if(subServers.get(i).handler == request.getSender()){
                        subServers.get(i).username = request.getData1();
                        names.add(request.getData1());
                        System.out.println(subServers.get(i).username);
                    }
                }
                request.getSender().sendInformation(new NetworkMessage(MessageValues.SIGNIN, null, null, null));
            }
        }
    }

    private boolean usernameIsAvailable(String data1) {
        if(data1 == null) return false;
        for(int i = 0; i < subServers.size(); i++){
            if(subServers.get(i).username!= null){
                if(data1.equals(subServers.get(i).username)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Displays a message, adding it to the log
     * @param messageToDisplay the message to display
     */
    private void displayMessage(final String messageToDisplay) {
        displayArea.append("\n"+messageToDisplay);
    }

}
