package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public class EmptyInputException extends StackRuntimeException {

    public EmptyInputException(int offendingLine) {
	super(offendingLine);
    }
    
    @Override
    public String toString() {
	return "Line " + this.getLine() + ": Cannot read from the input because it is empty";
    }

}
