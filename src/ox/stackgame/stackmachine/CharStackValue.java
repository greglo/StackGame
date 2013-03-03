package ox.stackgame.stackmachine;

import ox.stackgame.stackmachine.exceptions.TypeException;

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
    
    public int getCharCode() {
	return charCode;
    }

    private int intToCharCode(int i) {
	while (i < 0)
	    i += 26;
	return i % 26;
    }

    @Override
    public StackValue<?> add(StackValue<?> y) throws TypeException {
	if (y.getClass() == CharStackValue.class)
	    return new CharStackValue(this.charCode + ((CharStackValue)y).charCode);
	else if (y.getClass() == IntStackValue.class)
	    return new CharStackValue(this.charCode + ((IntStackValue)y).getValue());
	else
	    throw new TypeException(0, y.getClass());
    }

    @Override
    public StackValue<?> sub(StackValue<?> y) throws TypeException {
	if (y.getClass() == CharStackValue.class)
	    return new CharStackValue(this.charCode - ((CharStackValue)y).charCode);
	else if (y.getClass() == IntStackValue.class)
	    return new CharStackValue(this.charCode - ((IntStackValue)y).getValue());
	else
	    throw new TypeException(0, y.getClass());
    }

    @Override
    public StackValue<?> mul(StackValue<?> y) throws TypeException {
	throw new TypeException(0, this.getClass());
    }

    @Override
    public StackValue<?> div(StackValue<?> y) throws TypeException {
	throw new TypeException(0, this.getClass());
    }

}
