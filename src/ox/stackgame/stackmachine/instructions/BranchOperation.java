package ox.stackgame.stackmachine.instructions;

import java.util.*;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.StringStackValue;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public abstract class BranchOperation extends Operation {
    @Override
    public int execute(StackMachine machine, StackValue<?> arg) throws StackRuntimeException {
        assert arg != null;

        if (p(machine)) {
            String label = ((StringStackValue) arg).getValue();
            return machine.getLabelLine(label) + 1;
        } else
            return machine.nextInstruction();
    }

    @Override
    public List<Class<?>> argTypes() {
        return Operations.typeList(StringStackValue.class);
    }

    protected abstract boolean p(StackMachine machine) throws StackRuntimeException;
}
