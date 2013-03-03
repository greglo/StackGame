package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.*;

public class InstructionsTest {

    @Test
    public void testConst() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new ConstInstruction(new IntStackValue(2)));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
	assertEquals(new Integer(2), machine.getStack().pop().getValue());
    }
    
    @Test
    public void testAdd() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new ConstInstruction(new IntStackValue(2)));
	instructions.add(new ConstInstruction(new IntStackValue(3)));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
	machine.step();
	machine.step();
	assertEquals(new Integer(3), machine.getStack().pop().getValue());
    }

    @Test
    public void testStore() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new ConstInstruction(new IntStackValue(2)));
	instructions.add(new StoreInstruction(0));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
	machine.step();
	assertEquals(new Integer(2), machine.getStore(0).getValue());
    }
    
    @Test
    public void testLoad() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new LoadInstruction(0));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.setStore(0, new IntStackValue(2));
	machine.step();
	assertEquals(new Integer(2), machine.getStack().pop().getValue());
    }
}
