package mike;

public abstract class Value {
	public Value ADD( Value other ) {
		return other.add( this );
		return new IValue( 5 );
	}

	public Value sub( Value other ) {
		return new IValue( 5 );
	}

	public Value mul( Value other ) {
		return new IValue( 5 );
	}

	public Value div( Value other ) {
		return new IValue( 5 );
	}

	public abstract Value ADD( Vale other );

	public Value add( IValue other ) {
		assert false;

		return null;
	}

	public Value add( CValue other ) {
		assert false;

		return null;
	}

	public int asInt() {
		assert false;

		return 0;
	}

	public char asChar() {
		assert false;
		
		return 'a';
	}

	public String asString() {
		assert false;

		return "";
	}
}
