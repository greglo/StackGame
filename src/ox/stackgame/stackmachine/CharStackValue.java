package ox.stackgame.stackmachine;

import ox.stackgame.stackmachine.exceptions.TypeException;

public class CharStackValue extends StackValue<Character> {
    private int charCode;

    public CharStackValue() {
    }

    public CharStackValue(int charCode) {
        this.charCode = intToCharCode(charCode);
    }

    public CharStackValue(char ch) {
        this.charCode = intToCharCode((int)ch - 65);
    }

    public CharStackValue(Character value) {
        this.charCode = intToCharCode((int)value - 65);
    }

    public boolean init( String str ) {
        if( str.length() != 1 ) {
            return false;
        }

        this.charCode = intToCharCode((int)str.charAt( 0 ) - 65);

        // TODO: check if it's in our valid charset

        return true;
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

    @Override
    public boolean equals(Object other) {
        return false;
    }

    public boolean equals(CharStackValue other){
        return (charCode == other.charCode);
    }

}
