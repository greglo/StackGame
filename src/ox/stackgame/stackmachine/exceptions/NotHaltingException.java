package ox.stackgame.stackmachine.exceptions;

/**
 * Thrown by the stack machine when the number of run instructions
 *         is more than is considered reasonable, and we assume the worst (that
 *         execution will never end)
 * @author Greg 
 */

@SuppressWarnings("serial")
public class NotHaltingException extends Exception {
    public NotHaltingException(int numInstructions) {
	super("Execution terminated because the program may not end");
    }
}
