package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public class FullStackException extends StackRuntimeException {

    public FullStackException(int offendingLine) {
	super(offendingLine);
    }

}
