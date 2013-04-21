/**
 * 
 */
package ox.stackgame.challenge;

import java.util.Map;

import ox.stackgame.stackmachine.*;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * @author rgossiaux
 * A representation of the concept of a 'challenge', involving instructions, success conditions etc.
 */
public abstract class AbstractChallenge {
	public final String description;
    protected Map<Instruction, Integer> instructionSet;
    
	
    /**
     * Create a new challenge.
     * @param description Plaintext string explaining to the user what the aim is (e.g. Divide [expr] by [expr] with a stack size of x.)  Could include hints.
     * @param instructionSet A map from instructions to the number of that instruction permitted in the challenge; -1 for infinite 
     */
	AbstractChallenge (String description, Map<Instruction, Integer> instructionSet){
		this.description = description;
		this.instructionSet = instructionSet;
	}
	
	/**
	 * Determine whether the supplied machine meets the success criteria for the challenge.
	 * @param m a StackMachine to test.
	 * @return "" if the machine has passed, a message otherwise.  Note, the failure message could include a helpful tip.
	 */
	public abstract String hasSucceeded(StackMachine m);
}
