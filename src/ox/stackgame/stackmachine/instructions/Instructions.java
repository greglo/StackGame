package ox.stackgame.stackmachine.instructions;

import java.util.*;
import java.lang.*;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.exceptions.TypeException;

import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.CharStackValue;
import ox.stackgame.stackmachine.IntStackValue;

public class Instructions {
	private static final Hashtable< String, Instruction > ht = new Hashtable< String, Instruction >();
	private static boolean initialised = false;

	private static void init() {
		ht.put( "add", new BinopInstruction() {
			public StackValue< ? > binop( StackValue< ? > x, StackValue< ? > y ) throws TypeException {
				return x.add( y );
			}

			public int numArgs() {
				return 0;
			}
		} );

		ht.put( "sub", new BinopInstruction() {
			public StackValue< ? > binop( StackValue< ? > x, StackValue< ? > y ) throws TypeException {
				return x.sub( y );
			}

			public int numArgs() {
				return 0;
			}
		} );

		ht.put( "mul", new BinopInstruction() {
			public StackValue< ? > binop( StackValue< ? > x, StackValue< ? > y ) throws TypeException {
				return x.mul( y );
			}
		} );

		ht.put( "div", new BinopInstruction() {
			public StackValue< ? > binop( StackValue< ? > x, StackValue< ? > y ) throws TypeException {
				return x.div( y );
			}
		} );

		ht.put( "const", new SeqInstruction() {
			public void apply( StackMachine m, List< StackValue< ? > > args ) throws StackRuntimeException {
				m.getStack().push( args.get( 0 ) );
			}

			public < T > List< Class< T > > argTypes() {
				return new ArrayList< Class< T > >( Arrays.asList( Integer.class, Character.class, String.class ) );
			}
		} );

		ht.put( "load", new SeqInstruction() {
			public void apply( StackMachine m, List< StackValue< ? > > args ) throws StackRuntimeException {
				m.getStack().push( m.getStore( ( Integer ) args.get( 0 ).getValue() ) );
			}

			public < T > List< Class< T > > argTypes() {
				/* return new ArrayList< Class< T > >( Arrays.asList( Integer.class ) ); */
				return ( List< Class< T > > ) Arrays.asList( Integer.class );
			}
		} );

		ht.put( "store", new SeqInstruction() {
			public void apply( StackMachine m, List< StackValue< ? > > args ) throws StackRuntimeException {
				m.setStore( ( Integer ) args.get( 0 ).getValue(), m.getStack().pop() );
			}

			public < T > List< Class< T > > argTypes() {
				return new ArrayList< Class< T > >( Arrays.asList( Integer.class ) );
			}
		} );

		ht.put( "label", new SeqInstruction() {
			public void apply( StackMachine m, List< StackValue< ? > > args ) throws StackRuntimeException {
			}

			public < T > List< Class< T > > argTypes() {
				return new ArrayList< Class< T > >( Arrays.asList( String.class ) );
			}
		} );
	}

	public static Instruction get( String name ) {
		if( !initialised ) {
			init();
		}

		return ht.get( name );
	}
}
