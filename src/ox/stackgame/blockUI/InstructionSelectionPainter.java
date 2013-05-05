package ox.stackgame.blockUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/** Paints a single instruction block. Belongs to the View. */

public class InstructionSelectionPainter{
	public static final InstructionSelectionPainter INSTANCE = new InstructionSelectionPainter();

	public void paint(Graphics2D g, String name, CreatePanel.Entry entry, int x, int y) {
		Point realLocation = BlockUI.getLocationOfCell(x,y);

		Color c;
		c = Color.GREEN;
		
		g.setColor(c);
		g.fillRect(realLocation.x+1, realLocation.y+1, BlockUI.CELLWIDTH-2, BlockUI.CELLHEIGHT-2);

		g.setColor(Color.BLACK);
		g.drawString(name, realLocation.x+10, realLocation.y+20);
	}

	
}
