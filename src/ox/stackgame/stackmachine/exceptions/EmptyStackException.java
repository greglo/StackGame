package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public class EmptyStackException extends StackRuntimeException {

    public EmptyStackException(int offendingLine) {
	super(offendingLine);
    }

}
