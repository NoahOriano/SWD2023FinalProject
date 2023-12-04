/**
 * GameServerStarter is the class that initializes the server for the clients/players to connect to
 */
public class GameServerStarter {
    public static void main(String[] args) { // The main driver method for the game server that initializes the server and connects it to its socket and port.
        GameServer server;
        if(args.length > 1 && args[0]!= null && args[1]!= null){
            server = new GameServer(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        }
        else{
            server = new GameServer();
        }
        server.runServer();
    }
}
