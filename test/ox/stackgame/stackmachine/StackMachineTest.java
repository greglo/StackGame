package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ox.stackgame.stackmachine.exceptions.*;
import ox.stackgame.stackmachine.instructions.*;

public class StackMachineTest {

    @Test
    public void testEmptyProgram() {
	List<Instruction> instructions = new ArrayList<Instruction>();
	StackMachine machine = new StackMachine(instructions);
	assertEquals(false, machine.isRunning());
    }

    @Test(expected = NotHaltingException.class)
    public void testInfiniteLoop() throws StackRuntimeException,
	    NotHaltingException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("label", new StringStackValue("lab")));
	instructions.add(new Instruction("jump", new StringStackValue("lab")));
	StackMachine machine = new StackMachine(instructions);
	machine.runAll();
    }

    @Test(expected = EmptyStackException.class)
    public void testEmptyStackException() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("add"));
	StackMachine machine = new StackMachine(instructions);
	machine.step();
    }

    @Test(expected = NoSuchLabelException.class)
    public void testNoSuchLabelException() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("jump", new StringStackValue("lab")));
	StackMachine machine = new StackMachine(instructions);
	machine.step();
    }

    @Test(expected = InvalidAddressException.class)
    public void testInvalidAddressException() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("load", new IntStackValue(-1)));
	StackMachine machine = new StackMachine(instructions);
	machine.step();
    }

    public void testAlmostFullStack() throws StackRuntimeException {
	// We will push until the stack is full, but push no more
	// (No exception expected)
	List<Instruction> instructions = new ArrayList<Instruction>();
	for (int i = 0; i < StackMachine.STACK_SIZE; i++)
	    instructions.add(new Instruction("const", new IntStackValue(0)));
	StackMachine machine = new StackMachine(instructions);
	for (int i = 0; i < StackMachine.STACK_SIZE; i++)
	    machine.step();
    }

    @Test(expected = FullStackException.class)
    public void testFullStackException() throws StackRuntimeException {
	// We will push until the stack is full, and push again
	// (Exception expected)
	List<Instruction> instructions = new ArrayList<Instruction>();
	for (int i = 0; i <= StackMachine.STACK_SIZE; i++)
	    instructions.add(new Instruction("const", new IntStackValue(0)));
	StackMachine machine = new StackMachine(instructions);
	for (int i = 0; i <= StackMachine.STACK_SIZE; i++)
	    machine.step();
    }

}
