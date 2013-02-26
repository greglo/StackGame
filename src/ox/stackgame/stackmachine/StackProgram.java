package ox.stackgame.stackmachine;

import java.util.ArrayList;
import java.util.List;

import ox.stackgame.stackmachine.instructions.*;

public class StackProgram<E> {
    private final List<Instruction<E>> instructions;
    
    public StackProgram() {
	instructions = new ArrayList<Instruction<E>>();
    }
    
    public StackProgram(String source) {
	instructions = new ArrayList<Instruction<E>>();
    }
    
    public StackProgram(List<Instruction<E>> instructions) {
	this.instructions = instructions;
    }
    
    public int countInstructions() {
	return instructions.size();
    }
    
    public Instruction<E> instructionAt(int index) {
	return instructions.get(index);
    }
    
    public int getLabelPosition(String identifier) {
	for (int i = 0; i < instructions.size(); i++) {
	    Instruction<E> instruction = instructions.get(i);
	    if (instruction instanceof LabelInstruction<?> && ((LabelInstruction<E>) instruction).getIdentifier().equals(identifier))
		return i;
	}
	return -1;
    }
    
}
