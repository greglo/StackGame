/**
 * 
 */
package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.StackValue;

/**
 * @author danfox Visualisation of the evaluation stack of the machine. Scolls.
 */
@SuppressWarnings("serial")
public class StackUI extends JPanel {
    private final int BLOCK_WIDTH = ApplicationFrame.RIGHT_PANEL_WIDTH - 10;
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
    }

    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (stack == null) {
            return;
        }

        Graphics2D g = (Graphics2D) graphics;

        int i = 0;
        for (StackValue<?> v : stack) {
            int y = i * (BLOCK_HEIGHT + PADDING);

            g.setColor(Color.RED);
            g.fillRect((ApplicationFrame.RIGHT_PANEL_WIDTH - BLOCK_WIDTH) / 2, y, BLOCK_WIDTH, BLOCK_HEIGHT);

            g.setColor(Color.WHITE);
            g.drawString(v.toString(), 5, y + 5
                    + g.getFontMetrics().getAscent());

            i++;
        }
    }
}
