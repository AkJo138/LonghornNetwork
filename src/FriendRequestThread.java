import java.util.concurrent.Semaphore;
/**
 * Thread for sending and receiving a friend request
 */
public class FriendRequestThread implements Runnable {

    private UniversityStudent sender;
    private UniversityStudent receiver;

    private static final Semaphore semaphore = new Semaphore(1);
    /**
     * Constructor for new FriendRequestThread
     *
     * @param sender student sending the friend request
     * @param receiver student receiving the friend request
     */
    public FriendRequestThread(UniversityStudent sender, UniversityStudent receiver) {
        // Constructor
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Runs the action of requesting a friend, this method is implemented from Runnable
     * interface
     */
    @Override
    public void run() {
        try{
            semaphore.acquire();
            sender.addFriend(receiver);
            receiver.addFriend(sender);
            System.out.println("FriendRequest (Thread-Safe): " + sender.name  + " sent a friend request to " + receiver.name);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("FriendRequest interrupted: " + e.getMessage());
        }
        finally{
            semaphore.release();    
        } 
    }
}
