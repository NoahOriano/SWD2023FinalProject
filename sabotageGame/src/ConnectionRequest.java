import java.net.Socket;

public class ConnectionRequest extends ServerRequest{

    private Socket connection;

    public ConnectionRequest(Socket connection) {
        super();
        this.connection = connection;
    }

    public Socket getConnection() {
        return connection;
    }
}
