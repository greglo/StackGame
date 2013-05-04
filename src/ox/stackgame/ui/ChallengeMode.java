/**
 * 
 */
package ox.stackgame.ui;

import java.util.*;

import ox.stackgame.challenge.AbstractChallenge;
import ox.stackgame.challenge.StackResultChallenge;
import ox.stackgame.stackmachine.CharStackValue;
import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.exceptions.InvalidCharException;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * Allows the user to create a program to solve a specific challenge (limited
 * instruction set, well defined success conditions, specified max stack size).
 * 
 * @author danfox
 * @author rgossiaux
 * 
 */
public class ChallengeMode extends DesignMode {
    public static final List<AbstractChallenge> challengeList = new LinkedList<AbstractChallenge>();
    private AbstractChallenge currChallenge = null;

    public void accept(ModeVisitor v) {
        v.visit(this);
    }

    /**
     * 
     * @return the current challenge, or null if nothing is selected.
     */
    public AbstractChallenge getChallenge() {
        return this.currChallenge;
    }

    public void setChallenge(AbstractChallenge challenge) {
        this.currChallenge = challenge;
    }

    @SuppressWarnings("serial")
    public ChallengeMode() {
        List<AbstractChallenge> cl = challengeList;

        // initialise challengeList
        // HashMap<Instruction,Integer> instructionSet = new
        // HashMap<Instruction,Integer>();
        // instructionSet.put(new Instruction("const", new IntStackValue(1)),
        // -1);
        // instructionSet.put(new Instruction("add"), -1);
        // challengeList.add(new StackResultChallenge("demo1",
        // "Use only 'const 1' and 'add' instructions to produce 5 on the stack.",
        // instructionSet, new IntStackValue(5) ));
        //
        //
        //
        // challengeList.add(
        // new StackResultChallenge(
        // "demo2",
        // "Combine 'const 3', 'const 4', 'add' and 'div' to produce 5 on the stack.",
        // new HashMap<Instruction,Integer>() { {
        // put(new Instruction("const", new IntStackValue(4)), -1);
        // put(new Instruction("const", new IntStackValue(3)), -1);
        // put(new Instruction("add"), -1);
        // put(new Instruction("div"), -1);
        // }},
        // new IntStackValue(5)
        // )
        // );

        cl.add(new StackResultChallenge(
                "my first program is balling",
                "An arithmetic instruction pops two values from the stack, performs some operation and pushes the result back. An example of such an instruction is ADD, which does exactly what you think it does. CONST simply pushes the value after it onto the stack.",
                new HashMap<Instruction, Integer>() {
                    {
                        put(new Instruction("const", new IntStackValue(1)), 1);
                        put(new Instruction("const", new IntStackValue(2)), 1);
                        put(new Instruction("add"), 1);
                    }
                }, new IntStackValue(3)));
        

        try {
            cl.add(new StackResultChallenge(
                    "hey it works with letters too",
                    "fo reals",
                    new HashMap<Instruction, Integer>() {
                        {
                            put(new Instruction("const", new CharStackValue('a')), 1);
                            put(new Instruction("const", new IntStackValue(1)), 1);
                            put(new Instruction("add"), 1);
                        }
                    }, new CharStackValue('b')));
        } catch (InvalidCharException e) {
            // initialised it wrong.
            e.printStackTrace();
        }
    }

}
