package ox.stackgame.stackmachine.instructions;

public class IntegerSubInstruction extends BinopInstruction<Integer> {

    @Override
    protected Integer binop(Integer x, Integer y) {
	if (y != 0)
	    return x / y;
	else
	    return 0;
	// TODO Something better here
    }

}
