package ox.stackgame.challenge;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.EmptyStackException;

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

    protected final StackValue<?> correctAnswer;
    protected String message = "";
    protected Integer bestNumSteps = null;
    protected Integer bestProgramLength = null;

    public StackResultChallenge(String title, String description,
            Map<String, Integer> instructionSet,
            StackValue<?> correctAnswer, List<StackValue<?>> inputs) {
        super(title, description, instructionSet, inputs);
        this.correctAnswer = correctAnswer;
    }

    public StackResultChallenge(String title, String description,
            HashMap<String, Integer> instructionSet, IntStackValue correctAnswer,
            LinkedList<StackValue<?>> inputs, int bestNumSteps, int bestProgramLength) {
        super(title, description, instructionSet, inputs);
        this.correctAnswer = correctAnswer;
        this.bestNumSteps = bestNumSteps;
        this.bestProgramLength = bestProgramLength;
    }

    @Override
    /**
     * Returns the appropriate boolean, sets this.message appropriately.
     */
    public Boolean hasSucceeded(StackMachine m) {
        try {
            if (m.getStack().peek().equals(correctAnswer)) {
                message = "Solved!";
                if (bestNumSteps!=null && bestProgramLength!=null){
                    int numSteps = m.getNumExecutionSteps()+1;
                    int progLength = m.getInstructions().size();
                    
                    if (numSteps > bestNumSteps){
                        message = "Solved! How about with " + (numSteps - bestNumSteps) + " fewer steps?";
                    }
                    if (m.getInstructions().size() > bestProgramLength){
                        message = "Solved! How about with " + (progLength - bestProgramLength) + " fewer instruction?"; 
                    }
                    if (numSteps <= bestNumSteps && progLength <= bestProgramLength){
                        message = "Solved! (You beat the designer too!)";
                    }
                    if (numSteps == bestNumSteps && progLength == bestProgramLength){
                        message = "Perfect Solution!";
                    }
                }
                return true;
            } else {
                message= "Your answer: " + m.getStack().peek().toString()
                        + "; correct answer: " + correctAnswer.toString();
                return false;
            }
        } catch (EmptyStackException e) {
            message = "Empty stack";
            return false;
        }
    }

    public String getMessage() {
        return this.message;
    }

}
