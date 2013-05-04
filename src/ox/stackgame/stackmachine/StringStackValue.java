package ox.stackgame.stackmachine;

import ox.stackgame.stackmachine.exceptions.TypeException;

public class StringStackValue extends StackValue<String> {
    private String value;

    public StringStackValue() {
    }

    public StringStackValue(String value) {
        this.value = value;
    }

    public boolean init( String str ) {
        this.value = str;

        return true;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public StackValue<?> add(StackValue<?> y) throws TypeException {
        throw new TypeException(0, this.getClass());
    }

    @Override
    public StackValue<?> sub(StackValue<?> y) throws TypeException {
        throw new TypeException(0, this.getClass());
    }

    @Override
    public StackValue<?> mul(StackValue<?> y) throws TypeException {
        throw new TypeException(0, this.getClass());
    }

    @Override
    public StackValue<?> div(StackValue<?> y) throws TypeException {
        throw new TypeException(0, this.getClass());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof StackValue<?>) {
            StackValue<?> otherStackValue = (StackValue<?>) other;
            return value.equals(otherStackValue.toString()); 
        } else
            return false;
    }

    public int hashCode() {
        return value.hashCode();
    }
}
