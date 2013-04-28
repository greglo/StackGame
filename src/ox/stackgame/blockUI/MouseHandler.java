package ox.stackgame.blockUI;

import java.awt.event.KeyEvent;

/**
 * Handles mouse events of the view. This class implements the
 * State pattern. Concrete states subclass this class.
 */
abstract class MouseHandler implements EventHandler {
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}