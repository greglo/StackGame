package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ox.stackgame.stackmachine.instructions.ConstInstruction;
import ox.stackgame.stackmachine.instructions.Instruction;
import ox.stackgame.stackmachine.instructions.IntegerAddInstruction;
import ox.stackgame.stackmachine.instructions.JumpInstruction;
import ox.stackgame.stackmachine.instructions.LabelInstruction;

public class StackMachineTest {

    @Test
    public void testEmpty() {
	StackProgram<Integer> program = new StackProgram<Integer>();
	StackMachine<Integer> machine = new StackMachine<Integer>(program);
	
	assertEquals("Empty program is not runnable", false, machine.isRunnable());
    }
    
    @Test
    public void testStackEvaluation() {
	List<Instruction<Integer>> instructions = new ArrayList<Instruction<Integer>>();
	instructions.add(new ConstInstruction<Integer>(2));
	instructions.add(new ConstInstruction<Integer>(3));
	instructions.add(new IntegerAddInstruction());
	StackProgram<Integer> program = new StackProgram<Integer>(instructions);
	StackMachine<Integer> machine = new StackMachine<Integer>(program);
	
	machine.step();
	machine.step();
	machine.step();
	assertEquals(new Integer(5), machine.getStack().peek());
    }
    
    @Test(expected=NotHaltingException.class)
    public void testInfiniteLoop() throws NotHaltingException {
	List<Instruction<Integer>> instructions = new ArrayList<Instruction<Integer>>();
	instructions.add(new LabelInstruction<Integer>("lab"));
	instructions.add(new JumpInstruction<Integer>("lab"));
	StackProgram<Integer> program = new StackProgram<Integer>(instructions);
	StackMachine<Integer> machine = new StackMachine<Integer>(program);
	machine.runAll();
    }

}
