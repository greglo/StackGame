package ox.stackgame.stackmachine.instructions;

import java.util.List;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.StringStackValue;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public abstract class BranchOperation extends Operation {

    @Override
    public int execute(StackMachine machine, List<StackValue<?>> args) throws StackRuntimeException {
	assert args.size() > 0;
	assert args.get(0) instanceof StringStackValue;

	if (p(machine)) {
	    String label = ((StringStackValue) args.get(0)).getValue();
	    return machine.getLabelLine(label);
	} else
	    return machine.nextInstruction();
    }

    protected abstract boolean p(StackMachine machine);

}
