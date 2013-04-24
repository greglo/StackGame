/**
 * 
 */
package ox.stackgame.ui;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.exceptions.InvalidAddressException;

/**
 * A basic visualisation of the store.  
 * It is disabled during any DesignMode, but listens to the active machine in RunMode.  
 * No animation yet
 * @author danfox
 *
 */
@SuppressWarnings("serial")
public class StoreUI extends JPanel {
	
	private JLabel label;
	private StackMachine activeMachine = null;
	private static int boxSize = 50;
	
	private ModeVisitor modeActivationVisitor = new ModeVisitor(){
		public void visit(Mode m) {	}		
		
		public void visit(RunMode m){
			// change appearance to awaken. maybe redraw? 
			refillLabel();
		}
	};
	
	private ModeVisitor modeDeactivationVisitor = new ModeVisitor(){
		public void visit(Mode m) {}
		
		public void visit(RunMode m){			
			label.setText("[asleep]");
		}
	};
	
	private StackMachineListener l = new StackMachineListenerAdapter() {
		public void storeChanged(int address) {
			refillLabel();
		}		
	};
	
	private void refillLabel(){
		// update the whole thing:
		String s = "";
		try {
			for (int i=0;i<activeMachine.STORE_SIZE;i++)
				s  = s +"("+i+")" +activeMachine.getStore(i).toString() + "\n";
		} catch (InvalidAddressException e) { 
			// just to make the error go away. 
		}
		label.setText(s);
	}
	
	// override painting?
	
	public StoreUI(StateManager m){
		
		// pay attention to mode changes
		m.registerModeActivationVisitor(modeActivationVisitor);
		m.registerModeDeactivationVisitor(modeDeactivationVisitor);
		
		// listen to the stack machine
		m.stackMachine.addListener(l);
		
		this.setBackground(ApplicationFrame.caBlue2L);
		this.setSize(new Dimension(boxSize,boxSize * StackMachine.STORE_SIZE));
		
		label = new JLabel("[store stuff goes here]");
		this.add(label);
	}
}
