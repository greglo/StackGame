package mike;

import java.util.*;

public class Program {
	public final ArrayList< String > program;
	public final ArrayList< ArrayList< Value > > args;
	public final Hashtable< String, Integer > labels;

	public Program(
		ArrayList< String > program,
		ArrayList< ArrayList< Value > > args,
		Hashtable< String, Integer > labels
	) {
		assert( program != null );
		assert( args != null );
		assert( labels != null );

		this.program = program;
		this.args = args;
		this.labels = labels;
	}
}
