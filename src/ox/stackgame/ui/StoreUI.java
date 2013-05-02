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

import javax.swing.ImageIcon;
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
    private static int CONTROL_WIDTH    = 90;
    private static int TEXT_HEIGHT      = 20;
    private static int RAM_WIDTH        = 80;
    private static int RAM_HEIGHT       = 45;
    private static int Y_PADDING        = 15;

    
    public StoreUI(StateManager m) {
        activeMachine = m.stackMachine;
        activeMachine.addListener(l);
        
        label = new JLabel("Store:", SwingConstants.CENTER);
        label.setBounds(0, 0, CONTROL_WIDTH, 25);
        this.setBackground(ApplicationFrame.caBlueL);
        this.setSize(new Dimension(CONTROL_WIDTH, 25 + (RAM_HEIGHT + TEXT_HEIGHT + Y_PADDING) * StackMachine.STORE_SIZE));        
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
        int yOffset = 25 + Y_PADDING / 2;
        ImageIcon icon = new ImageIcon(getClass().getResource("/store.png"));
        
        try {
            for (int i = 0; i < activeMachine.STORE_SIZE; i++) {
                g2d.setColor(new Color(0,0,0));
                g2d.setFont(new Font(g2d.getFont().getName(), Font.PLAIN, 10));
                String addressString = "0x" + i;
                Rectangle2D addressBox = g2d.getFontMetrics().getStringBounds(addressString, g2d);
                int addressX = (CONTROL_WIDTH - (int)addressBox.getWidth()) / 2;
                int addressY = ((TEXT_HEIGHT - (int)addressBox.getHeight()) / 2);
                g2d.drawString(addressString, addressX, yOffset + addressY + g2d.getFontMetrics().getAscent());
                
                g.drawImage(icon.getImage(), (CONTROL_WIDTH - RAM_WIDTH) / 2, yOffset + TEXT_HEIGHT, RAM_WIDTH, RAM_HEIGHT, null);  
                
                g2d.setColor(ApplicationFrame.caBlue2L);
                g2d.setFont(new Font(g2d.getFont().getName(), Font.PLAIN, 20));
                String valueString = activeMachine.getStore(i).toString();
                Rectangle2D valueBox = g2d.getFontMetrics().getStringBounds(valueString, g2d);
                int valueX = (CONTROL_WIDTH - (int)valueBox.getWidth()) / 2;
                int valueY = (((RAM_HEIGHT - 5) - (int)valueBox.getHeight()) / 2) + TEXT_HEIGHT;
                g2d.drawString(valueString, valueX, yOffset + valueY + g2d.getFontMetrics().getAscent());
                
                yOffset += TEXT_HEIGHT + RAM_HEIGHT + Y_PADDING;
            }
        }
        catch (InvalidAddressException e) {  }
    }
}
