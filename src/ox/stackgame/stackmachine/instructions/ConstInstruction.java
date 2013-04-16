package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.CharStackValue;
import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.ImmutableStackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public class ConstInstruction extends Instruction {
    private final StackValue<?> value;
    
    public ConstInstruction(StackValue<?> value) {
	this.value = value;
	
	if (value == null)
	    throw new IllegalArgumentException("You may not load null into the machine");
    }
    
    public ConstInstruction(int i) {
	this.value = new IntStackValue(i);
    }
    
    public ConstInstruction(char ch) {
	this.value = new CharStackValue(ch);
    }

    @Override
    public int execute(ImmutableStackMachine machine) throws StackRuntimeException {
	machine.getStack().push(value);
	return machine.nextInstruction();
    }

    @Override
    public Instruction clone() {
	return new ConstInstruction(value);
    }
    
}
