package ox.stackgame.stackmachine.instructions;

import java.util.*;

import ox.stackgame.stackmachine.StackValue;

public class Instruction {
	public final String name;
	public final List< StackValue< ? > > args;

	public Instruction( String name, List< StackValue< ? > > args ) {
		assert name != null : "name == null";
		assert Operations.get( name ) != null : name + " isnt a real instruction";

		this.name = name;
		this.args = args;
	}
}
