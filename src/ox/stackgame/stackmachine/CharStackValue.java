package ox.stackgame.stackmachine;

import ox.stackgame.stackmachine.exceptions.*;

public class CharStackValue extends StackValue<Character> {
    private int charCode;

    public CharStackValue() {
    }

    public CharStackValue( Character ch ) throws InvalidCharException {
        ch = Character.toUpperCase( ch );

        char code = ch.charValue();

        if( code < 'A' || code > 'Z' ) {
            throw new InvalidCharException();
        }

        charCode = toInternalCode( code );
    }

    private CharStackValue( int code ) {
        charCode = code;
    }

    private int toInternalCode( char code ) {
        return code - 'A';
    }

    private char fromInternalCode( int code ) {
        return ( char ) ( code + 'A' );
    }

    private static int addCodes( int c1, int c2 ) {
        return ( ( ( c1 + c2 ) % 26 ) + 26 ) % 26;
    }

    public boolean init( String str ) {
        if( str.length() != 1 ) {
            return false;
        }

        char code = str.toUpperCase().charAt( 0 );
        charCode = toInternalCode( code );

        return code >= 'A' && code <= 'Z';
    }

    @Override
    public Character getValue() {
        return new Character( fromInternalCode( charCode ) );
    }

    public int getCharCode() {
        return charCode;
    }

    @Override
    public StackValue< ? > add( StackValue< ? > v ) throws TypeException {
        Class< ? > type = v.getClass();

        if( type == CharStackValue.class ) {
            return new CharStackValue( addCodes( charCode, ( ( CharStackValue ) v ).charCode ) );
        }

        if( type == IntStackValue.class ) {
            return new CharStackValue( addCodes( charCode, ( ( IntStackValue ) v ).getValue() ) );
        }

        throw new TypeException( 0, v.getClass() );
    }

    @Override
    public StackValue< ? > sub( StackValue< ? > v ) throws TypeException {
        Class< ? > type = v.getClass();

        if( type == CharStackValue.class ) {
            return new CharStackValue( addCodes( charCode, -( ( CharStackValue ) v ).charCode ) );
        }

        if( type == IntStackValue.class ) {
            return new CharStackValue( addCodes( charCode, -( ( IntStackValue ) v ).getValue() ) );
        }

        throw new TypeException( 0, v.getClass() );
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
        return (other instanceof CharStackValue) ? (this.charCode == ((CharStackValue)other).charCode) : false;
    }

    public int hashCode() {
        return new Integer(charCode).hashCode();
    }
}
