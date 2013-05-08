package ox.stackgame.blockUI;

import java.awt.Point;
import java.util.Map;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.instructions.Instruction;

/** Handles events responsible for creation of WallElements.
 * 	Methods here are a part of the Controller. 
 **/
public class DeleteHandler extends AbstractStretchBoxHandler{
	public DeleteHandler(BlockUI blockUI) {
		super(blockUI);
	}

	protected void boxStretchingFinished(Point boxOrigin, Point boxTarget) {
		int min = Math.min(boxOrigin.y,boxTarget.y);
	    int height = Math.abs(boxOrigin.y - boxTarget.y);
		
		StackMachine machine = blockUI.getCurrentStackMachine();
		
		for(int curY = height; curY >= 0; curY--){
		    if(min+curY < machine.getInstructions().size()){
    		    Instruction i = machine.getInstruction(min+curY);
    		
                //decrease the amount of this instruction available
                Map<String, Integer> available = blockUI.getBlockManager().availableInstructions;
                //Option 1: instructions will have this exact one available
                String s = i.toString();
                String toEdit = (available.containsKey(s) ? s : s.split(" ")[0] + " *");
                
                if(!available.containsKey(toEdit))throw new IllegalArgumentException("Invalid Instruction");
    
                int count = available.get(toEdit);
                if(count>-1)
                    available.put(toEdit, count+1);            
                
                //inform about the deletion
                blockUI.getBlockManager().useInstruction();
    
                
                machine.removeInstruction(min+curY);
		    }
		}
		
	    //Evoke synchronisation with TextUI
        blockUI.getBlockManager().sync();

	}
	
}
