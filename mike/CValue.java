package mike;

public class CValue extends Value {
	private char value;

	public CValue( char value ) {
		this.value = value;
	}

	public char asChar() {
		return value;
	}

	public String toString() {
		return value + "";
	}
}
