package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;

/**
 * Allow the user to select a challenge from a predefined list.  Listen to the machine during RunMode, evaluating it against the 
 * current challenge success criteria and displaying either success or failure messages as appropriate.  Also constrains execution
 * to the challenge's specified maxInstructions, stackSize and allowedInstructions.
 * @author danfox
 *
 */
public class ChallengeUI extends JPanel {
	
	private StackMachineListener l = new StackMachineListenerAdapter(){
		public void programCounterChanged(int line) {
			// TODO Check execution hasn't exceeded allowedInstructions
			// TODO evaluate machine against challenge's hasSucceeded() function
		}
		
		// TODO check the stacksize hasn't exceeded challenge's specified stackSize.
	};
	
	public ChallengeUI(StateManager m){
		
		// TODO connect up the listener appropriately
		
		// TODO decide how mode switching should work. (Challenge must be selected first!)
		// TODO what happens to this when it's not in challenge mode?
		
		this.setLayout(new FlowLayout());
		this.setBackground(Color.white);
		this.setSize(new Dimension(250,ApplicationFrame.h));
	    this.setBorder(new EmptyBorder(15,15,15,15));
	    JLabel l = new JLabel("Challenge");
	    l.setFont(new Font("Helvetica Neue", Font.BOLD,23));
	    l.setForeground(new Color(66,66,66));
	    this.add(l);
	    JLabel l2 = new JLabel("<html><body style=\"width:150px;padding-top:5px;font:11px 'Helvetica Neue';\">Lorem " +
	    		"ipsum dolor sit amet, consectetur adipiscing elit. Praesent risus nisl, " +
	    		"hendrerit vitae facilisis et, rutrum nec diam. Class aptent taciti sociosqu ad litora " +
	    		"torquent per conubia nostra, per inceptos himenaeos. Praesent facilisis convallis accumsan. </body></html>");
	    this.add(l2);
	}
}
