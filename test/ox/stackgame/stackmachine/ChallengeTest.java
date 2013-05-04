package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ox.stackgame.challenge.AbstractChallenge;
import ox.stackgame.challenge.TapeChallenge;
import ox.stackgame.stackmachine.Lexer.LexerException;
import ox.stackgame.stackmachine.exceptions.NotHaltingException;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.Instruction;

public class ChallengeTest {
    
    private class ISetTestChallenge extends AbstractChallenge{

        ISetTestChallenge(Map<String, Integer> instructionSet) {
            super("", "", instructionSet);
        }

        public Boolean hasSucceeded(StackMachine m) {  return null;   }

        public String getMessage() {return null;  }
        
    }

    @SuppressWarnings("serial")
    @Test
    public void testLimitedNoParam() {
        AbstractChallenge c = new ISetTestChallenge(new HashMap<String,Integer>(){{
            put("add",2);
        }});
        assertEquals(true,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("add"));
            add(new Instruction("add"));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("add"));
            add(new Instruction("add"));
            add(new Instruction("add"));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("sub"));
        }}));
    }

    @SuppressWarnings("serial")
    @Test
    public void testLimited1ParamSpec() {
        AbstractChallenge c = new ISetTestChallenge(new HashMap<String,Integer>(){{
            put("load 1",2);
        }});
        assertEquals(true,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("load", new IntStackValue(1)));
            add(new Instruction("load", new IntStackValue(1)));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("load", new IntStackValue(1)));
            add(new Instruction("load", new IntStackValue(1)));
            add(new Instruction("load", new IntStackValue(1)));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("load", new IntStackValue(2)));
        }}));
    }

    @SuppressWarnings("serial")
    @Test
    public void testLimited1ParamUnSpec() {
        AbstractChallenge c = new ISetTestChallenge(new HashMap<String,Integer>(){{
            put("load *",2);
        }});
        assertEquals(true,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("load", new IntStackValue(1)));
            add(new Instruction("load", new IntStackValue(2)));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("load", new IntStackValue(1)));
            add(new Instruction("load", new IntStackValue(3)));
            add(new Instruction("load", new IntStackValue(2)));
            add(new Instruction("load", new IntStackValue(4)));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("sub"));
        }}));
    }
    
    @SuppressWarnings("serial")
    @Test
    public void testUnlimitedNoParam() {
        AbstractChallenge c = new ISetTestChallenge(unlimitedISet("add"));
        assertEquals(true,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("add"));
            add(new Instruction("add"));
            add(new Instruction("add"));
            add(new Instruction("add"));
            add(new Instruction("add"));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("sub"));
        }}));
    }

    @SuppressWarnings("serial")
    @Test
    public void testUnlimited1ParamSpec() {
        AbstractChallenge c = new ISetTestChallenge(unlimitedISet("load 1"));
        assertEquals(true,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("load", new IntStackValue(1)));
            add(new Instruction("load", new IntStackValue(1)));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("load", new IntStackValue(1)));
            add(new Instruction("load", new IntStackValue(1)));
            add(new Instruction("load", new IntStackValue(2)));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("add"));
        }}));
    }

    @SuppressWarnings("serial")
    @Test
    public void testUnlimited1ParamUnSpec() {
        AbstractChallenge c = new ISetTestChallenge(unlimitedISet("load *"));
        assertEquals(true,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("load", new IntStackValue(1)));
            add(new Instruction("load", new IntStackValue(2)));
            add(new Instruction("load", new IntStackValue(3)));
            add(new Instruction("load", new IntStackValue(4)));
        }}));
        assertEquals(false,c.checkProgram(new ArrayList<Instruction>(){{
            add(new Instruction("sub"));
        }}));
    }

    @SuppressWarnings("serial")
    public void testTapeChallenge() {
        // set up challenge
        AbstractChallenge c = new TapeChallenge("t", "desc", unlimitedISet(
                "const *", "output"), new ArrayList<StackValue<?>>(),
                new ArrayList<StackValue<?>>() {
                    {
                        add(new IntStackValue(1));
                        add(new IntStackValue(2));
                        add(new IntStackValue(3));
                        add(new IntStackValue(4));
                    }
                });
        // set up machine
        try {
            StackMachine m = new StackMachine(Lexer.lex("const 1\n"
                    + "const 2\n" + "const 3\n" + "const 4\n" + "output\n"
                    + "output\n" + "output\n" + "output"));
            m.runAll();

            assertEquals(true, c.hasSucceeded(m));

            StackMachine m2 = new StackMachine(Lexer.lex("const 9\n"
                    + "const 9\n" + "const 9\n" + "const 9\n" + "output\n"
                    + "output\n" + "output\n" + "output"));
            m2.runAll();

            assertEquals(false, c.hasSucceeded(m2));

        } catch (LexerException e) {
            System.out.println("Test configured wrong");
            e.printStackTrace();
        } catch (StackRuntimeException e) {
            System.out.println("Test configured wrong");
            e.printStackTrace();
        } catch (NotHaltingException e) {
            System.out.println("Test configured wrong");
            e.printStackTrace();
        }

    }
    
    // UTILITY
    @SuppressWarnings("serial")
    private HashMap<String,Integer> unlimitedISet(final String... instructions){
        return new HashMap<String,Integer>(){{
           for (String i : instructions) put(i, null); 
        }};
    }
}
