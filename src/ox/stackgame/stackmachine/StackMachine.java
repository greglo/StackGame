package ox.stackgame.stackmachine;

import java.util.ArrayList;
import java.util.List;

import ox.stackgame.stackmachine.exceptions.NotHaltingException;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.Instruction;

public class StackMachine {
    private ImmutableStackMachine immutableMachine;
    private List<Instruction> instructions;
    private List<StackMachineListener> listeners = new ArrayList<StackMachineListener>();
    
    public StackMachine() {
	instructions = new ArrayList<Instruction>();
	updateImmutableMachine();
    }
    
    public StackMachine(List<Instruction> instructions) {
	if (instructions != null)
	    this.instructions = instructions;
	else
	    this.instructions = new ArrayList<Instruction>();
	updateImmutableMachine();
    }
    
    public void addInstruction(int line, Instruction instruction) {
	if (line < 0)
	    line = 0;
	if (line > instructions.size())
	    line = instructions.size();
	instructions.add(line, instruction);   
    }
    
    public void reset() {
	immutableMachine.reset();
    }
    
    public boolean isRunning() {
	return immutableMachine.isRunning();
    }
    
    public void step() throws StackRuntimeException {
	immutableMachine.step();
    }
    
    public void runAll() throws StackRuntimeException, NotHaltingException {
	immutableMachine.runAll();
    }
    
    private void updateImmutableMachine() {
	this.immutableMachine = new ImmutableStackMachine(new StackProgram(instructions));
	for (StackMachineListener l : listeners)
	    immutableMachine.addListener(l);
    }
    
    
}
