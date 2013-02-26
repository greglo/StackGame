package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;

public abstract class BinopInstruction<E> extends Instruction<E> {
    @Override
    public void execute(StackMachine<E> machine) {
	E x = machine.getStack().pop();
	E y = machine.getStack().pop();
	machine.getStack().push(binop(x, y));
	machine.incrProgramCounter();
    }
    
    @Override
    public Instruction<E> clone() {
	return this;
    }
    
    protected abstract E binop(E x, E y);
}
