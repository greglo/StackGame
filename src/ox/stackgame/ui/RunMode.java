/**
 * 
 */
package ox.stackgame.ui;

import ox.stackgame.stackmachine.StackMachine;

/**
 * Executes the machine passed to it (either stepping through or all at once)
 * @author danfox
 */
public class RunMode extends Mode {
	private StackMachine stackMachine = null;
	// Invariant: stackMachine is valid while RunMode is the active mode
	
	public void setMachine(StackMachine m){
		this.stackMachine = m;
	}
	
	public StackMachine getMachine(){
		return this.stackMachine;
	}
}
