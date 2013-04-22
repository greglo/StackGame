package ox.stackgame.stackmachine;

import ox.stackgame.stackmachine.exceptions.TypeException;

public class IntStackValue extends StackValue<Integer> {
    private final Integer value;

    public IntStackValue(int value) {
	this.value = value;
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
	    return new IntStackValue(this.getValue()
		    - ((CharStackValue) y).getCharCode());
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
    public StackValue<?> div(StackValue<?> y) throws TypeException {
	if (y instanceof IntStackValue)
	    return new IntStackValue(this.getValue() / (Integer) y.getValue());
	else
	    throw new TypeException(0, y.getClass());
    }

    @Override
    public boolean equals(Object other) {
	return false;
    }

    public boolean equals(IntStackValue other) {
	return (value == other.value);
    }

}
