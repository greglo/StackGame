package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackMachine;

public class ConstInstruction<E> extends Instruction<E> {
    private final E value;
    
    public ConstInstruction(E value) {
	this.value = value;
	
	if (value == null)
	    throw new IllegalArgumentException("You may not load null into the machine");
    }

    @Override
    public void execute(StackMachine<E> machine) {
	machine.getStack().push(value);
	machine.incrProgramCounter();
    }

    @Override
    public Instruction<E> clone() {
	return new ConstInstruction<E>(value);
    }
    
}
