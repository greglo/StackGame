package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ox.stackgame.stackmachine.exceptions.EmptyStackException;
import ox.stackgame.stackmachine.exceptions.NotHaltingException;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.AddInstruction;
import ox.stackgame.stackmachine.instructions.ConstInstruction;
import ox.stackgame.stackmachine.instructions.Instruction;
import ox.stackgame.stackmachine.instructions.JumpInstruction;
import ox.stackgame.stackmachine.instructions.LabelInstruction;

public class StackMachineTest {

    @Test
    public void testEmpty() {
	StackProgram program = new StackProgram();
	StackMachine machine = new StackMachine(program);

	assertEquals("Empty program is not runnable", false,
		machine.isRunning());
    }

    @Test
    public void testIntStackEvaluation() throws EmptyStackException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new ConstInstruction(new IntStackValue(2)));
	instructions.add(new ConstInstruction(new IntStackValue(3)));
	instructions.add(new AddInstruction());
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);

	try {
	    machine.step();
	    machine.step();
	    machine.step();
	} catch (StackRuntimeException e) {
	    System.out.println(e.getLine() + ": " + e.toString());
	}

	assertEquals(new Integer(5), machine.getStack().peek().getValue());
    }

    @Test
    public void testCharStackEvaluation() throws EmptyStackException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new ConstInstruction(new CharStackValue('A')));
	instructions.add(new ConstInstruction(new CharStackValue('B')));
	instructions.add(new AddInstruction());
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);

	try {
	    machine.step();
	    machine.step();
	    machine.step();
	} catch (StackRuntimeException e) {
	    System.out.println(e.getLine() + ": " + e.toString());
	}
	
	assertEquals('B', machine.getStack().peek().getValue());
    }

    @Test(expected = NotHaltingException.class)
    public void testInfiniteLoop() throws NotHaltingException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new LabelInstruction("lab"));
	instructions.add(new JumpInstruction("lab"));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	try {
	    machine.runAll();
	} catch (StackRuntimeException e) {
	    System.out.println(e.getLine() + ": " + e.toString());
	}
    }

}
