package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public class LoadInstruction extends Instruction {
    private final int address;
    
    public LoadInstruction(int address) {
	this.address = address;
    }
    
    @Override
    public int execute(StackMachine machine) throws StackRuntimeException {
	StackValue<?> val = machine.getStore(address);
	machine.getStack().push(val);
	return machine.nextInstruction();
    }

    @Override
    public Instruction clone() {
	return this;
    }

}
