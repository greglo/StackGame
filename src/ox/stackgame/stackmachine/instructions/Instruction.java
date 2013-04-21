package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

public abstract class Instruction {
    public abstract int execute(StackMachine machine) throws StackRuntimeException;
    public abstract Instruction clone();
    public abstract int numArgs();
}
