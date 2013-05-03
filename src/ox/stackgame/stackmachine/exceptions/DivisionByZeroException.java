package ox.stackgame.stackmachine.exceptions;

/**
 * Thrown by the stack machine when the number of run instructions
 *         is more than is considered reasonable, and we assume the worst (that
 *         execution will never end)
 * @author Greg 
 */

@SuppressWarnings("serial")
public class DivisionByZeroException extends StackRuntimeException {
    public DivisionByZeroException(int offendingLine) {
        super(offendingLine);
    }

    @Override
    public String getMessage() {
        return "Line " + this.getLine() + ": You cannot divide by zero!";
    }
}
