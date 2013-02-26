package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;

public class LabelInstruction<E> extends Instruction<E> {
    private final String identifier;
    
    public LabelInstruction(String identifier) {
	this.identifier = identifier;
	if (identifier == null)
	    throw new IllegalArgumentException("Label identifier cannot be null");
    }
    
    public String getIdentifier() {
	return identifier;
    }
    
    @Override
    public void execute(StackMachine<E> machine) {
	machine.incrProgramCounter();
    }

    @Override
    public Instruction<E> clone() {
	return this;
    }

}
