package ox.stackgame.stackmachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ox.stackgame.stackmachine.instructions.*;

public class StackProgram {
    private final List<Operation> instructions;
    private final Map<String, Integer> labels;
    
    public StackProgram() {
	instructions = new ArrayList<Operation>();
	labels = new HashMap<String, Integer>();
    }
    
    public StackProgram(String source) {
	this();
    }
    
    public StackProgram(List<Operation> instructions) {
	this.instructions = instructions;
	labels = new HashMap<String, Integer>();

	int i = 0;
	for( Operation op : instructions ) {
		if( op.name == "label" ) {
			labels.put( ( String ) op.args.get( 0 ).getValue(), i );
		}

		i++;
	}
    }
    
    public List<Instruction> getInstructions() {
	return instructions;
    }
    
    public int countInstructions() {
	return instructions.size();
    }
    
    public Operation instructionAt(int index) {
	return instructions.get(index);
    }
    
    public int getLabelPosition(String identifier) {
	if (labels.containsKey(identifier))
	    return labels.get(identifier);
	else
	    return -1;
    }
    
}
