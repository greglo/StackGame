/**
 * 
 */
package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

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
    private static int BOX_HEIGHT = 50;
    private static Color EVEN_COLOR = ApplicationFrame. caBlueL;
    private static Color ODD_COLOR = ApplicationFrame. caBlue2L;
    
    public StoreUI(StateManager m) {
        activeMachine = m.stackMachine;
        activeMachine.addListener(l);

        this.setSize(new Dimension(BOX_WIDTH, BOX_HEIGHT * StackMachine.STORE_SIZE));

        label = new JLabel("Store:");
        this.add(label);
    }

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void storeChanged(int address) {
            repaint();
        }
    };

    private void redrawStore() {
        // Redraw the entire control
        String s = "";
        try {
            for (int i = 0; i < activeMachine.STORE_SIZE; i++) {
                // get values from the store.
                s = s + "(" + i + ")";
                if (activeMachine.getStore(i) != null)
                    s = s + activeMachine.getStore(i);
                s = s + "\n";
            }
        } catch (InvalidAddressException e) {
            
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        try {
            for (int i = 0; i < activeMachine.STORE_SIZE; i++) {
                int yOffset = i * BOX_HEIGHT;
                g2d.setColor((i % 2 == 0) ? EVEN_COLOR : ODD_COLOR);
                g2d.fillRect(0, yOffset, BOX_WIDTH, BOX_HEIGHT);
                
                String string = activeMachine.getStore(i).toString();
                Rectangle2D stringBox = g2d.getFontMetrics().getStringBounds(string, g2d);
                int xStart = (BOX_WIDTH - (int)stringBox.getWidth()) / 2;
                int yStart = ((BOX_HEIGHT - (int)stringBox.getHeight()) / 2 )+ g2d.getFontMetrics().getAscent();
                g2d.drawString(activeMachine.getStore(i).toString(), xStart, yOffset - yStart);
            }
        }
        catch (InvalidAddressException e) {  }
    }
}
