package ox.stackgame.stackmachine.instructions;

import java.util.*;
import java.lang.*;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

import ox.stackgame.stackmachine.StackValue;

public abstract class SeqOperation extends Operation {
	public int execute( StackMachine m, List< StackValue< ? > > args ) throws StackRuntimeException {
		apply( m, args );

		return m.nextInstruction();
	}

	public abstract void apply( StackMachine m, List< StackValue< ? > > args ) throws StackRuntimeException;
}
