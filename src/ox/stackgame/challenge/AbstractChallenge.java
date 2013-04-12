/**
 * 
 */
package ox.stackgame.challenge;

import java.util.Collection;

import ox.stackgame.stackmachine.*;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * @author danfox
 * A representation of the concept of a 'challenge', involving instructions, success conditions etc.
 */
public abstract class AbstractChallenge {
	public final String description;
    public final int stackSize; // =200
    public final int maxInstructions; // = 10000;
    public final int storeSize;// = 4;
    public final Collection<Class<? extends Instruction>> allowedInstructions; 
	
    /**
     * Create a new challenge.
     * @param description Plaintext string explaining to the user what the aim is (e.g. Divide [expr] by [expr] with a stack size of x.)  Could include hints.
     * @param allowedInstructions A collection of allowed types of instruction for this challenge.  Send null to allow all instructions.
     * @param stackSize
     * @param maxInstructions
     * @param storeSize
     */
	AbstractChallenge (String description, Collection<Class<? extends Instruction>> allowedInstructions, 
			int stackSize, int maxInstructions, int storeSize){
		this.description = description;
		this.allowedInstructions = allowedInstructions;
		// TODO add a constuctor to StackMachine that allows these to be set.
		this.stackSize = stackSize;
		this.maxInstructions = maxInstructions;
		this.storeSize = storeSize;
	}
	
	/**
	 * Determine whether the supplied machine meets the success criteria for the challenge.
	 * @param m a StackMachine to test.
	 * @return "" if the machine has passed, a message otherwise.  Note, the failure message could include a helpful tip.
	 */
	public abstract String hasSucceeded(StackMachine m);
}
