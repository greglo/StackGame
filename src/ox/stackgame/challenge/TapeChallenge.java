/**
 * 
 */
package ox.stackgame.challenge;

import java.util.List;
import java.util.Map;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * @author danfox
 * 
 */
public class TapeChallenge extends StackResultChallenge {

    public final List<StackValue<?>> inputTape;
    public final List<StackValue<?>> outputTape;

    TapeChallenge(String title, String description,
            Map<Instruction, Integer> instructionSet,
            StackValue<?> correctAnswer, List<StackValue<?>> inputTape,
            List<StackValue<?>> outputTape) {
        super(title, description, instructionSet, correctAnswer);
        this.inputTape = inputTape;
        this.outputTape = outputTape;
        // TODO Auto-generated constructor stub
    }

    @Override
    /**
     * Returns the appropriate boolean, sets this.message appropriately.
     */
    public Boolean hasSucceeded(StackMachine m) {
        // TODO Auto-generated method stub
        return null;
    }

}
