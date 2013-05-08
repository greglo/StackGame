package ox.stackgame.blockUI;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import ox.stackgame.blockUI.BlockManager.BlockManagerListener;
import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.instructions.Instruction;
import ox.stackgame.ui.RunMode;
import ox.stackgame.ui.StateManager;

/**
 * The main file of the Block visualisation
 * 
 * To instantiate properly: Create a BlockManager Create a StateManager
 * Instantiate BlockUI(blockManager, stateManager) (in progress) Instantiate
 * CreatePanel(blockManager) (in progress) Instantiate EditPanel(blockManager)
 * (in progress) Instantiate DeletePanel(blockManager)
 * 
 */

@SuppressWarnings("serial")
public class BlockUI extends JPanel implements Scrollable{
    protected final GeneralListener generalListener;
    protected final EventHandler nullHandler;
    protected final MouseHandler editHandler;
    protected final MouseHandler createHandler;
    protected final MouseHandler deleteHandler;
    protected final SelectionManager selectionManager;
    protected final BlockManager blockManager;
    protected final StateManager stateManager;
    protected EventHandler currentEventHandler;

    protected StackMachine currentStackMachine;
    protected BlockUI blockui; // = this

    // holds the width and the height of a particular cell of the grid
    public final static int CELLHEIGHT = 40;
    public final static int CELLWIDTH = 100;

    // convert the grid dimensions to the actual display dimensions of the cave
    public static Dimension getRealBlockSize(Dimension viewSize) {
        return new Dimension(viewSize.width * CELLWIDTH, viewSize.height * CELLHEIGHT);
    }

    // get the first (upper left) point of the cell
    public static Point getLocationOfCell(int x, int y) {
        return new Point(x * CELLWIDTH, y * CELLHEIGHT);
    }

    public static Point getCellAtPoint(Point point) {
        return new Point(point.x / CELLWIDTH, point.y / CELLHEIGHT);
    }

    public BlockUI(BlockManager blockManager, StateManager stateManager) {
        blockui = this;
        BlockEventForwarder forwarder = new BlockEventForwarder();
        addMouseListener(forwarder);
        addMouseMotionListener(forwarder);
        addKeyListener(forwarder);

        // keeps the view in sync with many other components
        generalListener = new GeneralListener();

        nullHandler = NullHandler.INSTANCE;
        editHandler = new EditHandler(this);
        createHandler = new CreateHandler(this);
        deleteHandler = new DeleteHandler(this);
        currentEventHandler = editHandler;
        this.selectionManager = new SelectionManager();
        this.blockManager = blockManager;
        blockManager.addListener(generalListener);
        this.stateManager = stateManager;

        updateCurrentStackMachine();
        updateEventHandler();

        // visual stuff
        setFocusable(true);
        repaint();
    }

    // Controller-oriented methods

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public StackMachine getCurrentStackMachine() {
        return currentStackMachine;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    /** Retrieves the Instruction at a given coordinate of the grid */
    protected Instruction getInstructionAt(int x, int y) {
        if (currentStackMachine != null){
            List<Instruction> instructions = currentStackMachine.getInstructions();
            if(y >=0 && y<instructions.size())
                return instructions.get(y);
            else 
                return null;
        }else
            return null;
    }

    protected void updateEventHandler() {
        if (currentStackMachine == null)
            currentEventHandler = NullHandler.INSTANCE;
        else {
            String mode = blockManager.getMode();
            if (mode == BlockManager.EDIT)
                currentEventHandler = editHandler;
            else if (mode == BlockManager.CREATE)
                currentEventHandler = createHandler;
            else if (mode == BlockManager.DELETE)
                currentEventHandler = deleteHandler;
            else
                throw new IllegalArgumentException();
        }
        currentEventHandler.makeActive();
    }

    /**
     * Keeps the view synchronised with the Application State Model and Domain
     * Model
     */
    // basically, whatever relevant happens, repaint.
    protected class GeneralListener implements StackMachineListener, SelectionManagerListener, BlockManagerListener {

        // BlockManagerListener
        public void instructionChanged(String e) {
        }

        public void instructionCleared() {
        }

        public void modeChanged(String s) {
            updateEventHandler();
            repaint();
        }
        public void instructionUsed(String s){
        }

        // SelectionManagerListener
        public void objectsSelected(Collection<? extends Instruction> objects) {
            repaint();
        }

        public void objectsUnselected(Collection<? extends Instruction> objects) {
            repaint();
        }

        public void selectionCleared() {
            repaint();
        }

        // StackMachineListener
        public void storeChanged(int address) {
        }

        public void inputConsumed(int startIndex) {
        }

        public void programChanged(List<Instruction> instructions) {
            updateSize();
            revalidate();
            repaint();
        }

        public void stackChanged(EvaluationStack stack) {
        }

        public void outputChanged(Iterator<StackValue<?>> outputs) {
        }

        public void machineReset() {
            updateCurrentStackMachine();
            updateEventHandler();
            revalidate();
            repaint();
        }

        public void programCounterChanged(int line, Instruction instruction) {
            repaint();
        }

    }

    /** Display error messages */
    public void displayError(String text) {
        JOptionPane.showMessageDialog(blockui, text, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Forwards input events to the responsible handlers */
    protected class BlockEventForwarder implements MouseListener, MouseMotionListener, KeyListener {

        public void mouseEntered(MouseEvent e) {
            currentEventHandler.mouseEntered(e);
        }

        public void mouseExited(MouseEvent e) {
            currentEventHandler.mouseExited(e);
        }

        public void mouseClicked(MouseEvent e) {
            currentEventHandler.mouseClicked(e);
        }

        public void mousePressed(MouseEvent e) {
            currentEventHandler.mousePressed(e);
        }

        public void mouseReleased(MouseEvent e) {
            currentEventHandler.mouseReleased(e);
        }

        public void mouseDragged(MouseEvent e) {
            currentEventHandler.mouseDragged(e);
        }

        public void mouseMoved(MouseEvent e) {
            currentEventHandler.mouseMoved(e);
        }

        public void keyPressed(KeyEvent e) {
            currentEventHandler.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            currentEventHandler.keyReleased(e);
        }

        public void keyTyped(KeyEvent e) {
            currentEventHandler.keyTyped(e);
        }
    }

    // View-oriented methods

    protected void updateCurrentStackMachine() {
        StackMachine newMachine = stateManager.stackMachine;
        if (newMachine != currentStackMachine) {
            if (currentStackMachine != null)
                currentStackMachine.removeListener(generalListener);
            currentStackMachine = newMachine;
            currentStackMachine.addListener(generalListener);
        }

        updateSize();
        updateEventHandler();
        repaint();
    }
    
    public Dimension updateSize(){
        int stackMachineSize = (currentStackMachine == null ? 0 : currentStackMachine.getInstructions().size());
        
        Boolean bol = stateManager.getActiveMode() != null && stateManager.getActiveMode().getClass() == RunMode.class;
        //about-to-be-run line of code
        int high = (bol ? currentStackMachine.getProgramCounter() : 0);
        //one more line depending on whether it is RunMode or not
        int one = (bol ? 0 : 1);
        
        Dimension size = new Dimension(CELLWIDTH, CELLHEIGHT*(stackMachineSize-high+one));
        this.setPreferredSize(size);
        if(bol)revalidate();
        return size;
    }

    public void paintComponent(Graphics g) {
        Dimension size = updateSize();
        
        Graphics2D g2d = (Graphics2D) g;

        Color oldColor = g2d.getColor();
        Shape oldClip = g2d.getClip();

        // background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size.width, size.height);

        // the stuff
        if (currentStackMachine != null) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, size.width, size.height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(0, 0, size.width, size.height);

            // paint mask
            g2d.clipRect(1, 1, size.width - 2, size.height - 2);

            // paint instructions starting from the about-to-be-run instruction in run mode.
            int high = (stateManager.getActiveMode().getClass() == RunMode.class ? currentStackMachine.getProgramCounter() : 0);
            int j = 0;
            for (Instruction i : currentStackMachine.getInstructions()) {
                if(j >= high)
                    InstructionPainter.INSTANCE.paint(g2d, i, 0, j-high);
                j += 1;
            }

        } else {
            g2d.setColor(Color.BLACK);
            g.drawString("No-machine error.", 50, 50);
        }

        // paints mode-related graphics, such as selection boxes and game-over
        // screens
        currentEventHandler.paint(g2d);

        // restore the original state of the Graphics object
        g2d.setColor(oldColor);
        g2d.setClip(oldClip);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return updateSize();
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
        return CELLHEIGHT;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
        return CELLHEIGHT;
    }

}
