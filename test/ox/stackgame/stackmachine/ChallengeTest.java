package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ox.stackgame.challenge.AbstractChallenge;
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
        AbstractChallenge c = new ISetTestChallenge(new HashMap<String,Integer>(){{
            put("add",null);
        }});
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
        AbstractChallenge c = new ISetTestChallenge(new HashMap<String,Integer>(){{
            put("load 1",null);
        }});
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
        AbstractChallenge c = new ISetTestChallenge(new HashMap<String,Integer>(){{
            put("load *",null);
        }});
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

}
