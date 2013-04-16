package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.ImmutableStackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public class OutputInstruction extends Instruction {    

    @Override
    public int execute(ImmutableStackMachine machine) throws StackRuntimeException {
	StackValue<?> value = machine.getStack().pop();
	machine.output(value);
	return machine.nextInstruction();
    }

    @Override
    public Instruction clone() {
	return this;
    }
    
}
