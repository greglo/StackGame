package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public abstract class BinopInstruction extends Instruction {
    @Override
    public int execute(StackMachine machine) throws StackRuntimeException {
	StackValue<?> x = machine.getStack().pop();
	StackValue<?> y = machine.getStack().pop();
	machine.getStack().push(binop(x, y));
	return machine.nextInstruction();
    }
    
    @Override
    public Instruction clone() {
	return this;
    }
    
    protected abstract StackValue<?> binop(StackValue<?> x, StackValue<?> y) throws StackRuntimeException;
}
