package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ox.stackgame.stackmachine.Lexer.LexerException;
import ox.stackgame.stackmachine.exceptions.NotHaltingException;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.Instruction;

public class LexerTest {

    @Test(expected = LexerException.class)
    public void noArgsTest() throws LexerException {
	// Throws error as no argument provided to 'const'
	Lexer.lex("const");
    }

    @Test(expected = LexerException.class)
    public void tooManyArgsTest() throws LexerException {
	// Throws error as too many arguments provided
	Lexer.lex("const 1 2");
    }
    
    @Test
    public void formattingTest() throws LexerException {
	// The lexer should be able to handle weirdly (but validly) formatted code
	Lexer.lex(" add ");
	Lexer.lex("ADD");
	Lexer.lex("const       5");
    }

    @Test
    public void lexertest() throws LexerException {
	List<Instruction> ops = Lexer.lex("jump lab\n" + "const 5\n"
		+ "label lab\n" + "const 7\n" + "const 100\n" + "add\n"
		+ "nop\n");

	StackMachine m = new StackMachine(ops);

	try {
	    m.step();
	    m.step();
	    m.step();
	    m.step();
	    m.step();
	    m.step();
	    m.step();
	    m.step();
	    m.step();
	    assertEquals(107, m.getStack().peek().getValue()); // checks the
							       // value on the
							       // stack is 107
	} catch (StackRuntimeException e) {
	    System.out.println("it all blew up: " + e.toString());
	}

	try {
	    m.reset();
	    assertEquals(0, m.getProgramCounter());
	    m.runAll();
	    assertEquals(107, m.getStack().peek().getValue());
	} catch (StackRuntimeException e) {
	    e.printStackTrace();
	} catch (NotHaltingException e) {
	    e.printStackTrace();
	}

    }

}
