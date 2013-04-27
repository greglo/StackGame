package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ox.stackgame.challenge.AbstractChallenge;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;

/**
 * Allow the user to select a challenge from a predefined list. Listen to the
 * machine during RunMode, evaluating it against the current challenge success
 * criteria and displaying either success or failure messages as appropriate.
 * Also constrains execution to the challenge's specified maxInstructions,
 * stackSize and allowedInstructions.
 * 
 * @author danfox
 * 
 */
public class ChallengeUI extends JPanel {

	protected AbstractChallenge challenge;
	protected JLabel descLabel; 
	private StackMachineListener l = new StackMachineListenerAdapter() {
		public void programCounterChanged(int line) {
			// TODO Check execution hasn't exceeded allowedInstructions
			// TODO evaluate machine against challenge's hasSucceeded() function
		}

		// TODO check the stacksize hasn't exceeded challenge's specified
		// stackSize.
	};

	public ChallengeUI(StateManager m) {

		// TODO connect up the listener appropriately

		// TODO decide how mode switching should work. (Challenge must be
		// selected first!)
		// TODO what happens to this when it's not in challenge mode?

		m.registerModeActivationVisitor(modeActivationVisitor);
		m.registerModeDeactivationVisitor(modeDeactivationVisitor);

		this.setLayout(new FlowLayout());
		this.setBackground(Color.white);
		this.setSize(new Dimension(250, ApplicationFrame.h));
		this.setBorder(new EmptyBorder(15, 15, 15, 15));
		JLabel l = new JLabel("Challenge");
		l.setFont(new Font("Helvetica Neue", Font.BOLD, 23));
		l.setForeground(new Color(66, 66, 66));
		this.add(l);
		descLabel = new JLabel();
		this.add(descLabel);
	}

	private ModeVisitor modeActivationVisitor = new ModeVisitor() {

		public void visit(RunMode m) {
		}

		public void visit(ChallengeMode m) {
			challenge = m.challenge;
			descLabel.setText(challenge.description);
		}

        @Override
        public void visit(FreeDesignMode m) {
            // TODO Auto-generated method stub
            
        }
	};

	// code to be executed when a mode is deactivated
	private ModeVisitor modeDeactivationVisitor = new ModeVisitor() {
		public void visit(RunMode m) {
		}

        @Override
        public void visit(ChallengeMode m) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void visit(FreeDesignMode m) {
            // TODO Auto-generated method stub
            
        }
	};

}
