package ox.stackgame.blockUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** manages communication between block-based visual components (components in the BlockUI header) **/
public class BlockManager {
	
	//active mode
	//Use of strings for comfort reasons
	public static final String DELETE = "delete";
	public static final String EDIT = "edit";
	public static final String CREATE = "create";
	private String mode = EDIT;

	//available instructions in the CREATE mode
    public Map<String,Integer> availableInstructions = null;

	
	public String getMode(){return mode;}
	
	public void setMode(String mode){
		assert mode==DELETE || mode==EDIT || mode==CREATE;
		System.out.println("BlockManager mode: " + mode);
		this.mode=mode;
		for (BlockManagerListener l : listeners)
			l.modeChanged(mode);
	}
	
	
	//active instruction
	private String instruction = null;

	public String getInstruction(){return instruction;}
	
	public void setInstruction(String instruction){
		this.instruction=instruction;
		for (BlockManagerListener l : listeners)
			l.instructionChanged(this.instruction);
	}

	public void clearInstruction(){
		this.instruction=null;
		for (BlockManagerListener l : listeners)
			l.instructionCleared();
	}
	
	//signals that the instruction has been used - to inform CreatePanel
	public void useInstruction(){
        for (BlockManagerListener l : listeners)
            l.instructionUsed(instruction);
	}

	
	//listener boilerplate

	protected final List<BlockManagerListener> listeners;
	
	public BlockManager() {
		listeners = new ArrayList<BlockManagerListener>();
	}
	public void addListener(BlockManagerListener l) {
		listeners.add(l);
	}
	public void removeListener(BlockManagerListener l) {
		listeners.remove(l);
	}
	
	public interface BlockManagerListener{
		/** On change of instruction. We may assume that e is non-null **/
		public void instructionChanged(String s);
		/** Instruction set to null **/
		public void instructionCleared();
        /** On use of instruction.**/
        public void instructionUsed(String s);

        /** On change of mode.**/
        public void modeChanged(String s);
	}
	
}
