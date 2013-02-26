package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;

public abstract class Instruction<E> {
    public abstract void execute(StackMachine<E> machine);
    public abstract Instruction<E> clone();
}
