package ox.stackgame.blockUI;

import java.util.Collection;
import java.util.List;
import java.awt.Color;
import java.awt.Point;
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

import ox.stackgame.blockUI.BlockManager.BlockManagerListener;
import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.instructions.Instruction;
import ox.stackgame.ui.StateManager;


/**
 * The main file of the Block visualisation
 * 
 * To instantiate properly:
 * Create a BlockManager
 * Create a StateManager
 * Instantiate BlockUI(blockManager, stateManager)
 * (in progress) Instantiate CreatePanel(blockManager)
 * (in progress) Instantiate EditPanel(blockManager)
 * (in progress) Instantiate DeletePanel(blockManager)
 * 
 */

@SuppressWarnings("serial")
public class BlockUI extends JPanel {
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
    protected BlockUI blockui; //= this
    
    //holds the width and the height of a particular cell of the grid
    public final static int CELLHEIGHT = 40;
    public final static int CELLWIDTH = 100;
    
    //convert the grid dimensions to the actual display dimensions of the cave
    public static Dimension getRealBlockSize(Dimension viewSize){
        return new Dimension(viewSize.width*CELLWIDTH, viewSize.height*CELLHEIGHT);
    }
    
    //get the first (upper left) point of the cell
    public static Point getLocationOfCell(int x, int y){
        return new Point(x*CELLWIDTH, y*CELLHEIGHT);
    }
    public static Point getCellAtPoint(Point point){
        return new Point(point.x/CELLWIDTH,point.y/CELLHEIGHT);
    }

    public BlockUI(BlockManager blockManager,StateManager stateManager) {
        blockui = this;
        BlockEventForwarder forwarder = new BlockEventForwarder();
        addMouseListener(forwarder);
        addMouseMotionListener(forwarder);
        addKeyListener(forwarder);
        
        //visual stuff
        this.setBackground(Color.PINK);
        this.setSize(new Dimension(CELLWIDTH,CELLHEIGHT*10));
        setFocusable(true);

        //keeps the view in sync with many other components
        generalListener = new GeneralListener();
        
        nullHandler = NullHandler.INSTANCE;
        editHandler = new EditHandler(this);
        createHandler = new CreateHandler(this);
        deleteHandler = new DeleteHandler(this);
        currentEventHandler = editHandler;
        this.selectionManager = new SelectionManager();
        this.blockManager = blockManager;
        this.stateManager = stateManager;

        updateCurrentStackMachine();
        updateEventHandler();
    }
    
    
    //Controller-oriented methods
    
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }
    public StackMachine getCurrentStackMachine() {
        return currentStackMachine;
    }
    public BlockManager getBlockManager(){
        return blockManager;
    }
    
    /** Retrieves the Instruction at a given coordinate of the grid */
    protected Instruction getInstructionAt(int x, int y) {
        if (currentStackMachine != null)
            return currentStackMachine.getInstructions().get(y);
        else return null;
    }
    
    protected void updateEventHandler() {
        if (currentStackMachine == null)
            currentEventHandler = NullHandler.INSTANCE;
        else {
            String mode = blockManager.getMode();
            if(mode == BlockManager.EDIT)
                currentEventHandler = editHandler;
            else if(mode == BlockManager.CREATE)
                currentEventHandler = createHandler;
            else if(mode == BlockManager.DELETE)
                currentEventHandler = deleteHandler;
            else
                throw new IllegalArgumentException();
        }
        currentEventHandler.makeActive();
    }
    
    
    /** Keeps the view synchronised with the Application State Model and Domain Model */
    //basically, whatever relevant happens, repaint.
//TODO: listen to stackMachine change
    protected class GeneralListener implements StackMachineListener, SelectionManagerListener, BlockManagerListener {

        //BlockManagerListener
        //note that "instruction" here means "the selected type of instruction to be created"
        public void instructionChanged(String e) {
            repaint();
        }
        public void instructionCleared() {
            repaint();
        }
        public void modeChanged(String s) {
            repaint();
        }

        //SelectionManagerListener
        public void objectsSelected(Collection<? extends Instruction> objects) {
            repaint();
        }
        public void objectsUnselected(Collection<? extends Instruction> objects) {
            repaint();
        }
        public void selectionCleared() {
            repaint();
        }

        //StackMachineListener
        public void programCounterChanged(int line) {
            repaint();
        }
        public void storeChanged(int address) {
        }
        public void inputConsumed(int startIndex) {
        }
        public void outputChanged() {
        }
        public void programChanged(List<Instruction> instructions) {
            repaint();
        }
        public void stackChanged(EvaluationStack stack) {
        }
        
    }
    
    /** Display error messages */
    public void displayError(String text){
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
    
    
    //View-oriented methods
    
    protected void updateCurrentStackMachine() {
System.out.println("updating blockUI");
        StackMachine newMachine = stateManager.stackMachine;
        if(newMachine != currentStackMachine){
            if(currentStackMachine!=null)
                currentStackMachine.removeListener(generalListener);
            currentStackMachine = newMachine;
            currentStackMachine.addListener(generalListener);
        }
        
        if (currentStackMachine != null) {
            int stackMachineSize = currentStackMachine.getInstructions().size() + 1; //+1 to add extra space for 1 command
            setPreferredSize(new Dimension(CELLWIDTH, stackMachineSize*CELLHEIGHT));
        }
        else
            setPreferredSize(new Dimension(CELLWIDTH,CELLHEIGHT));
        updateEventHandler();
        repaint();
    }
    
    public void paintComponent(Graphics g) {
System.out.println("painting BlockUI");
        Graphics2D g2d = (Graphics2D)g; 

        Color oldColor = g2d.getColor();
        Shape oldClip = g2d.getClip();

        //background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        //the stuff
        if (currentStackMachine != null) {
            int codeLength = currentStackMachine.getInstructions().size();
            int height = codeLength + 1;
            Dimension viewSize = new Dimension(CELLWIDTH,CELLHEIGHT*height);
            
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, viewSize.width, viewSize.height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(0, 0, viewSize.width, viewSize.height);
        
            // paint mask
            g2d.clipRect(1, 1, viewSize.width - 2, viewSize.height - 2);
        
            //paint instructions
            int j = 0;
            for(Instruction i : currentStackMachine.getInstructions()){
                InstructionPainter.INSTANCE.paint(g2d, i, 0, j);
                j += 1;
            }
            
        
        }else{
            g2d.setColor(Color.BLACK);
            g.drawString("No-machine error.", 50, 50);
        }
        
        // paints mode-related graphics, such as selection boxes and game-over screens
        currentEventHandler.paint(g2d);

        // restore the original state of the Graphics object
        g2d.setColor(oldColor);
        g2d.setClip(oldClip);
    }
    

}
