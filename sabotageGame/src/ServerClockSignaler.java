import java.util.concurrent.ArrayBlockingQueue;

public class ServerClockSignaler {
    ArrayBlockingQueue<ServerRequest> requests;
    public ServerClockSignaler(ArrayBlockingQueue<ServerRequest> requests){
        this.requests = requests;
    }

    public void setRoundTimer(int seconds){

    }

    public void sendMessage(){

    }
}
