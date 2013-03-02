package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;

public class ConstInstruction extends Instruction {
    private final StackValue<?> value;
    
    public ConstInstruction(StackValue<?> value) {
	this.value = value;
	
	if (value == null)
	    throw new IllegalArgumentException("You may not load null into the machine");
    }

    @Override
    public int execute(StackMachine machine) {
	machine.getStack().push(value);
	return machine.nextInstruction();
    }

    @Override
    public Instruction clone() {
	return new ConstInstruction(value);
    }
    
}
