package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackValue;

public class AddInstruction extends BinopInstruction {

    @Override
    protected StackValue<?> binop(StackValue<?> x, StackValue<?> y) {
	return (x.add(y));
    }

}
