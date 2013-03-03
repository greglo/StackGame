package ox.stackgame.stackmachine;

public class CharStackValue extends StackValue<Character> {
    private final int charCode;
    
    public CharStackValue(int charCode) {
	this.charCode = intToCharCode(charCode);
    }
    
    CharStackValue(char ch) {
	this.charCode = intToCharCode((int)ch - 65);
    }
    
    CharStackValue(Character value) {
	this.charCode = intToCharCode((int)value - 65);
    }
    
    @Override
    public Character getValue() {
	return new Character((char)(charCode + 65));
    }

    private int intToCharCode(int i) {
	return i % 26;
    }

    @Override
    public StackValue<?> add(StackValue<?> y) {
	if (y.getClass() == CharStackValue.class)
	    return new CharStackValue(this.charCode + ((CharStackValue)y).charCode);
	else if (y.getClass() == IntStackValue.class)
	    return new CharStackValue(this.charCode + ((IntStackValue)y).getValue());
	else
	    return null;
    }

    @Override
    public StackValue<Character> sub(StackValue<?> y) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public StackValue<Character> mul(StackValue<?> y) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public StackValue<Character> div(StackValue<?> y) {
	// TODO Auto-generated method stub
	return null;
    }

}
