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
 * @author danfox
 * 
 */

@SuppressWarnings("serial")
public class TapeUI extends JPanel {

    private List<StackValue<?>> inputTape;
    private List<StackValue<?>> outputTape;
    private Map<StackValue<?>, Integer> sizeMap; // Maps values to the width of
                                                 // the blocks they create
    private final StackMachine machine;
    private static final int boxSize = 20;
    private static final int padding = 10;
    public static final int UIHeight = 2 * boxSize + 4 * padding;
    private static final Font font = new Font("Monospaced", Font.PLAIN, 12);
    private int addInputBoxX = padding; // Controls where we're looking for
                                        // input from
    private boolean editable = true;
    private ErrorUI eui;

    private ModeVisitor modeActivationVisitor = new ModeVisitor() {
        // TODO make input tape editable on DesignMode visitors

        public void visit(RunMode m) {
            editable = false;
            m.machine.loadInput(inputTape);
            resetTapes();
        }

        public void visit(ChallengeMode m) {
            editable = false;
            repaint();
        }

        public void visit(FreeDesignMode m) {
            editable = true;
            repaint();
        }
    };

    private ModeVisitor modeDeactivationVisitor = new ModeVisitor() {

        public void visit(RunMode m) {
        }

        public void visit(ChallengeMode m) {
        }

        public void visit(FreeDesignMode m) {
        }
    };

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void inputConsumed(int startIndex) {
            inputTape.remove(0);
            repaint();
        }

        public void outputChanged(Iterator<StackValue<?>> outputs) {
            // TODO update output tape
            StackValue<?> mostRecent = null;
            while (outputs.hasNext())
                mostRecent = outputs.next();
            outputTape.add(mostRecent);
            repaint();
        }

        public void machineReset() {
            resetTapes();
        }
    };

    public TapeUI(StateManager m, ErrorUI eui) {
        this.eui = eui;

        // pay attention to mode changes
        m.registerModeActivationVisitor(modeActivationVisitor);
        m.registerModeDeactivationVisitor(modeDeactivationVisitor);

        machine = m.stackMachine;
        resetTapes();

        // listen to the stack machine
        m.stackMachine.addListener(l);

        // sort out appearance
        this.setBackground(ApplicationFrame.caBlue2L);
        this.setSize(new Dimension(750 - 2 * ApplicationFrame.p, boxSize * 2
                + 4 * padding));
        
        sizeMap = new HashMap<StackValue<?>, Integer>();

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (editable && padding <= e.getY()
                        && e.getY() < padding + boxSize) {
                    int x = padding;
                    int i = 0;
                    for (StackValue<?> v : inputTape) {
                        if (x <= e.getX() && e.getX() <= x + sizeMap.get(v)) {
                            getInput(i);
                            break;
                        }
                        x += padding + sizeMap.get(v);
                        ++i;
                    }
                    if (addInputBoxX <= e.getX()
                            && e.getX() <= addInputBoxX + boxSize)
                        getInput(-1);
                }
            }
        });

        // TODO create scrolling (two scrollbars?) monotype font etc, different
        // colours for input/output

    }

    protected void getInput(int i) {
        String s;
        if (i == -1) {
            s = (String) JOptionPane.showInputDialog(this,
                    "Enter an input value to be added: ", "Input",
                    JOptionPane.PLAIN_MESSAGE, null, null, null);
        } else {
            s = (String) JOptionPane.showInputDialog(this,
                    "Enter a new value: ", "Edit", JOptionPane.PLAIN_MESSAGE,
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
        eui.clearErrors();
        repaint();
    }

    protected void resetTapes() {
        outputTape = new LinkedList<StackValue<?>>();
        inputTape = new LinkedList<StackValue<?>>();
        for (StackValue<?> v : machine.getInput())
            inputTape.add(v);
        repaint();
    }

    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setFont(font);

        // Draw input tape

        int x = padding;
        for (StackValue<?> v : inputTape) {
            if (!sizeMap.containsKey(v)) {
                sizeMap.put(v, g.getFontMetrics().stringWidth(v.toString()) + 2
                        * boxSize / 3);
            }
            int thisWidth = sizeMap.get(v);

            g.setColor(Color.gray);
            g.fillRect(x, padding, thisWidth, boxSize);

            g.setColor(Color.WHITE);
            g.drawString(v.toString(), x + boxSize / 3, padding
                    + (boxSize - g.getFontMetrics().getAscent()) / 2
                    + g.getFontMetrics().getAscent());

            x += thisWidth + padding;
        }
        // dumb place for this but w/e
        addInputBoxX = x;

        // Draw input box
        if (editable) {
            g.setColor(Color.gray);
            g.drawLine(addInputBoxX, padding, addInputBoxX + boxSize, padding);
            g.drawLine(addInputBoxX, padding, addInputBoxX, padding + boxSize);
            g.drawLine(addInputBoxX + boxSize, padding, addInputBoxX + boxSize,
                    boxSize + padding);
            g.drawLine(addInputBoxX, boxSize + padding, addInputBoxX + boxSize,
                    boxSize + padding);
        }

        // Draw output tape

        int j = padding;
        for (StackValue<?> v : outputTape) {
            if (!sizeMap.containsKey(v)) {
                sizeMap.put(v, g.getFontMetrics().stringWidth(v.toString()) + 2
                        * boxSize / 3);
            }
            int thisWidth = sizeMap.get(v);

            g.setColor(Color.gray);
            g.fillRect(j, padding * 3 + boxSize, thisWidth, boxSize);

            g.setColor(Color.WHITE);
            g.drawString(v.toString(), j + boxSize / 3, padding * 3 + boxSize
                    + (boxSize - g.getFontMetrics().getAscent()) / 2
                    + g.getFontMetrics().getAscent());

            j += thisWidth + padding;
        }
    }

}
