package mike;

import java.util.*;

public class Ops {
	private static final Hashtable< String, Op > ht = new Hashtable< String, Op >();
	private static boolean initialised = false;

	private static void init() {
		ht.put( "push", new Op() {
			public final int args = 1;

			public void apply( Machine m, ArrayList< Value > args ) {
				m.stack.push( args.get( 0 ) );
			}
		} );

		ht.put( "add", new Op() {
			public final int args = 0;

			public void apply( Machine m, ArrayList< Value > args ) {
				m.stack.push( ( ( Value ) m.stack.pop() ).add( ( Value ) m.stack.pop() ) );
			}
		} );

		ht.put( "sub", new Op() {
			public final int args = 0;

			public void apply( Machine m, ArrayList< Value > args ) {
				m.stack.push( ( ( Value ) m.stack.pop() ).sub( ( Value ) m.stack.pop() ) );
			}
		} );

		ht.put( "mul", new Op() {
			public final int args = 0;

			public void apply( Machine m, ArrayList< Value > args ) {
				m.stack.push( ( ( Value ) m.stack.pop() ).mul( ( Value ) m.stack.pop() ) );
			}
		} );

		ht.put( "div", new Op() {
			public final int args = 0;

			public void apply( Machine m, ArrayList< Value > args ) {
				m.stack.push( ( ( Value ) m.stack.pop() ).div( ( Value ) m.stack.pop() ) );
			}
		} );

		ht.put( "jump", new Op() {
			public final int args = 1;

			public void apply( Machine m, ArrayList< Value > args ) {
				/* m.pc = m.labels.get( args.get( 0 ).asString() ); */
				m.pc = args.get( 0 ).asInt();
			}
		} );

		ht.put( "dump", new Op() {
			public final int args = 0;

			public void apply( Machine m, ArrayList< Value > args ) {
				m.dump();
			}
		} );

		initialised = true;
	}

	public static Op get( String name ) {
		if( !initialised ) {
			init();
		}

		return ht.get( name );
	}
}
