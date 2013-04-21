/**
 * 
 */
package ox.stackgame.stackmachine;

import java.util.List;

import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * Blank listener to make overriding one listener method easier.
 * @author danfox
 *
 */
public class StackMachineListenerAdapter implements StackMachineListener {
	public void programCounterChanged(int line) { }
	
	public void storeChanged(int address) { }

	public void inputConsumed(int startIndex) { }
	
	public void outputChanged() { }

	@Override
	public void stackInstructionsChanged(List<Instruction> instructions) {
	    
	}
}
