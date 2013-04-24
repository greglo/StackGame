package ox.stackgame.stackmachine;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.Instruction;

public class StackMachineListenerTest {

    class MockListener implements StackMachineListener {
	public boolean stackInstructionsChangedFired = false;

	public void stackInstructionsChanged(List<Instruction> instructions) {
	    this.stackInstructionsChangedFired = true;
	}

	public boolean stackProgramCounterChanged = false;

	public void programCounterChanged(int line) {
	    this.stackProgramCounterChanged = true;
	}

	public boolean storeChangedFired = false;

	public void storeChanged(int address) {
	    this.storeChangedFired = true;
	}

	public boolean inputConsumedFired = false;

	public void inputConsumed(int startIndex) {
	    this.inputConsumedFired = true;
	}

	public boolean outputChangedFired = false;

	public void outputChanged() {
	    this.outputChangedFired = true;
	}
    }

    @Test
    public void testStackInstructionsChanged() {
	StackProgram program = new StackProgram();
	StackMachine machine = new StackMachine(program);
	MockListener l = new MockListener();
	machine.addListener(l);
	machine.addInstruction(0, new Instruction("load", new IntStackValue(2)));
	assertEquals(true, l.stackInstructionsChangedFired);
    }

    @Test
    public void testStackProgramCounterChanged() throws StackRuntimeException {
	List<Instruction> program = new ArrayList<Instruction>();
	StackMachine machine = new StackMachine(program);
	MockListener l = new MockListener();
	machine.addListener(l);
	machine.addInstruction(0, new Instruction("const", new IntStackValue(5)));
	machine.step();
	assertEquals(true, l.stackProgramCounterChanged);
    }

    @Test
    public void testStoreChanged() {

    }

    @Test
    public void testInputConsumed() {

    }

    @Test
    public void testOutputChanged() {

    }
}
