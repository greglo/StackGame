import java.util.ArrayList;
import java.util.Stack;

public class Machine {
	private final static int STORE_SIZE = 4;

	private ArrayList< Instruction > originalProgram;
	private ArrayList< Value > originalArgs;

	private ArrayList< Instruction > program;
	private ArrayList< Value > args;

	private Stack stack = new Stack();
	private Value[] store = new Value[ STORE_SIZE ];

	private int pc = 0;
	private int ac = 0;
 
	public Machine( Program program ) {
		assert( program != null );
		assert( args != null );

		this.originalProgram = program.program;
		this.originalArgs = program.args;

		this.reset();
	}

	public void reset() {
		// the casts are here because good language
		program = ( ArrayList< Instruction > ) originalProgram.clone();
		args = ( ArrayList< Value > ) originalArgs.clone();

		pc = 0;
		ac = 0;
	}

	public void step() {
		switch( program.get( pc++ ) ) {
			case PUSH:
				stack.push( args.get( ac++ ) );
				break;

			case ADD:
				stack.push( ( ( Value ) stack.pop() ).ADD( ( Value ) stack.pop() ) );
				break;

			case SUB:
				stack.push( ( ( Value ) stack.pop() ).SUB( ( Value ) stack.pop() ) );
				break;

			case NOP:
				break;
			
			case JUMP:
				pc = args.get( ac++ ).asInt();
				break;

			case GET:
				stack.push( store[ args.get( ac++ ).asInt() ] );
				break;

			case PUT:
				store[ args.get( ac++ ).asInt() ] = ( Value ) stack.pop();
				break;

			case DUMP:
				dump();
				break;
		}
	}

	public void dump() {
		System.out.println( "program:" );
		for( int i = Math.max( 0, pc - 5 ); i < Math.min( program.size(), pc + 5 ); i++ ) {
			System.out.print(
				GoodLanguage.e()  + ( i == pc ? "[1m" : "[0m" )
				+ program.get( i ).toString() + " " );
		}

		System.out.println( GoodLanguage.e() + "[0m" );

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
