import java.net.Socket;

public class ConnectionRequest extends ServerRequest{
    public static final int TYPE = 0;

    private Socket connection;

    public ConnectionRequest(Socket connection) {
        super();
        this.connection = connection;
    }

    public Socket getConnection() {
        return connection;
    }
}
