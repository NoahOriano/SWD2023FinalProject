import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServer extends JFrame{
    /**Maximum number of players which a game can run at once*/
    public static final int MAXPLAYERCOUNT = 10;
    /**List of ServerClientHandlers, generated to handle connections*/
    private ArrayList<ServerClientHandler> clientHandlers = new ArrayList<ServerClientHandler>();
    /**Connection service which allows connection to clients to be retrieved*/
    private GameClientConnectionService connectionService;
    /**Text area used to display information to used*/
    private final JTextArea displayArea;
    /**Executor service used to handle threads*/
    private ExecutorService service;
    /**Connections to give to client handlers*/
    private ArrayBlockingQueue<ServerRequest> requests;
    /**Counter of number of connections*/
    private int counter = 1;
    /**List of client handlers*/
    private final ServerClientHandler[] servers = new ServerClientHandler[MAXPLAYERCOUNT];

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
        service = Executors.newFixedThreadPool(17);
        requests = new ArrayBlockingQueue<ServerRequest>(100);
        connectionService = new GameClientConnectionService(port, backlog, requests);
        service.execute(connectionService);
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
        servers[counter] = new ServerClientHandler(connection);
        service.execute(servers[counter]);
        counter++;
    }

    /**
     * Handles the given action request, if the request is not valid, it will be ignored, for example, a player
     * Tries acting twice. Also sends data back to the client handler to send to the client on action completion
     * @param request Action request to handle all the data fom
     */
    public void handleActionRequest(ActionRequest request){
        // Make this
    }

    /**
     * Displays a message, adding it to the log
     * @param messageToDisplay the message to display
     */
    private void displayMessage(final String messageToDisplay) {
        displayArea.append(messageToDisplay);
    }

}
