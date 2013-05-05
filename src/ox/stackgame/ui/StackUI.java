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
    private final int BLOCK_WIDTH = ApplicationFrame.RIGHT_PANEL_WIDTH - 2 * ApplicationFrame.p;
    private final int BLOCK_HEIGHT = 33;
    private final int PADDING = 5;

    private EvaluationStack stack;

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void stackChanged(EvaluationStack s) {
            stack = s;

            setPreferredSize(new Dimension(ApplicationFrame.RIGHT_PANEL_WIDTH, Math.max(ApplicationFrame.STACK_HEIGHT, stack.size() * BLOCK_HEIGHT)));

            revalidate();
            repaint();
        }
    };

    public StackUI(StateManager m) {
        // listen to the stack machine
        m.stackMachine.addListener(l);

        this.setBackground(ApplicationFrame.caBlueL);
        this.setSize(new Dimension(ApplicationFrame.RIGHT_PANEL_WIDTH, ApplicationFrame.h));

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
        g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 20));

        int i = 0;
        for (StackValue<?> v : stack) {
            int y = (i) * (BLOCK_HEIGHT + PADDING) + TEXT_HEIGHT + 10;

            g.setColor(new Color(120, 120, 35));
            g.fillRect((ApplicationFrame.RIGHT_PANEL_WIDTH - BLOCK_WIDTH) / 2, y, BLOCK_WIDTH, BLOCK_HEIGHT);
            g.setColor(new Color(150, 150, 45));
            g.fillRect((ApplicationFrame.RIGHT_PANEL_WIDTH - BLOCK_WIDTH) / 2 + 1, y + 1, BLOCK_WIDTH - 2, BLOCK_HEIGHT - 2);

            String valueString = v.toString();
            Rectangle2D valueBox = g.getFontMetrics().getStringBounds(valueString, g);
            int valueX = (ApplicationFrame.RIGHT_PANEL_WIDTH - (int) valueBox.getWidth()) / 2;
            int valueY = ((BLOCK_HEIGHT - (int) valueBox.getHeight()) / 2);
            g.setColor(new Color(51, 68, 46));
            g.drawString(valueString, valueX, y + valueY + g.getFontMetrics().getAscent());

            i++;
        }
    }
}
