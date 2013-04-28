package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public class TypeException extends StackRuntimeException {
    private final Class<?> type;

    public TypeException(int offendingLine, Class<?> type) {
        super(offendingLine);
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Line " + this.getLine() + ": Type error, '" + type.toString() + "' not valid";
    }
}
