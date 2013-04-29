package ox.stackgame.challenge;

import java.util.Map;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.EmptyStackException;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * A simple type of challenge that simply presents a problem and then allows the
 * user to use any tools at their disposal to solve it, basing success only on
 * whether they leave the Stack in the correct state. (NB this only checks the
 * top value on the stack).
 * 
 * @author rgossiaux
 * 
 */
public class StackResultChallenge extends AbstractChallenge {

    private final StackValue<?> correctAnswer;

    public StackResultChallenge(String description,
            Map<Instruction, Integer> instructionSet,
            StackValue<?> correctAnswer) {
        super(description, instructionSet);
        this.correctAnswer = correctAnswer;
    }

    @Override
    /**
     * Returns "Your answer: x; correct answer: y" if the provided answer is wrong and "Empty stack" if stack is empty
     */
    public String hasSucceeded(StackMachine m) {
        try {
            if (m.getStack().peek().equals(correctAnswer)) {
                return "";
            } else {
                return "Your answer: " + m.getStack().peek().toString()
                        + "; correct answer: " + correctAnswer.toString();
            }
        } catch (EmptyStackException e) {
            return "Empty stack";
        }
    }

}
