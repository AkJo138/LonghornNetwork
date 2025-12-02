/**
 * Thread that makes "chat" between two UniversityStudent instances possible.
 */
public class ChatThread implements Runnable {
    
    private UniversityStudent sender;
    private UniversityStudent receiver;
    private String message;

    /**
     * Constructor for a new ChatThread
     *
     * @param sender   student sending message
     * @param receiver student receiving message
     * @param message  the String message transmitted
     */
    public ChatThread(UniversityStudent sender, UniversityStudent receiver, String message) {
        // Constructor
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * Since implements Runnable, run method is executed when thread is started
     * Performs the action of messaging between sender and receiver
     */
    @Override
    public void run() {
        sender.addToChatHistory("To " + receiver.name + ": " + message);
        receiver.addToChatHistory("From " + sender.name + ": " + message);
        System.out.println(sender.name + " sent message to " + receiver.name + ": " + message);
    }
}
