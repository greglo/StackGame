package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;

public class LabelInstruction extends Instruction {
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
    public int execute(StackMachine machine) {
	return machine.nextInstruction();
    }

    @Override
    public Instruction clone() {
	return this;
    }

}
