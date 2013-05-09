package ox.stackgame.blockUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import ox.stackgame.blockUI.BlockManager.BlockManagerListener;
import ox.stackgame.stackmachine.instructions.Operations;
import ox.stackgame.ui.ChallengeMode;
import ox.stackgame.ui.FreeDesignMode;
import ox.stackgame.ui.Mode;
import ox.stackgame.ui.ModeVisitor;
import ox.stackgame.ui.RunMode;
import ox.stackgame.ui.StateManager;

/**
 * Contains the selection of available instructions to be used in Create mode At
 * any one point, the state consists of a BlockManager mode and a StateManager
 * mode The selection panel is only active (instructions are clickable) when
 * block manager is on "create mode" Clicking at any other time will enable the
 * create mode. It is not active in RunMode - not even clickable.
 * 
 * */

@SuppressWarnings("serial")
public class CreatePanel extends JPanel implements BlockManagerListener, Scrollable {

    private BlockManager manager;
    private StateManager stateManager;
    // order of the selected item
    private int selected = -1;

    // for now, they are read from BlockUI
    // on change, create new painter for possible instructions
    final private int CELLWIDTH = BlockUI.CELLWIDTH;
    final private int CELLHEIGHT = BlockUI.CELLHEIGHT;

    public CreatePanel(BlockManager manager, StateManager stateManager) {
        this.manager = manager;
        this.stateManager = stateManager;
        manager.addListener(this);

        // pay attention to mode changes
        stateManager.registerModeActivationVisitor(modeActivationVisitor);

        // mouse listener
        addMouseListener(new PanelMouseListener());
        
        //current mode
        modeUpdate();

        // visual stuff
        updateSize();
        setFocusable(true);
        revalidate();
    }

    // listen to BlockManager
    public void instructionChanged(String e) {
    }

    public void instructionCleared() {
    }

    public void modeChanged(String s) {
        repaint();
    }
    public void instructionUsed(String e) {
        repaint();
    }
    
    //check if buttons are clickable
    protected boolean isActive(){
        //note that the mode is never null
        Mode activeMode = stateManager.getActiveMode();
        return manager.getMode() == BlockManager.CREATE
                && (activeMode.getClass() == ChallengeMode.class || activeMode.getClass() == FreeDesignMode.class);
    }
    
    //update current mode
    protected void modeUpdate(){
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
    
    public void paintComponent(Graphics g) {
        updateSize();

        Graphics2D g2d = (Graphics2D) g;

        Color oldColor = g2d.getColor();
        Shape oldClip = g2d.getClip();

        // background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // the stuff
        if (manager.availableInstructions != null){
            int height = manager.availableInstructions.size();

            Dimension viewSize = new Dimension(CELLWIDTH, CELLHEIGHT * height);
    
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, viewSize.width, viewSize.height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(0, 0, viewSize.width, viewSize.height);
    
            // paint mask
            g2d.clipRect(1, 1, viewSize.width - 2, viewSize.height - 2);
    
            // paint instructions
            int j = 0;
            for (String s : manager.availableInstructions.keySet()) {
                InstructionSelectionPainter.INSTANCE.paint(g2d, s, manager.availableInstructions.get(s), 0, j);
                j += 1;
            }
            
            //highlight selected instruction
            if(selected != -1)
                paintSelectionBox(g2d, new Point(0,selected*CELLHEIGHT));
            
            //gray mask on inactivity
            if(stateManager.getActiveMode().getClass() == RunMode.class || manager.getMode() != BlockManager.CREATE){
                Color color = new Color(0.75f, 0.75f, 0.75f, 0.25f); //Transparent gray 
                g2d.setColor(color);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }

    
            // restore the original state of the Graphics object
            g2d.setColor(oldColor);
            g2d.setClip(oldClip);
        }
    }
    
    public Dimension updateSize(){
        Dimension size = new Dimension(CELLWIDTH, CELLHEIGHT*(manager.availableInstructions == null ? 0 : manager.availableInstructions.size()));
        this.setPreferredSize(size);
        return size;
    }

    
    /** paints a single box on the grid of the selected region */
    protected void paintSelectionBox(Graphics2D g, Point p) {
        Color oldColor = g.getColor();

        Color color = new Color(0.75f, 0.75f, 0.75f, 0.25f); // Transparent gray
        g.setColor(color);
        g.fillRect(p.x, p.y, CELLWIDTH, CELLHEIGHT);

        g.setColor(Color.GRAY);
        Stroke oldStroke = g.getStroke();

        g.setStroke(new BasicStroke(CELLHEIGHT / 10));
        int margin = CELLHEIGHT / 10; // amount of pixels left as a margin
                                      // inside the box
        g.drawLine(p.x + margin, p.y + margin, p.x + margin, p.y + CELLHEIGHT - margin);
        g.drawLine(p.x + margin, p.y + CELLHEIGHT - margin, p.x + CELLWIDTH - margin, p.y + CELLHEIGHT - margin);
        g.drawLine(p.x + CELLWIDTH - margin, p.y + CELLHEIGHT - margin, p.x + CELLWIDTH - margin, p.y + margin);
        g.drawLine(p.x + CELLWIDTH - margin, p.y + margin, p.x + margin, p.y + margin);

        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    // code to be executed when a mode is activated.
    private ModeVisitor modeActivationVisitor = new ModeVisitor() {
        public void visit(ChallengeMode m) {
            if (m.getChallenge().instructionSet != null)
                manager.availableInstructions = new HashMap<String, Integer>(m.getChallenge().instructionSet);
            else
                allowAllInstructions();
            selected = -1;
            updateSize();
            revalidate();repaint();
        }

        public void visit(RunMode m) {
            repaint();
        }

        public void visit(FreeDesignMode m) {
            allowAllInstructions();
            selected = -1;
            updateSize();
            revalidate();repaint();
        }
    };

    private void allowAllInstructions() {
        //create a map of all possible instructions
        //notice the use of a TreeMap to keep the stuff sorted
        Iterator<String> it = Operations.names();
        manager.availableInstructions = new TreeMap<String, Integer>();
        while (it.hasNext()) {
            String name = it.next();
            if (Operations.get(name).argTypes() != null)
                name += " *";
            manager.availableInstructions.put(name, -1);
        }
        selected = -1;
    }

    class PanelMouseListener implements MouseListener {
        // Mouse actions
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
                if(x==0 && y < manager.availableInstructions.size()){
                    //Get the y'th key... ugly
                    List<String> keyList = new ArrayList<String>(manager.availableInstructions.keySet());
                    java.util.Collections.sort(keyList);
                    manager.setInstruction(keyList.get(y));

                    // highlight the selected item
                    selected = y;

                } else
                    selected = -1;

            }else{
                //enable CREATE mode and select 1st item
                manager.setMode(BlockManager.CREATE);
                List<String> keyList = new ArrayList<String>(manager.availableInstructions.keySet());
                java.util.Collections.sort(keyList);
                manager.setInstruction(keyList.get(0));
                selected = 0;
            }

            repaint();
        }

        public void mouseReleased(MouseEvent arg0) {
        }

    }



    //Scrollable
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
