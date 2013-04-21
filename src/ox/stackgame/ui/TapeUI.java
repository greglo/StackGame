package ox.stackgame.ui;

import java.awt.Dimension;

import javax.swing.*;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;

/**
 * Visualisation of the input and output tapes.  Allows user input in design mode only.  
 * Displays current read head during RunMode.
 * @author danfox
 *
 */
public class TapeUI extends JPanel {

	private StackMachine activeMachine = null;
	private static int boxSize = 50;
	
	private ModeVisitor modeActivationVisitor = new ModeVisitor(){
		public void visit(Mode m) {	}		
		
		public void visit(DesignMode m){
			// TODO make input tape editable
		}
		
		public void visit(RunMode m){
			// start listening to current machine
			activeMachine = m.getMachine();
			activeMachine.addListener(l);
			// TODO Cursor on first input 
		}
	};
	
	private ModeVisitor modeDeactivationVisitor = new ModeVisitor(){
		public void visit(Mode m) {}
		
		public void visit(DesignMode m){
			// TODO make input tape uneditable
		}
		
		public void visit(RunMode m){
			// stop listening to machine
			activeMachine.removeListener(l);
			activeMachine=null;
			// TODO hide cursor
		}
	};
	
	private StackMachineListener l = new StackMachineListenerAdapter() {
		public void inputConsumed(int startIndex) {
			// TODO update input tape, move cursor
		}
		
		public void outputChanged() {
			// TODO update output tape
		}		
	};
	
	public TapeUI(StateManager modeManager){
		
		// pay attention to mode changes
		modeManager.registerModeActivationVisitor(modeActivationVisitor);
		modeManager.registerModeDeactivationVisitor(modeDeactivationVisitor);
		
		// sort out appearance
		this.setBackground(ApplicationFrame.caBlue2L);
		this.setSize(new Dimension(750-2*ApplicationFrame.p,50));
		
		// TODO create scrolling (two scrollbars?) monotype font etc, different colours for input/output
		
	}
}
