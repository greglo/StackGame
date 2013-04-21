package ox.stackgame.stackmachine.instructions;

import java.util.*;

import ox.stackgame.stackmachine.StackValue;

public class Operation {
	public final String name;
	public final List< StackValue< ? > > args;

	public Operation( String name, List< StackValue< ? > > args ) {
		assert name != null : "name == null";
		assert Instructions.get( name ) != null : name + " isnt a real instruction";

		this.name = name;
		this.args = args;
	}
}
