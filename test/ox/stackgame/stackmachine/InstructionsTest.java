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
	instructions.add(new Instruction("const", new IntStackValue(2)));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
	assertEquals(new Integer(2), machine.getStack().pop().getValue());
    }
    
    @Test
    public void testAdd() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("const", new IntStackValue(2)));
	instructions.add(new Instruction("const", new IntStackValue(3)));
	instructions.add(new Instruction("add"));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
	machine.step();
	machine.step();
	assertEquals(new Integer(5), machine.getStack().pop().getValue());
	assertEquals(0, machine.getStack().size());
    }
    
    @Test
    public void testSub() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("const", new IntStackValue(2)));
	instructions.add(new Instruction("const", new IntStackValue(3)));
	instructions.add(new Instruction("sub"));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
	machine.step();
	machine.step();
	assertEquals(new Integer(-1), machine.getStack().pop().getValue());
	assertEquals(0, machine.getStack().size());
    }
    
    @Test
    public void testMul() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("const", new IntStackValue(2)));
	instructions.add(new Instruction("const", new IntStackValue(3)));
	instructions.add(new Instruction("mul"));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
	machine.step();
	machine.step();
	assertEquals(new Integer(6), machine.getStack().pop().getValue());
	assertEquals(0, machine.getStack().size());
    }    
    @Test
    public void testDiv() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("const", new IntStackValue(5)));
	instructions.add(new Instruction("const", new IntStackValue(2)));
	instructions.add(new Instruction("div"));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
	machine.step();
	machine.step();
	assertEquals(new Integer(2), machine.getStack().pop().getValue());
	assertEquals(0, machine.getStack().size());
    }

    @Test
    public void testStore() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("const", new IntStackValue(2)));
	instructions.add(new Instruction("store", new IntStackValue(0)));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.step();
	machine.step();
	assertEquals(new Integer(2), machine.getStore(0).getValue());
    }
    
    @Test
    public void testLoad() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("load", new IntStackValue(0)));
	StackProgram program = new StackProgram(instructions);
	StackMachine machine = new StackMachine(program);
	machine.setStore(0, new IntStackValue(2));
	machine.step();
	assertEquals(new Integer(2), machine.getStack().pop().getValue());
    }
    
    @Test
    public void testInput() throws StackRuntimeException {
	List<Instruction> instructions = new ArrayList<Instruction>();
	instructions.add(new Instruction("input"));
	List<StackValue<?>> input = new ArrayList<StackValue<?>>();
	input.add(new IntStackValue(2));
	StackMachine machine = new StackMachine(instructions, input);
	machine.step();
	assertEquals(new Integer(2), machine.getStack().pop().getValue());
    }
    
}
