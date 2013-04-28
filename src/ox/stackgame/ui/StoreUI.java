/**
 * 
 */
package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.exceptions.InvalidAddressException;

/**
 * A basic visualisation of the store. It is disabled during any DesignMode, but
 * listens to the active machine in RunMode. No animation yet
 * 
 * @author danfox
 * 
 */
public class StoreUI extends JPanel {
    private JLabel label;
    private final StackMachine activeMachine;
    private static int BOX_WIDTH = 80;
    private static int BOX_HEIGHT = 30;
    private static Color EVEN_COLOR = ApplicationFrame.caBlueL;
    private static Color ODD_COLOR = ApplicationFrame.caBlue2L;
    
    public StoreUI(StateManager m) {
        activeMachine = m.stackMachine;
        activeMachine.addListener(l);
        
        label = new JLabel("Store:", SwingConstants.CENTER);
        label.setBounds(0, 0, BOX_WIDTH, 25);
        this.setBackground(ApplicationFrame.caBlue2L);
        this.setSize(new Dimension(BOX_WIDTH, 25 + BOX_HEIGHT * StackMachine.STORE_SIZE));        
        this.add(label);
    }

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void storeChanged(int address) {
            validate();
            repaint();
        }
    };
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int yOffset = 25;
        g2d.setFont(new Font(g2d.getFont().getName(), Font.PLAIN, 20));
        
        try {
            for (int i = 0; i < activeMachine.STORE_SIZE; i++) {
                g2d.setColor((i % 2 == 0) ? EVEN_COLOR : ODD_COLOR);
                g2d.fillRect(0, yOffset, BOX_WIDTH, BOX_HEIGHT);
                
                String string = i + ": " + activeMachine.getStore(i).toString();
                System.out.println(string);
                Rectangle2D stringBox = g2d.getFontMetrics().getStringBounds(string, g2d);
                int xStart = (BOX_WIDTH - (int)stringBox.getWidth()) / 2;
                int yStart = ((BOX_HEIGHT - (int)stringBox.getHeight()) / 2);
                g2d.setColor((i % 2 == 0) ? ODD_COLOR : EVEN_COLOR);
                g2d.drawString(string, xStart, yOffset + yStart + g2d.getFontMetrics().getAscent());
                yOffset += BOX_HEIGHT;
            }
        }
        catch (InvalidAddressException e) {  }
    }
}
