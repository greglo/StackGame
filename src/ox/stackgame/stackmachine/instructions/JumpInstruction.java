package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;

public class JumpInstruction extends AbstractBranchInstruction {

    public JumpInstruction(String label) {
	super(label);
    }

    @Override
    protected boolean p(StackMachine machine) {
	return true;
    }
    
}
