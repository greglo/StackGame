public abstract class Value {
	public abstract int asInt();

	public abstract Value ADD( Value rhs );
	public abstract Value SUB( Value rhs );

	public abstract Value add( IValue rhs );
	public abstract Value sub( IValue rhs );
}
