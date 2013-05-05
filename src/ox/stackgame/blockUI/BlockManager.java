package ox.stackgame.blockUI;

import java.util.ArrayList;
import java.util.List;

/** manages communication between block-based visual components (components in the BlockUI header) **/
public class BlockManager {
	
	//active mode
	//Use of strings for comfort reasons
	public static final String DELETE = "delete";
	public static final String EDIT = "edit";
	public static final String CREATE = "create";
	private String mode = EDIT;
	
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

		/** On change of mode.**/
		public void modeChanged(String s);

	}
	
}
