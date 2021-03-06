package ox.stackgame.blockUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/** Paints a single instruction block. Belongs to the View. */

public class InstructionSelectionPainter{
	public static final InstructionSelectionPainter INSTANCE = new InstructionSelectionPainter();

	public void paint(Graphics2D g, String name, int count, int x, int y) {
		Point realLocation = BlockUI.getLocationOfCell(x,y);

		Color c;
		c = Color.GREEN;
		
		g.setColor(c);
        g.fillRect(realLocation.x+3, realLocation.y+1, BlockUI.CELLWIDTH-5, BlockUI.CELLHEIGHT-2);

		g.setColor(Color.BLACK);
        g.drawString(name, realLocation.x+10, realLocation.y+25);
        if(count != -1)g.drawString("x"+count, realLocation.x+80, realLocation.y+25);
        
        //gray mask if 0 available
        if(count == 0){
            Color color = new Color(0.75f, 0.75f, 0.75f, 0.25f); //Transparent gray 
            g.setColor(color);
            g.fillRect(realLocation.x+3, realLocation.y+1, BlockUI.CELLWIDTH-5, BlockUI.CELLHEIGHT-2);
        }

	}

	
}
