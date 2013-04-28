package ox.stackgame.blockUI;

import java.awt.Graphics2D;

import ox.stackgame.stackmachine.instructions.Instruction;

/** General interface for painting a block of an instruction. Belongs to the View. */

interface BlockElementPainter {
	void paint(Graphics2D g, Instruction e, int x, int y);
}
