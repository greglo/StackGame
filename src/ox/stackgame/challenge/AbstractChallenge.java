/**
 * 
 */
package ox.stackgame.challenge;

import java.util.Map;

import ox.stackgame.stackmachine.*;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * A representation of the concept of a 'challenge', involving instructions,
 * success conditions etc.
 * 
 * @author rgossiaux
 * @author iamdanfox
 */
public abstract class AbstractChallenge {
    public final String description;
    public final String title;
    public final Map<Instruction, Integer> instructionSet;

    /**
     * Create a new challenge.
     * 
     * @param title
     * @param description
     *            Plaintext string explaining to the user what the aim is (e.g.
     *            Divide [expr] by [expr] with a stack size of x.) Could include
     *            hints.
     * @param instructionSet
     *            A map from instructions to the number of that instruction
     *            permitted in the challenge; -1 for infinite
     */
    AbstractChallenge(String title, String description,
            Map<Instruction, Integer> instructionSet) {
        this.title = title;
        this.description = description;
        this.instructionSet = instructionSet;
    }

    /**
     * Determine whether the supplied machine meets the success criteria for the
     * challenge.
     * 
     * @param m
     *            a StackMachine to test.
     * @return true if the machine has passed, false otherwise.
     */
    public abstract Boolean hasSucceeded(StackMachine m);

    /**
     * 
     * @return a message to be displayed by the UI. Either a helpful tip when
     *         the user fails or perhaps a congratulatory message if the user
     *         gets the optimal program
     */
    public abstract String getMessage();
}
