package ox.stackgame.blockUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

/**
 * A MouseHandler that manages selection creation, etc. of instruction blocks.
 * 
 * It was strongly influenced by the one created over the lecture course by Dr. Motik.
 */
abstract class AbstractStretchBoxHandler extends MouseHandler {

	protected final BlockUI blockUI;
	protected Point stretchBoxOrigin;
	protected Point stretchBoxTarget;
	
	public AbstractStretchBoxHandler(BlockUI blockUI) {
		this.blockUI = blockUI;
	}
	
	
	//Predominantly Controller-oriented methods

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && stretchBoxOrigin == null) {
			stretchBoxOrigin = boxAtPoint(e.getPoint());
			stretchBoxTarget = stretchBoxOrigin;
			blockUI.repaint();
		}
	}
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && stretchBoxOrigin != null) {
			stretchBoxTarget = boxAtPoint(e.getPoint());
			blockUI.repaint();
			boxStretchingFinished(stretchBoxOrigin,stretchBoxTarget);
			stretchBoxOrigin = null;
			stretchBoxTarget = null;
			blockUI.repaint();
		}
	}
	public void mouseDragged(MouseEvent e) {
		if (stretchBoxOrigin != null) {
			stretchBoxTarget = boxAtPoint(e.getPoint());
			blockUI.repaint();
		}
	}
	public void makeActive() {
		// Reset 
		stretchBoxOrigin = null;
		stretchBoxTarget = null;
	}
	public void mouseMoved(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	
	/** entry point of subclasses */
	protected abstract void boxStretchingFinished(Point stretchBoxOrigin2, Point stretchBoxTarget2);
	
	/** return the x and y coordinates of the box where a certain point is located */
	protected Point boxAtPoint(Point p){
		return new Point(p.x / BlockUI.CELLWIDTH, p.y / BlockUI.CELLHEIGHT);
	} 
	
	
	//Predominantly View-oriented methods
	
	/** highlight selected region */
	public void paint(Graphics2D g) {
		if (stretchBoxOrigin != null) {
			int x1 = Math.min(stretchBoxOrigin.x, stretchBoxTarget.x);
			int y1 = Math.min(stretchBoxOrigin.y, stretchBoxTarget.y);
			int x2 = Math.max(stretchBoxOrigin.x, stretchBoxTarget.x);
			int y2 = Math.max(stretchBoxOrigin.y, stretchBoxTarget.y);
			
			for(int y = y1; y<=y2; y++)
				for(int x = x1; x<=x2; x++)
					paintSelectionBox(g, new Point(x*BlockUI.CELLWIDTH,y*BlockUI.CELLHEIGHT));
		}
	}
	
	/** paints a single box on the grid of the selected region */
	protected void paintSelectionBox(Graphics2D g, Point p){
		Color oldColor = g.getColor();

		Color color = new Color(0.75f, 0.75f, 0.75f, 0.25f); //Transparent gray 
		g.setColor(color);
        g.fillRect(p.x, p.y, BlockUI.CELLWIDTH, BlockUI.CELLHEIGHT);
        
		g.setColor(Color.GRAY);
		Stroke oldStroke = g.getStroke();

		g.setStroke(new BasicStroke(BlockUI.CELLHEIGHT/10));
		int margin = BlockUI.CELLHEIGHT/10; //amount of pixels left as a margin inside the box
		g.drawLine(p.x+margin, p.y+margin, p.x+margin, p.y+BlockUI.CELLHEIGHT-margin);
		g.drawLine(p.x+margin, p.y+BlockUI.CELLHEIGHT-margin, p.x+BlockUI.CELLWIDTH-margin, p.y+BlockUI.CELLHEIGHT-margin);
		g.drawLine(p.x+BlockUI.CELLWIDTH-margin, p.y+BlockUI.CELLHEIGHT-margin, p.x+BlockUI.CELLWIDTH-margin, p.y+margin);
		g.drawLine(p.x+BlockUI.CELLWIDTH-margin, p.y+margin, p.x+margin, p.y+margin);
		
		g.setStroke(oldStroke);
		g.setColor(oldColor);
	}
	
}