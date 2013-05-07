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
                new TreeMap<String, Integer>() {
                    {
                        put("const *", 1);
                    }
                }, new IntStackValue(1), new LinkedList<StackValue<?>>(), 1,1));
        
        cl.add(new StackResultChallenge("Arithmetic Operations", 
                "Binary arithmetic operations <strong>pop</strong> two values off the stack, and <strong>push</strong> the result of the operation back"
                + "<br/>"
                + "<br/>Use the following instructions to get <strong>3</strong> on the stack",
                new TreeMap<String, Integer>() {
                    {
                        put("const 1", 1);
                        put("const 2", 1);
                        put("add", 1);
                    }
                }, new IntStackValue(3), new LinkedList<StackValue<?>>(), 3, 3));

        cl.add(new StackResultChallenge("Reverse Polish",
                "There is no way to bracket your programs, so find a way to compute 1 + (2 * 3) = 7 on the stack",
                new TreeMap<String, Integer>() {
                    {
                        put("const 1", 1);
                        put("const 2", 1);
                        put("const 3", 1);
                        put("add", 1);
                        put("mul", 1);
                    }
                }, new IntStackValue(7), new LinkedList<StackValue<?>>(),5,5));

        cl.add(new TapeChallenge("The Tape",
                "The machine has two tapes you can use, an input tape and an output tape.<br/>"
                + "The input tape is used to pass information to your program. The INPUT instruction will take the first value from the input tape, and push it on the stack."
                + "<br/>The output tape starts empty. The OUTPUT instruction will pop a value off the stack, and append it to the output tape."
                + "<br/>"
                + "<br/>Use an INPUT and an OUTPUT instruction to move 1 from the input tape to the output tape",
                new TreeMap<String, Integer>() {
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
                },2,2));

        cl.add(new TapeChallenge("Branching and Loops",
                "The LABEL <name> instruction does nothing but name a point in a program. You may JUMP to this point in the program from anywhere using one of the branch instructions."
                + "<br/>JII <name> jumps to the appropriate label if there is something on the input tape."
                + "<br/>"
                + "<br/>Copy the numbers 1-10 from the input tape to the output tape, using only four instructions.",
                new TreeMap<String, Integer>() {
                    {
                        put("input", 1);
                        put("output", 1);
                        put("jii *", 1);
                        put("label *", 1);
                    }
                }, tape1to10, tape1to10, 31, 4));

        try {
            cl.add(new TapeChallenge(
                    "Store / Memory",
                    "Being caring and thoughtful, the authors have given users four boxes for storing intermediate " +
                    "results. <br/><br/>LOAD/STORE need to know which box you are accessing, but otherwise do exactly " +
                    "what you'd expect. <br/><br/>Take the four letters from the input tape and rearrange them to " +
                    "output in alphabetical order.",
                    new TreeMap<String, Integer>() {
                        {
                            put("load *", null);
                            put("store *", null);
                            put("input", null);
                            put("output", null);
                        }
                    }, new ArrayList<StackValue<?>>() {
                        {
                            add(new CharStackValue('d'));
                            add(new CharStackValue('b'));
                            add(new CharStackValue('c'));
                            add(new CharStackValue('a'));
                        }
                    }, new ArrayList<StackValue<?>>() {
                        {
                            add(new CharStackValue('a'));
                            add(new CharStackValue('b'));
                            add(new CharStackValue('c'));
                            add(new CharStackValue('d'));
                        }
                    }, 10, 10));
        } catch (InvalidCharException e1) {
            // this should never happen
            e1.printStackTrace();
        }
        
        cl.add(new TapeChallenge(
                "Further Branching",
                "JNEZ/JEZ (jump [not] equal zero) are instructions that pop a value off the stack, compare it with 0 and branch if they are not equal/equal respectively. " +
                "<br/><br/>Output all the numbers from 1 to 10 with the following instructions:<br>Hint: a == b is the same as ( a - b ) == 0",
                new TreeMap<String, Integer>() {
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
                }, new ArrayList<StackValue<?>>(), tape1to10, 101, 11));
        
/*
 * Inefficient solution: (so that people can beat something!!)
const 10
store 0
const 1
store 1
label loop
load 1
load 1
output
const 1
add
store 1
load 0
const 1
sub
store 0
load 0
jnez loop

(101,11) LINEAR SOLUTION, DOESN'T USE MOD: 
label loop
load 0
const 1
add
store 0
load 0
output
const 10
load 0
sub
jnez loop
 */
        
        cl.add(new TapeChallenge(
                "Filtering Using JLZ",
                "JLZ/JGZ (jump if less/greater than zero) are instructions that pop a value off the stack, compare it with 0 and branch if they are < 0 or > 0 respectively." +
                "<br/><br/>Use JLZ to filter the input tape, outputting only the positive values:<br/>(Hint: You will need to use STORE to hold onto each value)",
                new TreeMap<String, Integer>() {
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
                }, 30, 8));
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
                new TreeMap<String, Integer>() {
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
                }, 46, 6));
        
        cl.add(new TapeChallenge(
                "Greatest Common Divisor",
                "The MOD instruction finds the remainder when dividing one number by another: 5 MOD 3 = 2." +
                "<br/><br/>Implement an algorithm to find the Greatest Common Divisor of the two numbers " +
                "on the input tape, and put it on the output tape." +
                "<br/><br/>(Hint: Look up Euclid's Algorithm)",
                null
                , new ArrayList<StackValue<?>>() {
                    { 
                        add(new IntStackValue(738));
                        add(new IntStackValue(84));
                    }
                } , new ArrayList<StackValue<?>>() {
                    { 
                        add(new IntStackValue(6));
                    }
                }, 202, 23)); 
        
/*
input
store 0
input 
store 1
label loop
load 0
load 1
sub
store 0
load 0
load 1
sub
jgz gz
load 0
load 1
store 0
store 1
label gz
load 0
load 1
jnez loop
load 0
output
 */
        
        cl.add(new StackResultChallenge(
                "Maximum of an Array",
                "Use any of the instructions below to loop through the numbers on the input tape and " +
                "output the largest onto the stack.  You'll probably want to use some of the store boxes.",
                new TreeMap<String, Integer>() {
                    {
                        put("jii *", 1);
                        put("jgz *", null);
                        put("jlz *", null);
                        put("jez *", null);
                        put("jnez *", null);
                        put("label *", null);
                        put("add", null);
                        put("sub", null);
                        put("const 1", null); // just to confuse them
                        put("mul", null);
                        put("div", null);
                        put("load *", null);
                        put("store *", null);
                        put("input", null);
                        put("output", null);
                    }
                }, // instructionSet 
                new IntStackValue(99),
                new ArrayList<StackValue<?>>() {
                    { 
                        add(new IntStackValue(1));
                        add(new IntStackValue(5));
                        add(new IntStackValue(6));
                        add(new IntStackValue(8));
                        add(new IntStackValue(80));
                        add(new IntStackValue(-10));
                        add(new IntStackValue(99));
                        add(new IntStackValue(4));
                    }
                } ,  68, 14));
        
 /*
input
store 0
label a
load 0
input
store 1
load 1
sub
jgz notbigger
load 1
store 0
label notbigger
jii a
load 0
  */
    }
}

