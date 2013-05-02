package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public class FullStackException extends StackRuntimeException {
    public FullStackException(int offendingLine) {
        super(offendingLine);
    }

    @Override
    public String getMessage() {
        return "Line " + this.getLine() + ": Cannot push on to stack because it is full";
    }
}
