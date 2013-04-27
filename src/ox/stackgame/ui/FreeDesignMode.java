/**
 * 
 */
package ox.stackgame.ui;

/**
 * Allows the user to create a program and populate the input tape.
 * @author danfox
 *
 */
public class FreeDesignMode extends DesignMode {
	
	public void accept (ModeVisitor v){
		v.visit(this);
	}

}
