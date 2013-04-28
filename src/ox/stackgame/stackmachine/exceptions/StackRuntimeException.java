package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public abstract class StackRuntimeException extends Exception {
    private int offendingLine;

    public StackRuntimeException(int offendingLine) {
        this.offendingLine = offendingLine;
    }

    public int getLine() {
        return offendingLine;
    }

    @Override
    public abstract String toString();
}
