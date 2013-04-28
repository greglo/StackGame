package ox.stackgame.blockUI;

import java.awt.Point;

import ox.stackgame.stackmachine.StackMachine;

/** Handles events responsible for creation of WallElements.
 * 	Methods here are a part of the Controller. 
 **/
public class DeleteHandler extends AbstractStretchBoxHandler{
	public DeleteHandler(BlockUI blockUI) {
		super(blockUI);
	}

	protected void boxStretchingFinished(Point boxOrigin, Point boxTarget) {
		int height = Math.abs(boxOrigin.y - boxTarget.y);
		
		StackMachine machine = blockUI.getCurrentStackMachine();
		
		for(int curY = height-1; curY >= 0; curY++)
			machine.removeInstruction(curY);
	}
	
}
