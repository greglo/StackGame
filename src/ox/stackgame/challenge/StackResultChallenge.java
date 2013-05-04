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
 * @author iamdanfox
 * 
 */
public class StackResultChallenge extends AbstractChallenge {

    private final StackValue<?> correctAnswer;
    private String message = "";

    public StackResultChallenge(String title, String description,
            Map<Instruction, Integer> instructionSet,
            StackValue<?> correctAnswer) {
        super(title, description, instructionSet);
        this.correctAnswer = correctAnswer;
    }

    @Override
    /**
     * Returns the appropriate boolean, sets this.message appropriately.
     */
    public Boolean hasSucceeded(StackMachine m) {
        try {
            if (m.getStack().peek().equals(correctAnswer)) {
                return true;
            } else {
                message= "Your answer: " + m.getStack().peek().toString()
                        + "; correct answer: " + correctAnswer.toString();
                return false;
            }
        } catch (EmptyStackException e) {
            message = "Empty stack"; // TODO make this more descriptive?
            return false;
        }
    }

    public String getMessage() {
        return this.message;
    }

}
