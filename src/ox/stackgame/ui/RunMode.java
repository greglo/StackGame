/**
 * 
 */
package ox.stackgame.ui;

/**
 * 
 * A new RunMode should be created when we begin running. It stores whether execution has been paused or not.
 * 
 * @author danfox
 * @author rgossiaux
 */

// TODO: Think about what this class should do.

public class RunMode extends Mode {
    
    protected boolean isRunning = false;
    
    public void pause() {
        isRunning = false;
    }
    
    public void run() {
        isRunning = true;
    }

    public void accept(ModeVisitor v) {
        v.visit(this);
    }

}
