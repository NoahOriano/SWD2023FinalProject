import java.util.concurrent.ArrayBlockingQueue;

/**
 * ServerClockSignaler Class is used to be a timer to ensure that no action takes an exceedingly long amount of time and
 * control game flow
 */
public class ServerClockSignaler implements Runnable{
    /**ArrayBlockingQueue allows the signaler to send requests to the gameServer*/
    ArrayBlockingQueue<ServerRequest> requests;
    /**Delay sets the time that the thread will sleep*/
    private int delay;

    /**
     * Constructor for ServerClockSignaler
     * @param delay input intializes the amount of delay for the ServerClockSignaler
     * @param requests input allows ServerClockSignaler to join the list of ServerRequests
     */
    public ServerClockSignaler(int delay, ArrayBlockingQueue<ServerRequest> requests){
        this.delay = delay;
        this.requests = requests;
    }

    /**
     * When ServerClockSignaler is called to run it waits for a set amonunt of time determined by its delay, once it's
     * done waiting it inputs a request to end the current action as it has taken too long
     */
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(delay);
                try {
                    requests.put(new ActionRequest(MessageValue.TIMER, null, null, null, null, null));
                } catch (InterruptedException e) {
                    //Ignore lol
                }
            } catch (InterruptedException e) {
                //Ignore lol
            }
        }
    }
}
