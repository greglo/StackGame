package ox.stackgame.blockUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.instructions.Instruction;


/**
 * A MouseHandler that creates cave elements based on stretchbox regions.
 * Belongs to both, the View and the Controller
 */
class EditHandler extends AbstractStretchBoxHandler {
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
			for(int x = x1; x <= x2; x++)
				blockUI.getSelectionManager().toggleObjectSelection(blockUI.getInstructionAt(x, y));

	}
	
//TODO: make this method more elegant, more efficient
	protected void dragFinished(Point dragOrigin2, Point dragTarget2) {
		//check if such movement is possible, i.e. if target isn't out of bounds
		Iterator<Instruction> i = blockUI.getSelectionManager().getSelection();

//		int moveX = dragTarget2.x - dragOrigin2.x;
		int moveY = dragTarget2.y - dragOrigin2.y;

		
		
/*		boolean possible = true;
		while (possible && i.hasNext()) {
			Instruction e = i.next();
			int x = e.getX() + moveX;
			int y = e.getY() + moveY;
			Dimension caveSize = blockUI.getCurrentCave().getCaveSize();
			possible = (0<=x && 0<=y && x<caveSize.width && y<caveSize.height);
		}
		
		#yolo #yolo #yolo #yolo #yolo #yolo #yolo #yolo #yolo #yolo #yolo #yolo #yolo #yolo 
		
		*/
		
//		if (possible){
			//Moving the elements one by one might break Cave invariant 3 if the region being moved overlaps with itself
			//Therefore, we first remove them, then re-add them on the correct location
	
		//find their location
		StackMachine stackMachine = blockUI.getCurrentStackMachine();
		List<Instruction> instructions = stackMachine.getInstructions();
		Map<Integer,Instruction> moved = new HashMap<Integer,Instruction>();
		i = blockUI.getSelectionManager().getSelection();
		while (i.hasNext()) {
			Instruction e = i.next();
			moved.put(instructions.indexOf(e),e);
		}

		//remove the elements one by one
		List<Integer> movedList = new ArrayList<Integer>(moved.keySet());
		Collections.sort(movedList, Collections.reverseOrder());
		for(Integer j : movedList)
			stackMachine.removeInstruction(j);
		
//TODO: calculate the shift so that we don't place something out o bounds
		//moveY = Math.min(movedList., list.get())
		Collections.sort(movedList);
		//moveY = Math.max ...
		
		
		//add them at the correct location
		for(Integer j : movedList)
			stackMachine.addInstruction(j+moveY, moved.get(j));
		
		
		
//		}
		
	}
	
	
	//View methods
	
	public void paint(Graphics2D g) {
		// If dragging is active, then paint the outlines of the dragged elements and the original selection
//		int moveX = 0;
		int moveY = 0;
		if (dragOrigin != null){
//			moveX = dragTarget.x - dragOrigin.x;
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
	

}