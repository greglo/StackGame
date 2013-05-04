package ox.stackgame.stackmachine;

import ox.stackgame.stackmachine.exceptions.DivisionByZeroException;
import ox.stackgame.stackmachine.exceptions.TypeException;

public class IntStackValue extends StackValue<Integer> {
    private Integer value;

    public IntStackValue() {
    }

    public IntStackValue(int value) {
        this.value = value;
    }

    public boolean init(String str) {
        try {
            this.value = Integer.parseInt(str);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public StackValue<?> add(StackValue<?> y) throws TypeException {
        if (y instanceof IntStackValue)
            return new IntStackValue(this.getValue() + (Integer) y.getValue());
        else if (y instanceof CharStackValue)
            return y.add(this);
        else
            throw new TypeException(0, y.getClass());
    }

    @Override
    public StackValue<?> sub(StackValue<?> y) throws TypeException {
        if (y instanceof IntStackValue)
            return new IntStackValue(this.getValue() - (Integer) y.getValue());
        else if (y instanceof CharStackValue)
            return new IntStackValue(this.getValue() - ((CharStackValue) y).getCharCode());
        else
            throw new TypeException(0, y.getClass());
    }

    @Override
    public StackValue<?> mul(StackValue<?> y) throws TypeException {
        if (y instanceof IntStackValue)
            return new IntStackValue(this.getValue() * (Integer) y.getValue());
        else if (y instanceof CharStackValue)
            return y.mul(this);
        else
            throw new TypeException(0, y.getClass());
    }

    @Override
    public StackValue<?> div(StackValue<?> y) throws TypeException, DivisionByZeroException {
        if (y instanceof IntStackValue) {
            int denominator = (Integer) y .getValue();
            if (denominator != 0)
                return new IntStackValue(this.getValue() / denominator);
            else
                throw new DivisionByZeroException(0);
        } else
            throw new TypeException(0, y.getClass());
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof IntStackValue) ? (value == ((IntStackValue)other).value) : false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
