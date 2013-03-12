import java.util.ArrayList;

public class Program {
	public final ArrayList< Instruction > program;
	public final ArrayList< Value > args;

	public Program( ArrayList< Instruction > program, ArrayList< Value > args ) {
		this.program = program;
		this.args = args;
	}
}
