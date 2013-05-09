package ox.stackgame.blockUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.instructions.Instruction;


/**
 * A MouseHandler that creates cave elements based on stretchbox regions.
 * Belongs to both, the View and the Controller
 */
class EditHandler extends AbstractStretchBoxHandler{
    /** Holds the dragging origin. */
    protected Point dragOrigin;
    /** Holds the dragging target. */
    protected Point dragTarget;

    public EditHandler(BlockUI blockUI) {
        super(blockUI);
    }
    
    //Controller methods
    
    public void mousePressed(MouseEvent e) {
        // The following line tests whether the dragging or stretching should be started
        if (e.getButton() == MouseEvent.BUTTON1 && stretchBoxOrigin == null && dragOrigin == null) {
            Point cell = BlockUI.getCellAtPoint(new Point(e.getX(),e.getY()));
            Instruction clickedElement = blockUI.getInstructionAt(cell.x, cell.y);
            // If no selected element is located at the given coordinates... 
            if (clickedElement == null || !blockUI.getSelectionManager().isSelected(clickedElement)) {
                // ...then call the superclass to start box stretching.
                blockUI.getSelectionManager().clear();
                super.mousePressed(e);
            }
            else {
                // ...otherwise, start dragging.
                if (!blockUI.getSelectionManager().isSelectionEmpty()) {
                    dragOrigin = boxAtPoint(e.getPoint());
                    dragTarget = boxAtPoint(e.getPoint());
                    blockUI.repaint();
                }
            }
        }
    }
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && dragOrigin != null) {
            dragTarget = boxAtPoint(e.getPoint());
            blockUI.repaint();
            dragFinished(dragOrigin,dragTarget);
            dragOrigin = null;
            dragTarget = null;
            blockUI.repaint();
        }
        super.mouseReleased(e);
    }
    public void mouseDragged(MouseEvent e) {
        if (dragOrigin != null) {
            dragTarget = boxAtPoint(e.getPoint());
            blockUI.repaint();
        }
        super.mouseDragged(e);
    }
    
    
    protected void boxStretchingFinished(Point boxOrigin,Point boxTarget) {
        // Update the selection.
        int x1 = Math.min(boxOrigin.x, boxTarget.x);
        int y1 = Math.min(boxOrigin.y, boxTarget.y);
        int x2 = Math.max(boxOrigin.x, boxTarget.x);
        int y2 = Math.max(boxOrigin.y, boxTarget.y);

        for(int y = y1; y <= y2; y++)
            for(int x = x1; x <= x2; x++){
                Instruction i = blockUI.getInstructionAt(x, y);
                if(i!=null)blockUI.getSelectionManager().toggleObjectSelection(i);
            }

    }
    
    protected void dragFinished(Point dragOrigin2, Point dragTarget2) {
        Iterator<Instruction> i = blockUI.getSelectionManager().getSelection();

        int moveY = dragTarget2.y - dragOrigin2.y;

        //find their location
        StackMachine stackMachine = blockUI.getCurrentStackMachine();
        List<Instruction> instructions = stackMachine.getInstructions();
        //instead of the nice: HashMap
        //due to overriding of Instruction.hashCode
        //I have to do it using a TreeMap:
        Map<Integer,Instruction> moved = new TreeMap<Integer,Instruction>();
        i = blockUI.getSelectionManager().getSelection();
        while (i.hasNext()) {
            Instruction e = i.next();
            //instead of the nice: moved.put(instructions.indexOf(e),e);
            //due to overriding of Instruction.equals
            //I have to do it the hard way:
            int index = 0;
            for(Instruction in : instructions)
                if(in==e){
                    moved.put(index,e);
                    break;
                }else index+=1;
        }

        //remove the elements one by one
        List<Integer> movedList = new ArrayList<Integer>(moved.keySet());
        Collections.sort(movedList, Collections.reverseOrder());
        for(Integer j : movedList)
            stackMachine.removeInstruction(j);
        
//TODO: if you move a block of instructions below the 1st block, the order might reverse...
        Collections.sort(movedList);
    
        
        //add them at the correct location
        for(Integer j : movedList)
            stackMachine.addInstruction(j+moveY, moved.get(j));
        
        //Evoke synchronisation with TextUI
        blockUI.getBlockManager().sync();
    }
    
    
    //View methods
    
    public void paint(Graphics2D g) {
        // If dragging is active, then paint the outlines of the dragged elements and the original selection
        int moveY = 0;
        if (dragOrigin != null){
            moveY = dragTarget.y - dragOrigin.y;
        }
        List<Instruction> list = blockUI.getCurrentStackMachine().getInstructions();
        
        int i = 0;
        for(Instruction e : list){
            if(blockUI.getSelectionManager().isSelected(e)){
                //original
                paintSelectionBox(g, BlockUI.getLocationOfCell(0,i));
                //moved
                if (dragOrigin != null)
                    paintSelectionBox(g, BlockUI.getLocationOfCell(0, i + moveY));
            }
            i +=1;
        }
        
        // Call the superclass to paint the stretching box
        super.paint(g);
    }
    
    public void keyPressed(KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.VK_DELETE){
            
            Iterator<Instruction> i = blockUI.getSelectionManager().getSelection();
            StackMachine machine = blockUI.getCurrentStackMachine();
            List<Instruction> instructions = machine.getInstructions();
            i = blockUI.getSelectionManager().getSelection();
            while (i.hasNext()) {
                Instruction e = i.next();
                //instead of the nice: moved.put(instructions.indexOf(e),e);
                //due to overriding of Instruction.equals
                //I have to do it the hard way:
                int index = 0;
                for(Instruction in : instructions)
                    if(in==e)break;
                    else index+=1;

                Instruction inst = machine.getInstruction(index);
                
                //increase the amount of this instruction available
                Map<String, Integer> available = blockUI.getBlockManager().availableInstructions;
                String s = inst.toString();
                String toEdit = (available.containsKey(s) ? s : s.split(" ")[0] + " *");
                
                if(!available.containsKey(toEdit))throw new IllegalArgumentException("Invalid Instruction");
    
                int count = available.get(toEdit);
                if(count>-1)
                    available.put(toEdit, count+1);            
                
                //inform about the deletion
                blockUI.getBlockManager().useInstruction();
    
                
                machine.removeInstruction(index);
            }
            
            //clear the selection
            blockUI.getSelectionManager().clear();
            
            //Evoke synchronisation with TextUI
            blockUI.getBlockManager().sync();

        }
    }

    

}