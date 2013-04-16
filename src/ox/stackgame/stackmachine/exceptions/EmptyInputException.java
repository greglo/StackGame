package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public class EmptyInputException extends StackRuntimeException {
    public EmptyInputException(int offendingLine) {
	super(offendingLine);
    }

    @Override
    public String toString() {
	return "Line " + this.getLine() + ": There is no input to consume";
    }

}
