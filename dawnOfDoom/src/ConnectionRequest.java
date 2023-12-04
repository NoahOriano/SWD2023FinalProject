import java.net.Socket;

/**
 * ConnectionRequest to establish a connection with the socket
 */
public class ConnectionRequest extends ServerRequest{

    /**
     * Socket connection stores the variable of the socket for the class
     */
    private Socket connection;

    /**
     * ConnectionRequest constructor that establishes its socket
     * @param connection is the input for the socket
     */
    public ConnectionRequest(Socket connection) {
        super();
        this.connection = connection;
    }

    /**
     * Getter that return the socket item
     * @return the socket
     */
    public Socket getConnection() {
        return connection;
    }
}
