package ox.stackgame.stackmachine.instructions;

public class IntegerMulInstruction extends BinopInstruction<Integer> {

    @Override
    protected Integer binop(Integer x, Integer y) {
	return x * y;
    }

}
