public class IValue extends Value {
	protected int value;

	public IValue( int value ) {
		this.value = value;
	}

	public int asInt() {
		return value;
	}

	public Value ADD( Value rhs ) {
		return rhs.add( this );
	}
	public Value add( IValue rhs ) {
		return new IValue( value + rhs.value );
	}

	public Value SUB( Value rhs ) {
		return rhs.sub( this );
	}
	public Value sub( IValue rhs ) {
		return new IValue( value - rhs.value );
	}

	public String toString() {
		return Integer.toString( value );
	}
}
