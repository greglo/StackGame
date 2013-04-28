package ox.stackgame.blockUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import ox.stackgame.stackmachine.instructions.Instruction;

/** Paints a single instruction block. Belongs to the View. */

public class InstructionPainter implements BlockElementPainter {
	public static final InstructionPainter INSTANCE = new InstructionPainter();

	public void paint(Graphics2D g, Instruction e, int x, int y) {
		Point realLocation = BlockUI.getLocationOfCell(x,y);

		Color c;
		c = Color.GREEN;
		
		g.setColor(c);
		g.fillRect(realLocation.x, realLocation.y, BlockUI.CELLWIDTH, BlockUI.CELLHEIGHT);
//TODO: display instruction type
	}

	
}
