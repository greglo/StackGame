/**
 * 
 */
package ox.stackgame.ui;

import java.util.*;

import ox.stackgame.challenge.AbstractChallenge;
import ox.stackgame.challenge.StackResultChallenge;
import ox.stackgame.challenge.StackAndTapeChallenge;
import ox.stackgame.challenge.TapeChallenge;
import ox.stackgame.stackmachine.CharStackValue;
import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.StringStackValue;
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
        
        ArrayList<StackValue<?>> tape1to100 = new ArrayList<StackValue<?>>(){{
            for (int i=1;i<=100;i++) add(new IntStackValue(i));
        }};
        
        // simple Challenge
        cl.add(new StackResultChallenge(
                "my first program is balling",
                "An arithmetic instruction pops two values from the stack, performs some operation and pushes the result back. An example of such an instruction is ADD, which does exactly what you think it does. CONST simply pushes the value after it onto the stack.",
                new HashMap<String, Integer>() {
                    {
                        put("const 1", 1);
                        put("const 2", 1);
                        put("add", 1);
                    }
                }, new IntStackValue(3)));

        // simple Challenge
        try {
            cl.add(new StackResultChallenge("hey it works with letters too",
                    "fo reals", new HashMap<String, Integer>() {
                        {
                            put("const a", 1); // TODO check this is the correct way to use 
                            put("const 1", 1);
                            put("add", 1);
                        }
                    }, new CharStackValue('b')));
        } catch (InvalidCharException e) {
            // initialised it wrong.
            e.printStackTrace();
        }

        // simple Challenge
        cl.add(new StackResultChallenge(
                "operator precedence is a crutch for bads",
                "Write a program to compute (4 x 5) - (6 / 2). Notice we are missing a bracket instruction. HMMMMMMMMMMMMMM",
                new HashMap<String, Integer>() {
                    {
                        put("const 3", 1);
                        put("const 4", 1);
                        put("const 5", 1);
                        put("const 6", 1);
                        put("mul", 1);
                        put("sub", 1);
                        put("div", 1);
                    }
                }, new IntStackValue(17)));
        
        // tape challenge
        // TODO improve this challenge. it can currently be solved by the empty program...
        cl.add(new TapeChallenge(
                "tapes",
                "Our computer is bleeding edge and has TWO data streams for you to play with input and output. INPUT and OUTPUT read/write one value from/to their respective streams.",
                new HashMap<String, Integer>() {
                    {
                        put("input", 1);
                        put("output", 1);
                    }
                }, new ArrayList<StackValue<?>>() {
                    {
                        add(new IntStackValue(1));
                    }
                }, new ArrayList<StackValue<?>>() {
                    {
                        add(new IntStackValue(1));
                    }
                }));
        
        // why don't we BRANCH OUT?!?!?!
        cl.add(new TapeChallenge(
                "why don't we BRANCH OUT?!?!?!",
                "Today, the input stream contains all the numbers from 1 to 100. Copy it all to output using four instructions.",
                new HashMap<String, Integer>() {
                    {
                        put("input", 1);
                        put("output", 1);
                        put("jii *", 1);
                        put("label *", 1);
                    }
                }, new ArrayList<StackValue<?>>() {
                    {
                        add(new IntStackValue(1));
                    }
                }, tape1to100)); 
//        *Obviously if streaming was limited to the last level it would be beyond
//        worthless, which is why we've helpfully provided an instruction to check if
//        there's anything left on the input stream. This brings us to the idea of
//        branching. There are several instructions which will test something about the
//        machine and make the program jump to a "label" on success, or fall through to
//        the next instruction if it fails. The one we are introducing here is JII (jump
//        if input), which will jump to a given label if there is anything left on the
//        input stream.
//
//        label loop
//        input
//        output
//        jii loop


        // more branching
        cl.add(new TapeChallenge(
                "more branching",
                "JNEZ/JEZ (jump [not] equal zero) are instructions that pop a value off the stack, compare it with 0 and branch if they are not equal/equal respectively. JUMP jumps all the time. Output all the numbers from 1 to 100. Hint: a == b is the same as ( a - b ) == 0",
                new HashMap<String, Integer>() {
                    {
                        put("const 1", null);
                        put("const 100", null);
                        put("add", null);
                        put("sub", null);
                        put("jump *", null);  //PROBLEMS
                        put("jez *", null);
                        put("jnez *", null);
                        put("add", null);
                    }
                }, new ArrayList<StackValue<?>>(), tape1to100));  
//
//        ops: const 1, const 100, add, sub, jump, jez, jnez, label, output, dup
//        in:
//        out: [1..100]
//
//        *JNEZ/JEZ (jump [not] equal zero) are instructions that pop a value off the
//        stack, compare it with 0 and branch if they are not equal/equal respectively.
//        JUMP jumps all the time. Output all the numbers from 1 to 100.
//
//        Hint: a == b is the same as ( a - b ) == 0*
//
//        const 1
//        label loop
//        dup
//        const 100
//        sub
//        jez end
//        output
//        const 1
//        add
//        jump loop

        // storegame
        cl.add(new TapeChallenge(
                "storegame",
                "Being caring and thoughtful the authors have made the extra effort to give users four boxes for storing intermediate results in. LOAD/STORE need to know which box you are accessing and otherwise do exactly what you think they do. Using this, take the pair of numbers from input and output them backwards.",
                new HashMap<String, Integer>() {
                    {
                        put("load *", null); 
                        put("store *", null);
                        put("input", null);
                        put("output", null);
                    }
                },  new ArrayList<StackValue<?>>() { 
                    {
                        add(new IntStackValue(1));
                        add(new IntStackValue(2));
                    }
                }, new ArrayList<StackValue<?>>() {
                    {
                        add(new IntStackValue(2));
                        add(new IntStackValue(1));
                    }
                }));
//
//        ops: load, store, input, output
//        in: 1 2
//        out: 2 1
//
//        Being caring and thoughtful the authors have made the extra effort to give
//        users four boxes for storing intermediate results in. LOAD/STORE need to know
//        which box you are accessing and otherwise do exactly what you think they do.
//        Using this, take the pair of numbers from input and output them backwards.
//
//        input
//        store 1
//        input
//        output
//        load 1
//        output
        
    }

}
