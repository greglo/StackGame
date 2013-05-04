/**
 * 
 */
package ox.stackgame.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.EmptyStackException;

/**
 * @author danfox
 * 
 */
public class StackAndTapeChallenge extends StackResultChallenge {

    public final List<StackValue<?>> inputTape;
    public final List<StackValue<?>> outputTape;

    public StackAndTapeChallenge(String title, String description,
            Map<String, Integer> instructionSet,
            StackValue<?> correctAnswer, ArrayList<StackValue<?>> inputTape,
            ArrayList<StackValue<?>> outputTape) {
        
        super(title, description, instructionSet, correctAnswer);
        this.inputTape = inputTape;
        this.outputTape = outputTape;
    }

    @Override
    /**
     * Returns the appropriate boolean, sets this.message appropriately.
     */
    public Boolean hasSucceeded(StackMachine m) {
        //  TODO check stack
        
        try {
            if (m.getStack().peek().equals(correctAnswer)) {
                
            } else {
                this.message= "Your answer: " + m.getStack().peek().toString()
                        + "; correct answer: " + correctAnswer.toString();
                
            }
        } catch (EmptyStackException e) {
            message = "Empty stack"; 
            return false;
        }
        
        // TODO check tape
        
        return null;
    }

}
