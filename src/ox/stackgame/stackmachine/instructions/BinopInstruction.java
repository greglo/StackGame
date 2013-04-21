package ox.stackgame.stackmachine.instructions;

import java.util.*;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.exceptions.TypeException;

public abstract class BinopInstruction extends Instruction {
    @Override
    public int execute( StackMachine machine, List< StackValue< ? > > args ) throws StackRuntimeException {
	StackValue<?> y = machine.getStack().pop();
	StackValue<?> x = machine.getStack().pop();
	try {
	    machine.getStack().push(binop(x, y));
	}
	catch (TypeException e) {
	    throw new TypeException(machine.getProgramCounter(), e.getType());
	}
	return machine.nextInstruction();
    }
    
    @Override
    public Instruction clone() {
	return this;
    }
    
    protected abstract StackValue<?> binop(StackValue<?> x, StackValue<?> y) throws StackRuntimeException;
}
