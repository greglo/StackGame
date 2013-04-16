package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.ImmutableStackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public abstract class AbstractBranchInstruction extends Instruction {
    private final String label;
    
    public AbstractBranchInstruction(String label) {
	this.label = label;
    }
    
    @Override
    public int execute(ImmutableStackMachine machine) throws StackRuntimeException {
	if (p(machine))
	    return machine.getLabelLine(label);
	else
	    return machine.nextInstruction();
    }

    @Override
    public Instruction clone() {
	return this;
    }
    
    protected abstract boolean p(ImmutableStackMachine machine);

}
