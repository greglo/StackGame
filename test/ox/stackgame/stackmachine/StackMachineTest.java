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
	StackProgram program = new StackProgram();
	StackMachine machine = new StackMachine(program);
	assertEquals(false, machine.isRunning());
    }

    @Test(expected = NotHaltingException.class)
    public void testInfiniteLoop() throws StackRuntimeException, NotHaltingException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new LabelInstruction("lab"));
	instructions.add(new JumpInstruction("lab"));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.runAll();
    }
    
    @Test(expected = EmptyStackException.class)
    public void testEmptyStackException() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new AddInstruction());
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
    }

    @Test(expected = NoSuchLabelException.class)
    public void testNoSuchLabelException() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new JumpInstruction("lab"));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
    }
    
    @Test(expected = InvalidAddressException.class)
    public void testInvalidAddressException() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new LoadInstruction(-1));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
    }
    
}
