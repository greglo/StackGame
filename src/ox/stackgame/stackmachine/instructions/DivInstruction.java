package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.TypeException;

public class DivInstruction extends BinopInstruction {

    @Override
    protected StackValue<?> binop(StackValue<?> x, StackValue<?> y) throws TypeException {
	return (x.div(y));
    }

}
