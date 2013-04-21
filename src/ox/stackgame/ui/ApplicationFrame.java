package ox.stackgame.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ox.stackgame.stackmachine.StackMachine;



public class ApplicationFrame {
	final static int h = 660;
	final static int p =15;
	final static Color caBlue = new Color(35,44,49);
	final static Color caBlueL = new Color(50,57,60);
	final static Color caBlue2L = new Color(82,88,92);
	final static Color codeacademyBlack = new Color(33,33,33);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// initialise modes
		StateManager modeManager = new StateManager(new StackMachine(null)); // instantiate the StateManager with a 'blank' machine
		RunMode runMode = new RunMode();
		Mode freeDesignMode = new FreeDesignMode();
		Mode challengeMode = new ChallengeMode();
		modeManager.setActiveMode(challengeMode);
		
		// setup window to display
		final JFrame frame = new JFrame("Stack Game");
		frame.setBounds(200, 100, 1000, h);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// window contents
		JLayeredPane contentPane = new JLayeredPane();
		contentPane.setPreferredSize(new Dimension(1000,h));
		frame.add(contentPane,BorderLayout.CENTER);
		
		// ChallengeUI
		{
			JComponent u = new ChallengeUI(modeManager);
			u.setLocation(0,0);
			contentPane.add(u,new Integer(0));
		}
		
		// fake StackUI
		JPanel su = new JPanel();
		su.setBackground(caBlueL);
		su.setSize(new Dimension(300,h));
		su.setLocation(700,0);
		contentPane.add(su, new Integer(0));
		
		
		// StoreUI
		{
			JComponent u = new StoreUI(modeManager);
			u.setLocation(1000-u.getWidth()-p,p);
			contentPane.add(u, new Integer(1));
		}
		
		// ProgramUI
		{
			JComponent u = new ProgramTextUI(modeManager, runMode);
			u.setLocation(250,0);
			contentPane.add(u, new Integer(0));
		}
		
		// TapeUI	
		{
			JComponent u = new TapeUI(modeManager);
			u.setLocation(250+p,h-p-70); // no idea why this needs a 70.
			contentPane.add(u, new Integer(1));// layer 1 is on top of 0	
		}
		
		frame.setVisible(true);
	}

}