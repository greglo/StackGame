package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public class NoSuchLabelException extends StackRuntimeException {
    private String labelName;

    public NoSuchLabelException(String labelName, int offendingLine) {
        super(offendingLine);
        this.labelName = labelName;
    }

    @Override
    public String toString() {
        return "Line " + this.getLine() + ": Label '" + labelName + "' does not exist";
    }
}
