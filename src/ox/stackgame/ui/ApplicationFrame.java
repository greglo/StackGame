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
		JFrame frame = new JFrame("Stack Game");
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
		
		
		// fake StoreUI
		{
			JPanel u = new StoreUI(modeManager);
			u.setLocation(1000-u.getPreferredSize().width-p,p);
			contentPane.add(u);
		}
		
		// fake StackUI
		JPanel stackUI = new JPanel();
		stackUI.setBackground(caBlue2L);
		stackUI.setPreferredSize(new Dimension(750-2*p,50));
		stackUI.setLocation(250+p,h-p -70);
		contentPane.add(stackUI);
		
		// fake ProgramUI
		JPanel progui = new JPanel();
		progui.setBackground(codeacademyBlue);
		progui.setPreferredSize(new Dimension(450,h));
		progui.setLocation(250,00);
		contentPane.add(progui);
		
		
		// fake StackUI
		JPanel sw = new JPanel();
		sw.setBackground(caBlueL);
		sw.setPreferredSize(new Dimension(300,h));
		sw.setLocation(700,0);
		contentPane.add(sw);
		
		frame.setVisible(true);
	}

}
