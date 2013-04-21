package ox.stackgame.stackmachine.instructions;

import java.util.*;
import java.lang.*;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

import ox.stackgame.stackmachine.StackValue;

public abstract class Operation {
    public abstract int execute( StackMachine machine, List< StackValue< ? > > args ) throws StackRuntimeException;

    public List< Class< ? > > argTypes() {
	    return null;
    }
}
