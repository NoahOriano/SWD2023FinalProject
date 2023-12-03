import java.util.concurrent.ArrayBlockingQueue;

public class ServerClockSignaler implements Runnable{
    ArrayBlockingQueue<ServerRequest> requests;

    private int delay;

    public ServerClockSignaler(int delay){
        this.delay = delay;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(delay);
                try {
                    requests.put(new ActionRequest(MessageValue.TIMER, null, null, null, null));
                } catch (InterruptedException e) {
                    //Ignore lol
                }
            } catch (InterruptedException e) {
                //Ignore lol
            }
        }
    }
}
