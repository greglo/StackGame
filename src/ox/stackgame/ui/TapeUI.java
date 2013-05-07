package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import ox.stackgame.stackmachine.CharStackValue;
import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.InvalidCharException;

/**
 * Visualisation of the input and output tapes. Allows user input in design mode
 * only. Displays current read head during RunMode.
 * 
 * @author rgossiaux
 * 
 */

@SuppressWarnings("serial")
public class TapeUI extends JPanel {

    private List<StackValue<?>> inputTape;
    private List<StackValue<?>> outputTape;
    private Map<StackValue<?>, Integer> sizeMap; // Maps values to the width of
                                                 // the blocks they create
    private final StackMachine machine;
    private static final int TEXT_WIDTH = 50;
    private static final int BOX_SIZE = 30;
    private static final int BOX_PADDING = 1;
    private static final int PADDING = 10;
    public static final int UIHeight = 2 * BOX_SIZE + 3 * PADDING;
    private static final Font font = new Font("Monospaced", Font.PLAIN, 12);
    private int addInputBoxX = PADDING; // Controls where we're looking for
                                        // input from
    private boolean editable = true;
    private ErrorUI eui;

    private ModeVisitor modeActivationVisitor = new ModeVisitor() {
        public void visit(RunMode m) {
            editable = false;
            m.machine.loadInput(inputTape);
            resetTapes();
        }

        public void visit(ChallengeMode m) {
            editable = false;
            setInput(m.getChallenge().inputs);
            repaint();
        }

        public void visit(FreeDesignMode m) {
            editable = true;
            repaint();
        }
    };

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void inputConsumed(int startIndex) {
            inputTape.remove(0);
            repaint();
            resize();
        }

        public void outputChanged(Iterator<StackValue<?>> outputs) {
            // TODO update output tape
            StackValue<?> mostRecent = null;
            while (outputs.hasNext())
                mostRecent = outputs.next();
            outputTape.add(mostRecent);
            repaint();
            resize();
        }

        public void machineReset() {
            resetTapes();
        }
    };
    
    private void resize() {
        this.repaint();
        int inputWidth = addInputBoxX + BOX_SIZE + BOX_PADDING;
        int outputWidth = 2 * PADDING + TEXT_WIDTH + BOX_PADDING;
        for (StackValue<?> v : outputTape) {
            try {
                outputWidth += sizeMap.get(v) + BOX_PADDING;
            } catch (RuntimeException e) {
                outputWidth += BOX_SIZE + BOX_PADDING;
            }
        }
        this.setPreferredSize(new Dimension(Math.max(ApplicationFrame.CENTER_PANEL_WIDTH + ApplicationFrame.RIGHT_PANEL_WIDTH, 
                Math.max(inputWidth, outputWidth)), UIHeight));
        this.revalidate();
        this.repaint();
    }

    public TapeUI(StateManager m, ErrorUI eui) {
        this.eui = eui;

        // pay attention to mode changes
        m.registerModeActivationVisitor(modeActivationVisitor);

        machine = m.stackMachine;
        resetTapes();

        // listen to the stack machine
        m.stackMachine.addListener(l);

        // sort out appearance
        this.setBackground(ApplicationFrame.caBlue2L);
        this.setSize(new Dimension(750 - 2 * ApplicationFrame.p, BOX_SIZE * 2
                + 3 * PADDING));
        
        sizeMap = new HashMap<StackValue<?>, Integer>();

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (editable && PADDING <= e.getY()
                        && e.getY() < PADDING + BOX_SIZE) {
                    int x = 2 * PADDING + TEXT_WIDTH;
                    int i = 0;
                    for (StackValue<?> v : inputTape) {
                        if (x <= e.getX() && e.getX() <= x + sizeMap.get(v)) {
                            getInput(i);
                            break;
                        }
                        x += BOX_PADDING + sizeMap.get(v);
                        ++i;
                    }
                    if (addInputBoxX <= e.getX()
                            && e.getX() <= addInputBoxX + BOX_SIZE)
                        getInput(-1);
                }
            }
        });

    }

    protected void getInput(int i) {
        String s;
        if (i == -1) {
            s = (String) JOptionPane.showInputDialog(this,
                    "Enter an input value to be added: ", "Input",
                    JOptionPane.PLAIN_MESSAGE, null, null, null);
        } else {
            s = (String) JOptionPane.showInputDialog(this,
                    "Enter a new value (enter nothing to delete): ", "Edit", JOptionPane.PLAIN_MESSAGE,
                    null, null, null);
        }
        if (s != null && s.length() > 0) {
            try {
                int x = Integer.parseInt(s);
                addInput(new IntStackValue(x), i);
            } catch (NumberFormatException e) {
                if (s.length() == 1) {
                    try {
                        addInput(new CharStackValue(s.charAt(0)), i);
                    } catch (InvalidCharException f) {
                        eui.displayError("\"" + s + "\" is not a valid stack value.");
                    }
                } else {
                    eui.displayError("\"" + s + "\" is not a valid stack value.");
                }
            }
        }
        else if (s != null && i != -1) {
            inputTape.remove(i);
            eui.clearError();
            repaint();
        }
        resize();
    }

    protected void addInput(StackValue<?> i, int loc) {
        // This stuff is magic so watch out
        // The repaint method updates the addInputBoxX (needed for mouse
        // handler), since
        // we need to know the width of the string to do so
        if (loc == -1) {
            inputTape.add(i);
        } else {
            inputTape.remove(loc);
            inputTape.add(loc, i);
        }
        eui.clearError();
        repaint();
        resize();
    }

    protected void resetTapes() {
        outputTape = new LinkedList<StackValue<?>>();
        setInput(machine.getInput());
        resize();
    }
    
    protected void setInput(List<StackValue<?>> input) {
        inputTape = new LinkedList<StackValue<?>>();
        for (StackValue<?> v : input)
            inputTape.add(v);
        resize();
    }

    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setFont(font);
        
        int yOffset = BOX_SIZE - (int) g.getFontMetrics().getStringBounds("Input:", g).getWidth() / 2 + g.getFontMetrics().getAscent();
        g.drawString("Input:", PADDING, PADDING + yOffset);
        g.drawString("Output:", PADDING, 2 * PADDING + BOX_SIZE + yOffset);

        // Draw input tape

        int x = 2 * PADDING + TEXT_WIDTH;
        for (StackValue<?> v : inputTape) {
            if (!sizeMap.containsKey(v)) {
                sizeMap.put(v, Math.max(BOX_SIZE, g.getFontMetrics().stringWidth(v.toString()) + 2
                        * BOX_SIZE / 3));
            }
            int thisWidth = sizeMap.get(v);

            g.setColor(new Color(100, 100, 100));
            g.fillRect(x, PADDING, thisWidth, BOX_SIZE);
            g.setColor(new Color(130, 130, 130));
            g.fillRect(x + 1, PADDING + 1, thisWidth - 2, BOX_SIZE - 2);

            g.setColor(Color.WHITE);
            g.drawString(v.toString(), x + BOX_SIZE / 3, PADDING
                    + (BOX_SIZE - g.getFontMetrics().getAscent()) / 2
                    + g.getFontMetrics().getAscent());

            x += thisWidth + BOX_PADDING;
        }
        // dumb place for this but w/e
        addInputBoxX = x;

        // Draw input box
        if (editable) {
            g.setColor(Color.gray);
            g.drawLine(addInputBoxX, PADDING + 1, addInputBoxX + BOX_SIZE - 1, PADDING + 1);
            g.drawLine(addInputBoxX, PADDING + 1, addInputBoxX, PADDING + BOX_SIZE - 1);
            g.drawLine(addInputBoxX + BOX_SIZE - 1, PADDING + 1, addInputBoxX + BOX_SIZE - 1,
                    BOX_SIZE + PADDING - 1);
            g.drawLine(addInputBoxX, BOX_SIZE + PADDING - 1, addInputBoxX + BOX_SIZE - 1,
                    BOX_SIZE + PADDING - 1);
        }

        // Draw output tape

        int j = 2 * PADDING + TEXT_WIDTH;
        for (StackValue<?> v : outputTape) {
            if (!sizeMap.containsKey(v)) {
                sizeMap.put(v, Math.max(BOX_SIZE, g.getFontMetrics().stringWidth(v.toString()) + 2
                        * BOX_SIZE / 3));
            }
            int thisWidth = sizeMap.get(v);

            g.setColor(new Color(100, 100, 100));
            g.fillRect(j, PADDING * 2 + BOX_SIZE, thisWidth, BOX_SIZE);
            g.setColor(new Color(130, 130, 130));
            g.fillRect(j + 1, PADDING * 2 + BOX_SIZE + 1, thisWidth - 2, BOX_SIZE - 2);

            g.setColor(Color.WHITE);
            g.drawString(v.toString(), j + BOX_SIZE / 3, PADDING * 2 + BOX_SIZE
                    + (BOX_SIZE - g.getFontMetrics().getAscent()) / 2
                    + g.getFontMetrics().getAscent());

            j += thisWidth + BOX_PADDING;
        }
    }

}
