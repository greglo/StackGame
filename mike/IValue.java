package mike;

public class IValue extends Value {
	private int value;

	public IValue( int value ) {
		this.value = value;
	}

	public int asInt() {
		return value;
	}

	public String toString() {
		return Integer.toString( value );
	}

	public static IValue valueOf( String str ) {
		return new IValue( Integer.parseInt( str ) );
	}
}
