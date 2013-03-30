package mike;

public class stuff {
	private static final String prog = ""
		+ "jump 1\n"
		+ "push 5\n"
		+ "push 7\n"
		+ "jump 5\n"
		+ "push 100\n"
		+ "add\n"
		+ "dump\n" 
		+ "nop\n";

	public static void main( String[] args ) {
		Machine machine = new Machine( Lexer.lex( prog ) );

		machine.step();
		machine.step();
		machine.step();
		machine.step();
		machine.step();
		machine.step();
		machine.step();
	}
}
