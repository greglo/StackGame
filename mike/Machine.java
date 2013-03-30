package mike;

import java.util.*;

public class Machine {
	private final static int STORE_SIZE = 4;

	private ArrayList< String > program;
	private ArrayList< ArrayList< Value > > args;
	protected Hashtable< String, Integer > labels;

	protected Stack stack = new Stack();
	protected Value[] store = new Value[ STORE_SIZE ];

	protected int pc = 0;
 
	public Machine( Program program ) {
		assert( program != null );

		this.program = program.program;
		this.args = program.args;
		this.labels = program.labels;

		this.reset();
	}

	public void reset() {
		pc = 0;
	}

	public void step() {
		String op = program.get( pc );
		ArrayList< Value > opArgs = args.get( pc );

		pc++;

		Ops.get( op ).apply( this, opArgs );
	}

	public void dump() {
		System.out.println( "program:" );
		for( int i = Math.max( 0, pc - 5 ); i < Math.min( program.size(), pc + 5 ); i++ ) {
			System.out.print(
				VT102.e()  + ( i == pc ? "[1m" : "[0m" )
				+ program.get( i ).toString() );

			System.out.print( VT102.e() + "[0m" );

			for( int j = 0; j < args.get( i ).size(); j++ ) {
				System.out.print( " " + args.get( i ).get( j ).toString() );
			}

			System.out.println();
		}

		System.out.println( "stack:" );
		for( int i = 0; i < stack.size(); i++ ) {
			System.out.print( stack.get( i ).toString() + " " );
		}

		System.out.println();

		System.out.println( "store:" );
		for( int i = 0; i < STORE_SIZE; i++ ) {
			System.out.print( store[ i ] == null ? "[null] " : ( store[ i ].toString() + " " ) );
		}

		System.out.println();
	}
}
