package ox.stackgame.blockUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import ox.stackgame.stackmachine.instructions.Instruction;

/** Paints a single instruction block. Belongs to the View. */

public class InstructionPainter{
	public static final InstructionPainter INSTANCE = new InstructionPainter();

	public void paint(Graphics2D g, Instruction e, int x, int y) {
		Point realLocation = BlockUI.getLocationOfCell(x,y);

		Color c;
		c = Color.GREEN;
		
		g.setColor(c);
		g.fillRect(realLocation.x+3, realLocation.y+1, BlockUI.CELLWIDTH-5, BlockUI.CELLHEIGHT-2);

		g.setColor(Color.BLACK);
		g.drawString(e.toString(), realLocation.x+10, realLocation.y+25);
	}

	
}
