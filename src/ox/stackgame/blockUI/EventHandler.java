package ox.stackgame.blockUI;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Handles input (i.e. mouse and keyboard) events of the view. Implements the
 * State pattern. Concrete states subclass this class.
 * Mostly addresses issues of the Controller and View parts of the application.
 */
interface EventHandler extends KeyListener, MouseListener, MouseMotionListener {
	/** Activate the handler */
	void makeActive();
	/** Paint the necessary graphics */
	void paint(Graphics2D g);
}