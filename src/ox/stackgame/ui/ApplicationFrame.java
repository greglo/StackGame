package ox.stackgame.ui;

import java.awt.*;

import javax.swing.*;



public class ApplicationFrame {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Stack Game");
		frame.setBounds(200, 100, 1000, 760);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// window contents
		JPanel contentPane = new JPanel(new BorderLayout());
		Color codeacademyBlue = new Color(41,56,61);
		contentPane.setBackground(codeacademyBlue);
		frame.setContentPane(contentPane);
		
		// fake ControllerComponent
		JPanel cc = new JPanel();
		cc.setBackground(Color.white);
		cc.setPreferredSize(new Dimension(250,100));
		contentPane.add(cc, BorderLayout.WEST);
		
		frame.setVisible(true);
	}

}
