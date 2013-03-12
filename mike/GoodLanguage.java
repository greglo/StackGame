// "There is no technical reason why an interface couldn't support static methods."
// http://stackoverflow.com/questions/512877/why-cant-i-define-a-static-method-in-a-java-interface

public class GoodLanguage {
	public static IValue ivalueOf( String str ) {
		return new IValue( Integer.parseInt( str ) );
	}
	
	public static String e() {
		return Character.toString( ( char ) 27 );
	}
}
