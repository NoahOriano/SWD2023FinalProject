import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class GameServer {
    /**Maximum number of players which a game can run at once*/
    public static final int MAXPLAYERCOUNT = 10;
    /**
     * Executor service used to handle threads
     */
    ExecutorService service;
    /**
     * List of ServerClientHandlers, generated to handle connections
     */
    ArrayList<ServerClientHandler> clientHandlers = new ArrayList<ServerClientHandler>();
    public static void main(String[] args) {

    }
}
