package ox.stackgame.blockUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Map;

import javax.swing.JPanel;

import ox.stackgame.blockUI.BlockManager.BlockManagerListener;
import ox.stackgame.stackmachine.instructions.Instruction;
import ox.stackgame.ui.ChallengeMode;
import ox.stackgame.ui.FreeDesignMode;
import ox.stackgame.ui.ModeVisitor;
import ox.stackgame.ui.RunMode;

/** Contains the selection of available instructions to be used in Create mode */
@SuppressWarnings("serial")
public class CreatePanel extends JPanel implements BlockManagerListener, ModeVisitor {

    private boolean active;
    private Map<Instruction,Integer> availableInstructions;
    private BlockManager manager;
    
    public CreatePanel(BlockManager manager){
        this.manager = manager;
        manager.addListener(this);
        active = manager.getMode() == BlockManager.CREATE;
        
    }
    
    public void instructionChanged(String e) {}
    public void instructionCleared() {}
    public void modeChanged(String s) {
        if (s == BlockManager.EDIT)activate();
        else deactivate();
    }
    
    protected void activate(){
        active = true;
        manager.setMode(BlockManager.EDIT);
    }
    protected void deactivate(){
        active = false;
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g; 

        Color oldColor = g2d.getColor();
        Shape oldClip = g2d.getClip();

        //background
        if(active)g2d.setColor(Color.GREEN);
        else g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 50, 50);
        
        // restore the original state of the Graphics object
        g2d.setColor(oldColor);
        g2d.setClip(oldClip);
    }

    
    
    public void visit(ChallengeMode m) {
//TODO: fix
//        availableInstructions = m.getChallenge().instructionSet;
    }

    public void visit(RunMode m) {
//TODO: do something here?
    }

    public void visit(FreeDesignMode m) {
//TODO: get or create a map of all possible instructions
        //availableInstructions = 
    }
    
//TODO: mouse clicks
    

    
    

}
