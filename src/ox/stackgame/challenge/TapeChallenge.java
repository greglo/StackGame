package ox.stackgame.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;

public class TapeChallenge extends AbstractChallenge {
    
    protected String message = "";
    public final List<StackValue<?>> inputTape;
    public final List<StackValue<?>> outputTape;

    public TapeChallenge(String title, String description,
            Map<String, Integer> instructionSet,
            ArrayList<StackValue<?>> startTape,
            ArrayList<StackValue<?>> finalTape) {

        super(title, description, instructionSet);
        this.inputTape = startTape;
        this.outputTape = finalTape;
    }

    @Override
    public Boolean hasSucceeded(StackMachine m) {
        Boolean hs = m.getOutputTape().equals(outputTape);
        this.message = hs ? "Congratulations" : "Not quite, try again";
        System.out.println("Machine: "+m.getOutputTape()+", Challenge: "+outputTape+". hasSucceeded returning: "+hs+", message: "+message);
        return hs;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
