package ox.stackgame.stackmachine.exceptions;

@SuppressWarnings("serial")
public class InvalidAddressException extends StackRuntimeException {
    private final int address;

    public InvalidAddressException(int address, int offendingLine) {
        super(offendingLine);
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public String getMessage() {
        return "Line " + this.getLine() + ": Address '" + address + "' is not valid";
    }
}
