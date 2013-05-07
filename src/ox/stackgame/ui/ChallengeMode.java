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
        this.currChallenge = challenge;
    }

    @SuppressWarnings("serial")
    public ChallengeMode() {
        List<AbstractChallenge> cl = challengeList;

        ArrayList<StackValue<?>> tape1to10 = new ArrayList<StackValue<?>>() {
            {
                for (int i = 1; i <= 10; i++)
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
                }, new IntStackValue(1), new LinkedList<StackValue<?>>()));
        
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
                }, new IntStackValue(3), new LinkedList<StackValue<?>>()));

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
                }, new IntStackValue(6), new LinkedList<StackValue<?>>()));

        cl.add(new TapeChallenge("The Tape",
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

        cl.add(new TapeChallenge("Branching and Loops",
                "The LABEL <name> instruction does nothing but name a point in a program. You may JUMP to this point in the program from anywhere using one of the branch instructions."
                + "<br/>JII <name> jumps to the appropriate label if there is something on the input tape."
                + "<br/>"
                + "<br/>Copy the numbers 1-10 from the input tape to the output tape, using only 4 instructions.",
                new HashMap<String, Integer>() {
                    {
                        put("input", 1);
                        put("output", 1);
                        put("jii *", 1);
                        put("label *", 1);
                    }
                }, tape1to10, tape1to10));

        cl.add(new TapeChallenge(
                "Store / Memory",
                "Being caring and thoughtful, the authors have given users four boxes for storing intermediate results. <br/><br/>LOAD/STORE need to know which box you are accessing, but otherwise do exactly what you'd expect. <br/><br/>Take the pair of numbers from input and output them backwards.",
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
        
        cl.add(new TapeChallenge(
                "Further Branching",
                "JNEZ/JEZ (jump [not] equal zero) are instructions that pop a value off the stack, compare it with 0 and branch if they are not equal/equal respectively. " +
                "<br/><br/>Output all the numbers from 1 to 10 with the following instructions:<br>(Hint: a == b is the same as ( a - b ) == 0",
                new HashMap<String, Integer>() {
                    {
                        put("const 1", null);
                        put("const 10", null);
                        put("add", null);
                        put("sub", null);
                        put("jnez *", null);
                        put("load *", null);
                        put("store *", null);
                        put("label *", null);
                        put("output", null);
                    }
                }, new ArrayList<StackValue<?>>(), tape1to10));
        
        cl.add(new TapeChallenge(
                "Filtering Using JLZ",
                "JLZ/JGZ (jump if less/greater than zero) are instructions that pop a value off the stack, compare it with 0 and branch if they are < 0 or > 0 respectively." +
                "<br/><br/>Use JLZ to filter the input tape, outputting only the positive values:<br/>(Hint: You will need to use STORE to hold onto each value)",
                new HashMap<String, Integer>() {
                    {
                        put("label *", null);
                        put("jlz *", null);
                        put("load *", null);
                        put("store *", null);
                        put("input", null);
                        put("output", null);
                        put("jii *", null);
                    }
                }, new ArrayList<StackValue<?>>() {
                    { 
                        add(new IntStackValue(-1));
                        add(new IntStackValue(5));
                        add(new IntStackValue(1));
                        add(new IntStackValue(-10));
                        add(new IntStackValue(50));
                    }
                } , new ArrayList<StackValue<?>>() {
                    { 
                        add(new IntStackValue(5));
                        add(new IntStackValue(1));
                        add(new IntStackValue(50));
                    }
                }));
        /* Solved with:
            label a
            input
            store 1
            load 1
            jlz a
            load 1
            output
            jii a
         */
        
        cl.add(new TapeChallenge(
                "ROT13",
                "One of the most secure encryption methods today is ROT13. To do ROT13 encryption, you ADD 13 to each character in your plaintext to get a ciphertext.<br/>Incredibly, you do exactly the same to decrypt ciphetext.<br/><br/>Implement ROT13 to decrypt the secret message on the input tape.",
                new HashMap<String, Integer>() {
                    {
                        put("jii *", null);
                        put("const *", null);
                        put("add", null);
                        put("label *", null);
                        put("input", null);
                        put("output", null);
                    }
                }, new ArrayList<StackValue<?>>() {
                    {
                        try {
                            add(new CharStackValue(new Character('T')));
                            add(new CharStackValue(new Character('E')));
                            add(new CharStackValue(new Character('R')));
                            add(new CharStackValue(new Character('T')));
                            add(new CharStackValue(new Character('E')));
                            add(new CharStackValue(new Character('H')));
                            add(new CharStackValue(new Character('Y')));
                            add(new CharStackValue(new Character('R')));
                            add(new CharStackValue(new Character('F')));
                        }
                        catch (InvalidCharException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ArrayList<StackValue<?>>() {
                    {
                        try {
                            add(new CharStackValue(new Character('G')));
                            add(new CharStackValue(new Character('R')));
                            add(new CharStackValue(new Character('E')));
                            add(new CharStackValue(new Character('G')));
                            add(new CharStackValue(new Character('R')));
                            add(new CharStackValue(new Character('U')));
                            add(new CharStackValue(new Character('L')));
                            add(new CharStackValue(new Character('E')));
                            add(new CharStackValue(new Character('S')));
                        }
                        catch (InvalidCharException e) {
                            e.printStackTrace();
                        }                    }
                }));
        
        cl.add(new TapeChallenge(
                "Euclid's Algorithm",
                "The MOD instruction finds the remainder when dividing one number by another: 5 MOD 3 = 2." +
                "<br/><br/>Use MOD, among other instructions, to implement Euclid's Algorithm",
                null
                , new ArrayList<StackValue<?>>() {
                    { 
                        add(new IntStackValue(100));
                        add(new IntStackValue(80));
                    }
                } , new ArrayList<StackValue<?>>() {
                    { 
                        add(new IntStackValue(20));
                    }
                }));
    }
}

