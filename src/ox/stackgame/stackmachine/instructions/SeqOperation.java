package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

import ox.stackgame.stackmachine.StackValue;

public abstract class SeqOperation extends Operation {
    public int execute(StackMachine m, StackValue<?> arg) throws StackRuntimeException {
	apply(m, arg);
	return m.nextInstruction();
    }

    public abstract void apply(StackMachine m, StackValue<?> arg) throws StackRuntimeException;
}
