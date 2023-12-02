import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ArrayBlockingQueue;

public class GameClientConnectionService implements Runnable{
    /**Server socket for retrieving connections*/
    private ServerSocket server;
    /**Connection to client which will be passed to handler*/
    private ServerRequest connection;
    /**Connections to clients the GameServer needs to handle*/
    private ArrayBlockingQueue<ServerRequest> connections;

    /**Port server is hosted on*/
    private int port;
    /**Backlog size*/
    private int backlog;

    /**
     * Generates service hosted on given port that will push connections to given server requests queue
     * @param port port to host Server Socket on
     * @param backlog size of backlog
     * @param connections queue to push connections to
     */
    GameClientConnectionService(int port, int backlog, ArrayBlockingQueue<ServerRequest> connections){
        this.port = port;
        this.backlog = backlog;
        this.connections = connections;
    }

    /**Set up and run server*/
    public void runServer() {
        try // set up server to receive connections; process connections
        {
            server = new ServerSocket(port, backlog); // create ServerSocket
            sendMessageRequest("ServerSocket open on Client Connection Service");
            while (true) {
                try {
                    waitForConnection(); // wait for a connection
                    sendConnectionToGameServer(); // give connection to sockServer
                } catch (EOFException eofException) {
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**Run the service*/
    @Override
    public void run() {
        runServer();
    }

    public void sendMessageRequest(String message ){
        connections.add(new MessageRequest(message));
    }

    /**Sends a connection back to game server*/
    public void sendConnectionToGameServer(){
        connections.add(connection);
        connection = null;
    }

    /**Wait for a connection to arrive, collecting it*/
    private void waitForConnection() throws IOException {
        connection = new ConnectionRequest(server.accept()); // allow server to accept connection
    }
}
