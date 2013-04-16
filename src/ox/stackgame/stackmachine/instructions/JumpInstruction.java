package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.ImmutableStackMachine;

public class JumpInstruction extends AbstractBranchInstruction {

    public JumpInstruction(String label) {
	super(label);
    }

    @Override
    protected boolean p(ImmutableStackMachine machine) {
	return true;
    }
    
}
