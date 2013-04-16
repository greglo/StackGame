package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.ImmutableStackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public class InputInstruction extends Instruction {    

    @Override
    public int execute(ImmutableStackMachine machine) throws StackRuntimeException {
	StackValue<?> value = machine.consumeInput();
	machine.getStack().push(value);
	return machine.nextInstruction();
    }

    @Override
    public Instruction clone() {
	return this;
    }
    
}
