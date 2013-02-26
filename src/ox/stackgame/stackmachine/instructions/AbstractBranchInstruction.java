package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;

public abstract class AbstractBranchInstruction<E> extends Instruction<E> {
    private final String label;
    
    public AbstractBranchInstruction(String label) {
	this.label = label;
    }
    
    @Override
    public void execute(StackMachine<E> machine) {
	if (p(machine))
	    machine.jumpToLabel(label);
	else
	    machine.incrProgramCounter();
    }

    @Override
    public Instruction<E> clone() {
	return this;
    }
    
    protected abstract boolean p(StackMachine<E> machine);

}
