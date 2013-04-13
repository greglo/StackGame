package ox.stackgame.ui;

import java.awt.*;

import javax.swing.*;



public class ApplicationFrame {
	final static int h = 660;
	final static int p =15;
	final static Color codeacademyBlue = new Color(35,44,49);
	final static Color caBlueL = new Color(50,57,60);
	final static Color caBlue2L = new Color(82,88,92);
	final static Color codeacademyBlack = new Color(33,33,33);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// initialise modes
		ModeManager modeManager = new ModeManager();
		Mode runMode = new RunMode();
		Mode freeDesignMode = new FreeDesignMode();
		Mode challengeMode = new ChallengeMode();
		modeManager.setActiveMode(challengeMode);
		
		// setup window to display
		final JFrame frame = new JFrame("Stack Game");
		frame.setBounds(200, 100, 1000, h);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// window contents
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new OverlayLayout(contentPane){
			public void layoutContainer(Container target) { 
				for (Component c : target.getComponents())
		            c.setBounds(c.getLocation().x, c.getLocation().y, c.getPreferredSize().width, c.getPreferredSize().height);   
		    }  
		});
		frame.add(contentPane);
		
		// fake ChallengeUI
		JPanel cc = new JPanel();
		cc.setBackground(Color.white);
		cc.setPreferredSize(new Dimension(250,h));
	    	cc.setLocation(0,0);
		contentPane.add(cc);
		
		
		// StoreUI
		{
			JComponent u = new StoreUI(modeManager);
			u.setLocation(1000-u.getPreferredSize().width-p,p);
			contentPane.add(u);
		}
		
		// ProgramUI
		{
			JComponent u = new ProgramTextUI(modeManager);
			u.setLocation(250,0);
			contentPane.add(u);
			contentPane.setComponentZOrder(u, 1);
		}
		
		// fake TapeUI
		JPanel tape = new JPanel();
		tape.setBackground(caBlue2L);
		tape.setPreferredSize(new Dimension(750-2*p,50));
		tape.setLocation(250+p,h-p -70);
		contentPane.add(tape);
		contentPane.setComponentZOrder(tape, 0);
		
		
		// fake StackUI
		JPanel u = new JPanel();
		u.setBackground(caBlueL);
		u.setPreferredSize(new Dimension(300,h));
		u.setLocation(700,0);
		contentPane.add(u);
		
		frame.setVisible(true);
	}

}
