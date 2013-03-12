import java.util.ArrayList;
import java.util.Stack;

public class Machine {
	private final static int STORE_SIZE = 4;

	private ArrayList< Instruction > program;
	private ArrayList< ArrayList< Value > > allArgs;

	private Stack stack = new Stack();
	private Value[] store = new Value[ STORE_SIZE ];

	private int pc = 0;
 
	public Machine( Program program ) {
		assert( program != null );

		this.program = program.program;
		this.allArgs = program.args;

		this.reset();
	}

	public void reset() {
		pc = 0;
	}

	public void step() {
		ArrayList< Value > args = allArgs.get( pc );

		switch( program.get( pc++ ) ) {
			case PUSH:
				stack.push( args.get( 0 ) );
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
				pc = args.get( 0 ).asInt();
				break;

			case GET:
				stack.push( store[ args.get( 0 ).asInt() ] );
				break;

			case PUT:
				store[ args.get( 0 ).asInt() ] = ( Value ) stack.pop();
				break;

			case DUMP:
				pc--;
				dump();
				pc++;
				break;
		}
	}

	public void dump() {
		System.out.println( "program:" );
		for( int i = Math.max( 0, pc - 5 ); i < Math.min( program.size(), pc + 5 ); i++ ) {
			System.out.print(
				GoodLanguage.e()  + ( i == pc ? "[1m" : "[0m" )
				+ program.get( i ).toString() );

			System.out.print( GoodLanguage.e() + "[0m" );

			for( int j = 0; j < allArgs.get( i ).size(); j++ ) {
				System.out.print( " " + allArgs.get( i ).get( j ).toString() );
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
