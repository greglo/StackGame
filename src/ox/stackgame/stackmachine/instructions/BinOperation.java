package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.exceptions.TypeException;

public abstract class BinOperation extends Operation {
    @Override
    public int execute(StackMachine machine, StackValue<?> arg) throws StackRuntimeException {
	StackValue<?> y = machine.getStack().pop();
	StackValue<?> x = machine.getStack().pop();
	try {
	    machine.getStack().push(binop(x, y));
	} catch (TypeException e) {
	    throw new TypeException(machine.getProgramCounter(), e.getType());
	}
	return machine.nextInstruction();
    }

    protected abstract StackValue<?> binop(StackValue<?> x, StackValue<?> y) throws StackRuntimeException;
}
