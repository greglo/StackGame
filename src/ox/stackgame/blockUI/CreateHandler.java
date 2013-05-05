package ox.stackgame.blockUI;

import java.awt.Point;

import ox.stackgame.blockUI.BlockManager.NameOrInstruction;
import ox.stackgame.stackmachine.instructions.Instruction;

/** Handles events responsible for creation of WallElements.
 * 	Methods here are a part of the Controller. 
 **/
public class CreateHandler extends AbstractStretchBoxHandler{
	public CreateHandler(BlockUI blockUI) {
		super(blockUI);
	}

	protected Instruction newInstruction() {
//TODO: deal with arguments
	    NameOrInstruction noi = blockUI.getBlockManager().getInstruction();
		if(noi.instruction != null) return noi.instruction;
		else{ 
		  //TODO: deal with arguments
		    return new Instruction(noi.name);
		}
	}
	
	
//TODO: review this
	protected void boxStretchingFinished(Point boxOrigin, Point boxTarget) {
//		int x = Math.min(boxOrigin.x, boxTarget.x);
//		int y = Math.min(boxOrigin.y, boxTarget.y);
//		int width = Math.abs(boxOrigin.x - boxTarget.x);
		int height = Math.abs(boxOrigin.y - boxTarget.y);
		
		for(int curY = 0; curY <= height; curY++)
//			for(int curX = 0; curX <= width; curX++)
				blockUI.getCurrentStackMachine().addInstruction(curY, newInstruction());
	}
	
}
