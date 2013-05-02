/**
 * 
 */
package ox.stackgame.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;

/**
 * 
 * A new RunMode should be created when we begin running. It stores whether execution has been paused or not.
 * 
 * @author danfox
 * @author rgossiaux
 */

// TODO: Think about what this class should do.

public class RunMode extends Mode {
    
    public final StackMachine machine;
    protected boolean running = false;
    /**
     * Number of ms between steps
     */
    public final int stepDelay = 500;
    protected final Timer timer;
    private ActionListener finishedCallback = null;
    
    public RunMode(StackMachine m) {
        machine = m;
        ActionListener timeTrigger = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    machine.step();
                    // machine has halted
                    if (!machine.isRunning()) {
                        pause();
                        if (finishedCallback != null) {
                            finishedCallback.actionPerformed(null);
                            finishedCallback = null;
                        }
                    }
                } catch (StackRuntimeException e1) {
                    // TODO eui should display error
                    e1.printStackTrace();
                }
            }
        };
        timer = new Timer(stepDelay, timeTrigger);
    }
    
    public void pause() {
        running = false;
        timer.stop();
    }
    
    public void run() {
        running = true;
        timer.start();
    }
    
    public void run(ActionListener finishedCallback){
        running=true;
        this.finishedCallback  = finishedCallback;
        timer.start();
    }
    
    public boolean timerRunning() { return running; }

    public void accept(ModeVisitor v) {
        v.visit(this);
    }

}
