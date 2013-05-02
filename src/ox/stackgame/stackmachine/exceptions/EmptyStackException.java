package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public class EmptyStackException extends StackRuntimeException {
    public EmptyStackException(int offendingLine) {
        super(offendingLine);
    }

    @Override
    public String getMessage() {
        return "Line " + this.getLine() + ": Cannot read from the stack because it is empty";
    }
}
