package ox.stackgame.stackmachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ox.stackgame.stackmachine.instructions.*;

public class StackProgram {
    private final List<Instruction> instructions;
    private final Map<String, Integer> labels;
    
    public StackProgram() {
	instructions = new ArrayList<Instruction>();
	labels = new HashMap<String, Integer>();
    }
    
    public StackProgram(String source) {
	this();
    }
    
    public StackProgram(List<Instruction> instructions) {
	this.instructions = instructions;
	labels = new HashMap<String, Integer>();
	int index = 0;
	for (Instruction instr : instructions) {
	    if (instr instanceof LabelInstruction) {
		LabelInstruction lab = (LabelInstruction)instr;
		labels.put(lab.getIdentifier(), index);
	    }
	    index++;
	}
    }
    
    public int countInstructions() {
	return instructions.size();
    }
    
    public Instruction instructionAt(int index) {
	return instructions.get(index);
    }
    
    public int getLabelPosition(String identifier) {
	if (labels.containsKey(identifier))
	    return labels.get(identifier);
	else
	    return -1;
    }
    
}
