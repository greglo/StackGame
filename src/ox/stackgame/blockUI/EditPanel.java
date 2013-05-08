package ox.stackgame.blockUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JPanel;

import ox.stackgame.blockUI.BlockManager.BlockManagerListener;

/** Holds a single custom button that enables the Edit mode */
@SuppressWarnings("serial")
public class EditPanel extends JPanel implements BlockManagerListener {

	private boolean active;
	private BlockManager manager;
	
	public EditPanel(BlockManager manager){
		this.manager = manager;
		manager.addListener(this);
		if(manager.getMode() == BlockManager.EDIT)
			active = true;
	}
	
	public void instructionChanged(String e) {}
	public void instructionCleared() {}
	public void modeChanged(String s) {
		if (s == BlockManager.EDIT)activate();
		else deactivate();
	}
	
	protected void activate(){
		active = true;
		manager.setMode(BlockManager.EDIT);
	}
	protected void deactivate(){
		active = false;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g; 

		Color oldColor = g2d.getColor();
		Shape oldClip = g2d.getClip();

		//background
		if(active)g2d.setColor(Color.GREEN);
		else g2d.setColor(Color.RED);
		g2d.fillRect(0, 0, 50, 50);
		
		// restore the original state of the Graphics object
		g2d.setColor(oldColor);
		g2d.setClip(oldClip);
	}

    @Override
    public void instructionUsed(String s) {
        // TODO Auto-generated method stub
        
    }
	
//TODO: mouse clicks
	

	
	

}
