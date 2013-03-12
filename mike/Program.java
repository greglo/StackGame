import java.util.ArrayList;

public class Program {
	public final ArrayList< Instruction > program;
	public final ArrayList< ArrayList< Value > > args;

	public Program( ArrayList< Instruction > program, ArrayList< ArrayList< Value > > args ) {
		assert( program != null );
		assert( args != null );

		this.program = program;
		this.args = args;
	}
}
