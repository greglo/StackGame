/**
 * 
 */
package ox.stackgame.ui;

import java.awt.Color;
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
public class StoreUI extends JPanel {
	
	private JLabel label;
	private StackMachine activeMachine = null;
	
	private ModeVisitor modeActivationListener = new ModeVisitor(){
		public void visit(Mode m) {	}		
		
		public void visit(RunMode m){
			// wakeup, start listening to newly active machine.
			activeMachine = m.getMachine();
			activeMachine.addListener(l);
			// change appearance to awaken. maybe redraw? 
			refillLabel();
		}
	};
	
	private ModeVisitor modeDeactivationListener = new ModeVisitor(){
		public void visit(Mode m) {}
		
		public void visit(RunMode m){
			// stop listening.
			activeMachine.removeListener(l);
			activeMachine = null; // unnecessary, but makes sure no-one uses it afterwards
			
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
	
	
	public StoreUI(ModeManager m){
		
		// pay attention to mode changes
		m.registerModeActivationVisitor(modeActivationListener);
		m.registerModeDeactivationVisitor(modeDeactivationListener);
		
		// TODO sort out appearance based on StackMachine.STORE_SIZE 
		this.setPreferredSize(new Dimension(100,100));
		this.setBackground(Color.WHITE);
		
		label = new JLabel("[store stuff goes here]");
		this.add(label);
	}
}
