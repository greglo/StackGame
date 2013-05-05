/**
 * 
 */
package ox.stackgame.ui;

import java.util.*;

import ox.stackgame.challenge.AbstractChallenge;
import ox.stackgame.challenge.StackResultChallenge;
import ox.stackgame.challenge.TapeChallenge;
import ox.stackgame.stackmachine.CharStackValue;
import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.InvalidCharException;

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
        System.out.println("Set current challenge: " + challenge.title);
        this.currChallenge = challenge;
    }

    @SuppressWarnings("serial")
    public ChallengeMode() {
        List<AbstractChallenge> cl = challengeList;

        ArrayList<StackValue<?>> tape1to100 = new ArrayList<StackValue<?>>() {
            {
                for (int i = 1; i <= 100; i++)
                    add(new IntStackValue(i));
            }
        };

        cl.add(new StackResultChallenge("CONST", 
                "The CONST instruction <strong>pushes</strong> the given value onto the stack"
                + "<br/>"
                + "<br/>Use a CONST instruction to push 1 onto the stack",
                new HashMap<String, Integer>() {
                    {
                        put("const *", 1);
                    }
                }, new IntStackValue(1)));
        
        cl.add(new StackResultChallenge("Arithmetic Operations", 
                "Binary arithmetic operations <strong>pop</strong> two values off the stack, and <strong>push</strong> the result of the operation back"
                + "<br/>"
                + "<br/>Use the following instructions to get <strong>3</strong> on the stack",
                new HashMap<String, Integer>() {
                    {
                        put("const 1", 1);
                        put("const 2", 1);
                        put("add", 1);
                    }
                }, new IntStackValue(3)));

        cl.add(new StackResultChallenge("Reverse Polish",
                "There is no way to bracket your programs, so find a way to compute 1 + (2 * 3) = 7 on the stack",
                new HashMap<String, Integer>() {
                    {
                        put("const 1", 1);
                        put("const 2", 1);
                        put("const 3", 1);
                        put("add", 1);
                        put("mul", 1);
                    }
                }, new IntStackValue(6)));

        cl.add(new TapeChallenge("The tape",
                "The machine has two tapes you can use, an input tape and an output tape.<br/>"
                + "The input tape is used to pass information to your program. The INPUT instruction will take the first value from the input tape, and push it on the stack."
                + "<br/>The output tape starts empty. The OUTPUT instruction will pop a value off the stack, and append it to the output tape."
                + "<br/>"
                + "<br/>Use an INPUT and an OUTPUT instruction to move 1 from the input tape to the output tape",
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
        cl.add(new TapeChallenge("why don't we BRANCH OUT?!?!?!",
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
        // *Obviously if streaming was limited to the last level it would be
        // beyond
        // worthless, which is why we've helpfully provided an instruction to
        // check if
        // there's anything left on the input stream. This brings us to the idea
        // of
        // branching. There are several instructions which will test something
        // about the
        // machine and make the program jump to a "label" on success, or fall
        // through to
        // the next instruction if it fails. The one we are introducing here is
        // JII (jump
        // if input), which will jump to a given label if there is anything left
        // on the
        // input stream.
        //
        // label loop
        // input
        // output
        // jii loop

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
                        put("jump *", null); // PROBLEMS
                        put("jez *", null);
                        put("jnez *", null);
                        put("add", null);
                    }
                }, new ArrayList<StackValue<?>>(), tape1to100));
        //
        // ops: const 1, const 100, add, sub, jump, jez, jnez, label, output,
        // dup
        // in:
        // out: [1..100]
        //
        // *JNEZ/JEZ (jump [not] equal zero) are instructions that pop a value
        // off the
        // stack, compare it with 0 and branch if they are not equal/equal
        // respectively.
        // JUMP jumps all the time. Output all the numbers from 1 to 100.
        //
        // Hint: a == b is the same as ( a - b ) == 0*
        //
        // const 1
        // label loop
        // dup
        // const 100
        // sub
        // jez end
        // output
        // const 1
        // add
        // jump loop

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
                }, new ArrayList<StackValue<?>>() {
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
        // ops: load, store, input, output
        // in: 1 2
        // out: 2 1
        //
        // Being caring and thoughtful the authors have made the extra effort to
        // give
        // users four boxes for storing intermediate results in. LOAD/STORE need
        // to know
        // which box you are accessing and otherwise do exactly what you think
        // they do.
        // Using this, take the pair of numbers from input and output them
        // backwards.
        //
        // input
        // store 1
        // input
        // output
        // load 1
        // output

    }

}
