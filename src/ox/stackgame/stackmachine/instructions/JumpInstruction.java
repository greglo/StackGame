package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;

public class JumpInstruction<E> extends AbstractBranchInstruction<E> {

    public JumpInstruction(String label) {
	super(label);
    }

    @Override
    protected boolean p(StackMachine<E> machine) {
	return true;
    }
    
}
