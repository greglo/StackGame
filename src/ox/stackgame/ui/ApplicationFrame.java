package ox.stackgame.ui;

import java.awt.*;

import javax.swing.*;



public class ApplicationFrame {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		int h = 660;
		int p =15;
		Color codeacademyBlue = new Color(35,44,49);
		Color caBlueL = new Color(50,57,60);
		Color caBlue2L = new Color(82,88,92);
		Color codeacademyBlack = new Color(33,33,33);
		
		
		JFrame frame = new JFrame("Stack Game");
		frame.setBounds(200, 100, 1000, h);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// window contents
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.magenta);
		
		LayoutManager ol = new OverlayLayout(contentPane){
			public void layoutContainer(Container target) {  
		        for (int i = 0; i < target.getComponentCount(); i++){  
		            Component c = target.getComponent(i);  
		            c.setBounds(c.getLocation().x, c.getLocation().y, c.getPreferredSize().width, c.getPreferredSize().height);  
		        }  
		    }  
		};
		contentPane.setLayout(ol);
		
		frame.add(contentPane);
		
		// fake ChallengeUI
		JPanel cc = new JPanel();
		cc.setBackground(Color.white);
		cc.setPreferredSize(new Dimension(250,h));
	    	cc.setLocation(0,0);
		contentPane.add(cc);
		
		
		// fake StoreUI
		JPanel storeui = new JPanel();
		storeui.setBackground(caBlue2L);
		storeui.setPreferredSize(new Dimension(50,200));
		storeui.setLocation(950-p,p);
		contentPane.add(storeui);
		
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
