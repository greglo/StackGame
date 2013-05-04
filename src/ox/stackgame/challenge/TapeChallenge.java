package ox.stackgame.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.instructions.Instruction;

public class TapeChallenge extends AbstractChallenge {
    
    protected String message = "";
    public final List<StackValue<?>> inputTape;
    public final List<StackValue<?>> outputTape;

    public TapeChallenge(String title, String description,
            Map<Instruction, Integer> instructionSet,
            ArrayList<StackValue<?>> startTape,
            ArrayList<StackValue<?>> finalTape) {

        super(title, description, instructionSet);
        this.inputTape = startTape;
        this.outputTape = finalTape;
    }

    public Boolean hasSucceeded(StackMachine m) {
        // TODO make stackMachine's output tape accessible.
        //Boolean hs = m.getTape().equals(outputTape)
        //message = hs ? "Congratulations" : "Not quite, try again";
        return null;
    }

    public String getMessage() {
        return this.message;
    }

}
