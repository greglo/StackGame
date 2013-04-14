package ox.stackgame.challenge;

import java.util.HashSet;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * A simple type of challenge that simply presents a problem and then allows the user to use any tools at their
 * disposal to solve it, basing success only on whether they leave the Stack in the correct state. (NB this checks
 * the entire stack, not just the top value - possibly should be changed).
 * @author danfox
 *
 */
public class StackResultChallenge extends AbstractChallenge {
	
	private final EvaluationStack correctAnswer;
	
	public StackResultChallenge(String description, EvaluationStack correctAnswer) {				
		super(description, new HashSet<Class<? extends Instruction>> (), StackMachine.STACK_SIZE, StackMachine.MAX_INSTRUCTIONS);
		this.correctAnswer = correctAnswer;
	}

	@Override
	public String hasSucceeded(StackMachine m) {
		// Compare just top value:
		//if (m.getStack().peek().equals(correctAnswer.peek())) {
		// Compare entire stack:
		if (m.getStack().equals(correctAnswer)){
			return "";
		} else {
			return "Not quite, try again";
		}
	}

}
