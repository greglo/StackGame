/**
 * 
 */
package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.StackValue;

/**
 * @author danfox Visualisation of the evaluation stack of the machine. Scolls.
 */
@SuppressWarnings("serial")
public class StackUI extends JPanel {
    private final int TEXT_HEIGHT = 30;
    private final int BLOCK_WIDTH = ApplicationFrame.RIGHT_PANEL_WIDTH - 30;
    private final int BLOCK_HEIGHT = 23;
    private final int PADDING = 5;

    private EvaluationStack stack;

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void stackChanged(EvaluationStack s) {
            stack = s;

            setPreferredSize(new Dimension(ApplicationFrame.RIGHT_PANEL_WIDTH,
                    Math.max(ApplicationFrame.STACK_HEIGHT, stack.size()
                            * BLOCK_HEIGHT)));

            revalidate();
            repaint();
        }
    };

    public StackUI(StateManager m) {
        // listen to the stack machine
        m.stackMachine.addListener(l);

        this.setBackground(ApplicationFrame.caBlueL);
        this.setSize(new Dimension(ApplicationFrame.RIGHT_PANEL_WIDTH,
                ApplicationFrame.h));
        
        JLabel label = new JLabel("Evaluation Stack:", SwingConstants.CENTER);
        label.setBounds(0, 0, ApplicationFrame.RIGHT_PANEL_WIDTH, TEXT_HEIGHT);
        label.setFont(this.getFont().deriveFont(20f));
        label.setForeground(ApplicationFrame.caBlue2L);
        this.add(label);
    }

    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (stack == null) {
            return;
        }

        Graphics2D g = (Graphics2D) graphics;
        
        

        int i = 0;
        int size = stack.size();
        for (StackValue<?> v : stack) {
            int y = (size - i) * (BLOCK_HEIGHT + PADDING) + TEXT_HEIGHT + 10;

            g.setColor(new Color(100, 100, 30));
            g.fillRect((ApplicationFrame.RIGHT_PANEL_WIDTH - BLOCK_WIDTH) / 2, y, BLOCK_WIDTH, BLOCK_HEIGHT);

            g.setColor(Color.WHITE);
            g.drawString(v.toString(), 5, y + 5
                    + g.getFontMetrics().getAscent());

            i++;
        }
    }
}
