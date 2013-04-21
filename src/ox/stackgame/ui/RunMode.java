/**
 * 
 */
package ox.stackgame.ui;

import ox.stackgame.stackmachine.StackMachine;

/**
 * Executes the StateManager's StackMachine
 * @author danfox
 */
public class RunMode extends Mode {
	private StackMachine stackMachine = null;
	// Invariant: stackMachine is valid while RunMode is the active mode
	
//	public void setMachine(StackMachine m){
//		this.stackMachine = m;
//	}
//	
//	public StackMachine getMachine(){
//		return this.stackMachine;
//	}
}
