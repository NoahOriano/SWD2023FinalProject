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
    ArrayList<ServerClientHandler> clientHandlers = new ArrayList<ServerClientHandler>();
    /**Connection service which allows connection to clients to be retrieved*/
    GameClientConnectionService connectionService;
    /**Text area used to display information to used*/
    private JTextArea displayArea;
    /**output stream to client*/
    private ObjectOutputStream output;
    /**input stream to client*/
    private ObjectInputStream input;
    /**Executor service used to handle threads*/
    private ExecutorService service;
    /**Connections to give to client handlers*/
    private ArrayBlockingQueue<ServerRequest> requests;
    /**Counter of number of connections*/
    private int counter = 1;
    /**List of client handlers*/
    private final ServerClientHandler[] servers = new ServerClientHandler[MAXPLAYERCOUNT];

    /**Constructor, which sets up a GUI*/
    public GameServer(int port, int backlog) {
        super("GameServer");
        runServer(port, backlog);
    }
    /**Constructor, which sets up a GUI*/
    public GameServer() {
        this(23791, 100);

    }
    /**Set up and run server*/
    public void runServer(int port, int backlog) {
        service = Executors.newFixedThreadPool(17);
        requests = new ArrayBlockingQueue<ServerRequest>(20);
        connectionService = new GameClientConnectionService(port, backlog, requests);
        service.execute(connectionService);
        while (true) {
            try {
                ServerRequest request = requests.poll(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**Sends a connection to a sub-server*/
    public void sendConnectionToClientHandler(Socket connection){
        servers[counter] = new ServerClientHandler(connection);
        service.execute(servers[counter]);
        counter++;
    }

    /**Displays a message, adding it to the log*/
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                displayArea.append(messageToDisplay); // append message
            }
        });
    }

}
