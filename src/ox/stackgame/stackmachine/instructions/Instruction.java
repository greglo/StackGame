package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.ImmutableStackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public abstract class Instruction {
    public abstract int execute(ImmutableStackMachine machine) throws StackRuntimeException;
    public abstract Instruction clone();
}
