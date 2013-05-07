package ox.stackgame.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackValue;

public class TapeChallenge extends AbstractChallenge {
    
    protected String message = "";
    public final List<StackValue<?>> outputTape;
    protected final int bestNumSteps;
    protected final int bestProgramLength;
    

    public TapeChallenge(String title, String description,
            Map<String, Integer> instructionSet,
            ArrayList<StackValue<?>> startTape,
            ArrayList<StackValue<?>> finalTape, int bestNumSteps, int bestProgramLength) {

        super(title, description, instructionSet, startTape);
        this.outputTape = finalTape;
        this.bestNumSteps = bestNumSteps;
        this.bestProgramLength = bestProgramLength;
    }

    @Override
    public Boolean hasSucceeded(StackMachine m) {
        Boolean hs = m.getOutputTape().equals(outputTape);
        if (hs){
            message = "Solved!";
            int numSteps = m.getNumExecutionSteps()+1;
            int progLength = m.getInstructions().size();
            
            System.out.println("Their numSteps:"+numSteps+" their progLength:"+progLength);
            
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
        } else {
            message = "Not quite, try again";
        }
        //System.out.println("Machine: "+m.getOutputTape()+", Challenge: "+outputTape+". hasSucceeded returning: "+hs+", message: "+message);
        return hs;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
