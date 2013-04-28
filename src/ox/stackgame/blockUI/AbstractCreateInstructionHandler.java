package ox.stackgame.blockUI;

import java.awt.Point;


/**
 * A MouseHandler that creates instruction blocks.
 * It holds the basis of the Controller part of stretch-box based handlers.
 * 
 * It was strongly influenced by the one created over the lecture course by Dr. Motik.
 */
abstract class AbstractCreateInstructionHandler extends AbstractStretchBoxHandler {
	public AbstractCreateInstructionHandler(BlockUI blockUI) {
		super(blockUI);
	}
//TODO
	protected void boxStretchingFinished(Point boxOrigin, Point boxTarget) {
/*		int x = Math.min(boxOrigin.x, boxTarget.x);
		int y = Math.min(boxOrigin.y, boxTarget.y);
		int width = Math.abs(boxOrigin.x - boxTarget.x);
		int height = Math.abs(boxOrigin.y - boxTarget.y);
		
		for(int curY = 0; curY <= height; curY++)
			for(int curX = 0; curX <= width; curX++){
				BlockElement e = newCaveElement(curX + x,curY + y);
				caveView.getCurrentCave().addCaveElement(e);
			}*/
	}
	
//	protected abstract BlockElement newCaveElement(int x, int y);
}