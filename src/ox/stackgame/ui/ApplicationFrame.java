package ox.stackgame.ui;

import java.awt.*;

import javax.swing.*;



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
		ModeManager modeManager = new ModeManager();
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
		
		// fake ChallengeUI
		JPanel cc = new JPanel();
		cc.setBackground(Color.white);
		cc.setSize(new Dimension(250,h));
	    cc.setLocation(0,0);
		contentPane.add(cc);
		
		
		// StoreUI
		{
			JComponent u = new StoreUI(modeManager);
			u.setLocation(1000-u.getWidth()-p,p);
			contentPane.add(u);
		}
		
		// ProgramUI
		{
			JComponent u = new ProgramTextUI(modeManager, runMode);
			u.setLocation(250,0);
			contentPane.add(u, new Integer(0));
		}
		
		// fake TapeUI
		JPanel tape = new JPanel();
		tape.setBackground(caBlue2L);
		tape.setSize(new Dimension(750-2*p,50));
		tape.setLocation(250+p,h-p -70);
		contentPane.add(tape, new Integer(1));	// layer 1 is on top of 0	
		
		// fake StackUI
		JPanel u = new JPanel();
		u.setBackground(caBlueL);
		u.setSize(new Dimension(300,h));
		u.setLocation(700,0);
		contentPane.add(u);
		
		frame.setVisible(true);
	}

}
