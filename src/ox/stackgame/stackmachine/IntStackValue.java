package ox.stackgame.stackmachine;

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
    public StackValue<?> add(StackValue<?> y) {
	if (y.getClass() == IntStackValue.class)
	    return new IntStackValue(this.getValue() + (Integer)y.getValue());
	else if (y.getClass() == CharStackValue.class)
	    return y.add(this);
	else
	    return null;
    }

    @Override
    public StackValue<Integer> sub(StackValue<?> y) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public StackValue<Integer> mul(StackValue<?> y) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public StackValue<Integer> div(StackValue<?> y) {
	// TODO Auto-generated method stub
	return null;
    }

}
