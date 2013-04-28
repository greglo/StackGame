package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import ox.stackgame.stackmachine.StackMachine;

public class ButtonUI extends JPanel {
    public ButtonUI(StateManager m){

        this.setBackground(Color.RED);
        this.setSize(new Dimension(100,100));
    }
}
