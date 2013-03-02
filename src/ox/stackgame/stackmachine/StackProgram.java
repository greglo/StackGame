package ox.stackgame.stackmachine;

import java.util.ArrayList;
import java.util.List;

import ox.stackgame.stackmachine.instructions.*;

public class StackProgram {
    private final List<Instruction> instructions;
    
    public StackProgram() {
	instructions = new ArrayList<Instruction>();
    }
    
    public StackProgram(String source) {
	instructions = new ArrayList<Instruction>();
    }
    
    public StackProgram(List<Instruction> instructions) {
	this.instructions = instructions;
    }
    
    public int countInstructions() {
	return instructions.size();
    }
    
    public Instruction instructionAt(int index) {
	return instructions.get(index);
    }
    
    public int getLabelPosition(String identifier) {
	for (int i = 0; i < instructions.size(); i++) {
	    Instruction instruction = instructions.get(i);
	    if (instruction instanceof LabelInstruction && ((LabelInstruction) instruction).getIdentifier().equals(identifier))
		return i;
	}
	return -1;
    }
    
}
