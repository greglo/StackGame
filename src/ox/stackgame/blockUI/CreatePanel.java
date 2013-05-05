package ox.stackgame.blockUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import ox.stackgame.blockUI.BlockManager.BlockManagerListener;
import ox.stackgame.blockUI.BlockManager.NameOrInstruction;
import ox.stackgame.stackmachine.instructions.Instruction;
import ox.stackgame.stackmachine.instructions.Operations;
import ox.stackgame.ui.ChallengeMode;
import ox.stackgame.ui.FreeDesignMode;
import ox.stackgame.ui.Mode;
import ox.stackgame.ui.ModeVisitor;
import ox.stackgame.ui.RunMode;
import ox.stackgame.ui.StateManager;

/** Contains the selection of available instructions to be used in Create mode 
 * At any one point, the state consists of a BlockManager mode and a StateManager mode
 * The selection panel is only active (instructions are clickable) when block manager is on "create mode"
 * Clicking at any other time will enable the create mode.
 * It is not active in RunMode - not even clickable.
 * 
 * */

@SuppressWarnings("serial")
public class CreatePanel extends JPanel implements BlockManagerListener, MouseListener {

    //whether or not instructions are clickable
//    private boolean clickable;
    //whether or not any buttons are displayed
//    private boolean active;
    private Map<String,Entry> availableInstructions = null;
    private BlockManager manager;
    private StateManager stateManager;
    //order of the selected item
    private int selected = -1;
    
    //for now, they are read from BlockUI
    //on change, create new painter for possible instructions
    final private int CELLWIDTH = BlockUI.CELLWIDTH;
    final private int CELLHEIGHT = BlockUI.CELLHEIGHT;
    
    public CreatePanel(BlockManager manager, StateManager stateManager){
        this.manager = manager;
        this.stateManager = stateManager;
        manager.addListener(this);
//        active = manager.getMode() == BlockManager.CREATE;
        
        // visual stuff
//TODO: adjust to one's needs
        this.setBackground(Color.PINK);
        this.setSize(new Dimension(CELLWIDTH, CELLHEIGHT * 10));
        setFocusable(true);
        
        // pay attention to mode changes
        stateManager.registerModeActivationVisitor(modeActivationVisitor);
        stateManager.registerModeDeactivationVisitor(modeDeactivationVisitor);
        
        //current mode
        //Warning: dirty
        Mode activeMode = stateManager.getActiveMode();
        if(activeMode != null)
            if(activeMode.getClass() == RunMode.class)
                modeActivationVisitor.visit((RunMode)activeMode);
            else if(activeMode.getClass() == ChallengeMode.class)
                modeActivationVisitor.visit((ChallengeMode)activeMode);
            else if(activeMode.getClass() == FreeDesignMode.class)
                modeActivationVisitor.visit((FreeDesignMode)activeMode);
            else throw new IllegalArgumentException("Wrong mode");
    }
    
    
    //listen to BlockManager
    public void instructionChanged(NameOrInstruction e) {}
    public void instructionCleared() {}
    public void modeChanged(String s) {
//        if (s == BlockManager.CREATE)activate();
  //      else deactivate();
        repaint();
    }
    
    protected void activate(){
        
//        active = true;
        manager.setMode(BlockManager.CREATE);
    }
//    protected void deactivate(){
//        active = false;
//    }
    
    //check if buttons are clickable
    protected boolean isActive(){
//TODO: can this be null?
        Mode activeMode = stateManager.getActiveMode();
        return manager.getMode()==BlockManager.CREATE && (activeMode.getClass()==ChallengeMode.class || activeMode.getClass()==FreeDesignMode.class);
    }
    
//TODO: listen for stackMachine changes?
    
    public void paintComponent(Graphics g) {
//TODO: add current-instruction highlighting, gray mask on inactivity
        System.out.println("painting BlockUI");
        Graphics2D g2d = (Graphics2D) g;

        Color oldColor = g2d.getColor();
        Shape oldClip = g2d.getClip();

        // background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // the stuff
        if (availableInstructions != null){
            int height = availableInstructions.size();

            Dimension viewSize = new Dimension(CELLWIDTH, CELLHEIGHT * height);

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, viewSize.width, viewSize.height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(0, 0, viewSize.width, viewSize.height);

            // paint mask
            g2d.clipRect(1, 1, viewSize.width - 2, viewSize.height - 2);

            // paint instructions
            int j = 0;
            for (String s : availableInstructions.keySet()) {
                InstructionSelectionPainter.INSTANCE.paint(g2d, s, availableInstructions.get(s), 0, j);
                j += 1;
            }
            
            //highlight selected instruction
            if(selected != -1)
                paintSelectionBox(g2d, new Point(0,selected*CELLHEIGHT));
            
        } else {
            g2d.setColor(Color.BLACK);
            g.drawString("No-machine error.", 50, 50);
        }

        // restore the original state of the Graphics object
        g2d.setColor(oldColor);
        g2d.setClip(oldClip);
        
        
    }
    
    
    /** paints a single box on the grid of the selected region */
    protected void paintSelectionBox(Graphics2D g, Point p){
        Color oldColor = g.getColor();

        Color color = new Color(0.75f, 0.75f, 0.75f, 0.25f); //Transparent gray 
        g.setColor(color);
        g.fillRect(p.x, p.y, CELLWIDTH, CELLHEIGHT);
        
        g.setColor(Color.GRAY);
        Stroke oldStroke = g.getStroke();

        g.setStroke(new BasicStroke(CELLHEIGHT/10));
        int margin = CELLHEIGHT/10; //amount of pixels left as a margin inside the box
        g.drawLine(p.x+margin, p.y+margin, p.x+margin, p.y+CELLHEIGHT-margin);
        g.drawLine(p.x+margin, p.y+CELLHEIGHT-margin, p.x+CELLWIDTH-margin, p.y+CELLHEIGHT-margin);
        g.drawLine(p.x+CELLWIDTH-margin, p.y+CELLHEIGHT-margin, p.x+CELLWIDTH-margin, p.y+margin);
        g.drawLine(p.x+CELLWIDTH-margin, p.y+margin, p.x+margin, p.y+margin);
        
        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    
    // code to be executed when a mode is activated.
    private ModeVisitor modeActivationVisitor = new ModeVisitor() {
        public void visit(ChallengeMode m) {
/*            //Translate to match the format used here 
            //note the use of a TreeMap to keep the list sorted
            availableInstructions = new TreeMap<String,Entry>();
            Map<Instruction, Integer> set = m.getChallenge().instructionSet;
            for(Instruction i : set.keySet())
                availableInstructions.put(i.toString(),new Entry(i,set.get(i)));
            repaint();
            selected = -1;
*/
        }

        public void visit(RunMode m) {
//TODO: do something here?
            repaint();
        }

        public void visit(FreeDesignMode m) {
            //create a map of all possible instructions
            Iterator<String> it = Operations.names();
            availableInstructions = new TreeMap<String,Entry>();
            while(it.hasNext()){
                String name = it.next();
                Instruction inst = (Operations.get(name).argTypes() == null ? new Instruction(name) : null);
                availableInstructions.put(name, new Entry(inst,-1));
            }
            repaint();
            selected = -1;
        }
    };

    // code to be executed when a mode is deactivated
//TODO: is it necessary at all?
    private ModeVisitor modeDeactivationVisitor = new ModeVisitor() {
        public void visit(RunMode m) {
//            deactivate();
        }

        public void visit(ChallengeMode m) {
//            deactivate();
        }

        public void visit(FreeDesignMode m) {
//            deactivate();
        }
    };


    
    
    //very simple, self-explanatory structure
    //note that if instruction is null, the user will be prompted for arguments for the instruction
    public static class Entry{
        public int count;
        public Instruction instruction;
        public Entry(Instruction instruction, int count){
           this.instruction = instruction;
           this.count = count;
        }
    }



    //Mouse actions
    public void mouseClicked(MouseEvent arg0) {
    }
    public void mouseEntered(MouseEvent arg0) {
    }
    public void mouseExited(MouseEvent arg0) {
    }
    public void mousePressed(MouseEvent e) {
        if(isActive()){
            //if in bounds, select the instruction
            Point p = e.getPoint();
            int x = p.x / CELLWIDTH;
            int y = p.y / CELLHEIGHT;
            if(x==0 && y < availableInstructions.size()){
                //Get the y'th key... ugly
                List<String> keyList = new ArrayList<String>(availableInstructions.keySet());
                java.util.Collections.sort(keyList);
                String key = keyList.get(y);
                Entry value = availableInstructions.get(key);
                if(value.instruction == null){
                    manager.setInstruction(key);
                }else{
                    manager.setInstruction(value.instruction);
                }
                //highlight the selected item
                selected = y;           

            }else selected = -1;
                
            
        }else{
            //enable CREATE mode
            manager.setMode(BlockManager.CREATE);
        }
    }
    public void mouseReleased(MouseEvent arg0) {
    }
    

}
