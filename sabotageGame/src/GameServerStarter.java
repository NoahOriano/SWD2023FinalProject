public class GameServerStarter {
    public static void main(String[] args) {
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
