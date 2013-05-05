package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.exceptions.NotHaltingException;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.Instruction;

public class StackMachineListenerTest {

    class MockListener implements StackMachineListener {
        public boolean programChangedFired = false;
        public boolean stackProgramCounterChanged = false;
        public boolean storeChangedFired = false;
        public boolean inputConsumedFired = false;
        public boolean outputChangedFired = false;
        public boolean stackChangedFired = false;
        public boolean machineResetFired = false;

        public void programChanged(List<Instruction> instructions) {
            this.programChangedFired = true;
        }

        public void programCounterChanged(int line, Instruction instruction) {
            this.stackProgramCounterChanged = true;
        }

        public void storeChanged(int address) {
            this.storeChangedFired = true;
        }

        public void inputConsumed(int startIndex) {
            this.inputConsumedFired = true;
        }

        public void outputChanged(Iterator<StackValue<?>> output) {
            this.outputChangedFired = true;
        }

        public void stackChanged(EvaluationStack stack) {
            stackChangedFired = true;
        }

        public void machineReset() {
            machineResetFired = true;
        }

    }

    @Test
    public void testStackInstructionsChanged() throws StackRuntimeException, NotHaltingException {
        List<Instruction> instructions = new ArrayList<Instruction>();
        StackMachine machine = new StackMachine(instructions);
        MockListener l = new MockListener();
        machine.addListener(l);
        machine.addInstruction(0, new Instruction("load", new IntStackValue(2)));
        machine.runAll();
        assertTrue(l.programChangedFired);
    }

    @Test
    public void testStackProgramCounterChanged() throws StackRuntimeException, NotHaltingException {
        List<Instruction> instructions = new ArrayList<Instruction>();
        StackMachine machine = new StackMachine(instructions);
        MockListener l = new MockListener();
        machine.addListener(l);
        machine.addInstruction(0,
                new Instruction("const", new IntStackValue(2)));
        machine.runAll();
        assertTrue(l.stackProgramCounterChanged);
    }

    @Test
    public void testStoreChanged() throws StackRuntimeException, NotHaltingException {
        List<Instruction> instructions = new ArrayList<Instruction>();
        StackMachine machine = new StackMachine(instructions);
        MockListener l = new MockListener();
        machine.addListener(l);
        machine.addInstruction(0,
                new Instruction("const", new IntStackValue(2)));
        machine.addInstruction(1,
                new Instruction("store", new IntStackValue(0)));
        machine.runAll();
        assertTrue(l.storeChangedFired);
    }

    @Test
    public void testInputConsumed() throws StackRuntimeException, NotHaltingException {
        List<Instruction> instructions = new ArrayList<Instruction>();
        List<StackValue<?>> input = new ArrayList<StackValue<?>>();
        input.add(new IntStackValue(2));
        StackMachine machine = new StackMachine(instructions, input);
        MockListener l = new MockListener();
        machine.addListener(l);
        machine.addInstruction(0, new Instruction("input"));
        machine.runAll();
        assertTrue(l.inputConsumedFired);
    }

    @Test
    public void testOutputChanged() throws StackRuntimeException, NotHaltingException {
        List<Instruction> instructions = new ArrayList<Instruction>();
        StackMachine machine = new StackMachine(instructions);
        MockListener l = new MockListener();
        machine.addListener(l);
        machine.addInstruction(0, new Instruction("const", new IntStackValue(2)));
        machine.addInstruction(1, new Instruction("output"));
        machine.runAll();
        assertTrue(l.outputChangedFired);
    }

    @Test
    public void testStackChanged() throws StackRuntimeException, NotHaltingException {
        List<Instruction> instructions = new ArrayList<Instruction>();
        StackMachine machine = new StackMachine(instructions);
        MockListener l = new MockListener();
        machine.addListener(l);
        machine.addInstruction(0, new Instruction("const", new IntStackValue(2)));
        machine.addInstruction(1, new Instruction("output"));
        machine.step();
        assertTrue(l.stackChangedFired);
        l.outputChangedFired = false;
        machine.step();
        assertTrue(l.stackChangedFired);
    }
    
    @Test
    public void testMachineReset() throws StackRuntimeException, NotHaltingException {
        List<Instruction> instructions = new ArrayList<Instruction>();
        StackMachine machine = new StackMachine(instructions);
        MockListener l = new MockListener();
        machine.addListener(l);
        machine.addInstruction(0, new Instruction("const", new IntStackValue(2)));
        machine.addInstruction(1, new Instruction("output"));
        machine.reset();
        assertTrue(l.machineResetFired);
    }
}
