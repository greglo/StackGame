/**
 * 
 */
package ox.stackgame.challenge;

import java.util.HashMap;
import java.util.List;
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
    public final Map<String, Integer> instructionSet;

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
    public AbstractChallenge(String title, String description, Map<String, Integer> instructionSet) {
        this.title = title;
        this.description = description;
        this.instructionSet = instructionSet;
    }

    public Boolean checkProgram(List<Instruction> instructions) {
        Map<String, Integer> used = new HashMap<String, Integer>();

        System.out.println("Checking machine against challenge's instructionSet (checkProgram)");

        // check each instruction
        if (instructionSet != null)
            for (Instruction i : instructions) {
                String line = i.toString();
                String starredLine = i.name + " *";

                if (instructionSet.containsKey(line)) { // entry exists for this
                                                        // specific instruction
                    used.put(line, used.containsKey(line) ? used.get(line) + 1 : 1);
                    // if this line exceeds a non-null specification, return
                    // false
                    if (instructionSet.get(line) != null && used.get(line) > instructionSet.get(line))
                        return false;
                } else if (instructionSet.containsKey(starredLine)) {
                    used.put(starredLine, used.containsKey(starredLine) ? used.get(starredLine) + 1 : 1);
                    // if this line exceeds a non-null specification, return
                    // false
                    if (instructionSet.get(starredLine) != null && used.get(starredLine) > instructionSet.get(starredLine))
                        return false;
                } else {
                    // instruction is not allowed
                    return false;
                }
            }
        return true;
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
