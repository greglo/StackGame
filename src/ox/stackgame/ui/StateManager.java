package ox.stackgame.ui;

import java.util.Collection;
import java.util.HashSet;

import ox.stackgame.stackmachine.StackMachine;

/**
 * The StateManager maintains all the 'state' information pertinent to the
 * StackGame. This includes the current Mode and the current active Machine.
 * 
 * @author danfox
 * 
 */
public class StateManager {
    private Mode activeMode = null;
    private Mode lastMode = null;
    public final StackMachine stackMachine;

    private Collection<ModeVisitor> modeDeactivationVisitors = new HashSet<ModeVisitor>();
    private Collection<ModeVisitor> modeActivationVisitors = new HashSet<ModeVisitor>();
    
    /* BlockUI switching begins here */
    //Whether BlockUI is active/visible as opposed to TextUI
    private Boolean active = false;
    public Boolean isBlockUIActive(){
        return active;
    }
    public void setBlockUIActive(Boolean active){
        this.active = active;
    }
    public void toggleBlockUIActive(){
        active = !active;
    }
    /* BlockUI switching ends here */

    public StateManager(StackMachine stackMachine) {
        this.stackMachine = stackMachine;
    }

    public void setActiveMode(Mode newMode) {
        if (newMode != null) {
            if (newMode != activeMode) {
                // allow each ControllerComponent to react to the imminent
                // deactivation of the current activeMode
                if (activeMode != null) {
                    for (ModeVisitor v : modeDeactivationVisitors)
                        activeMode.accept(v);
                }
                // activate new mode
                lastMode = activeMode;
                activeMode = newMode;
                // allow each ControllerComponent to react to the activation of
                // the newMode
                for (ModeVisitor v : modeActivationVisitors)
                    activeMode.accept(v);
            }
        } else {
            throw new RuntimeException("setActiveMode(null) not allowed");
        }
    }
    
    public Mode getLastMode(){
        return lastMode;
    }

    public Mode getActiveMode() {
        return activeMode;
    }

    public void registerModeActivationVisitor(ModeVisitor v) {
        modeActivationVisitors.add(v);
    }

    public void registerModeDeactivationVisitor(ModeVisitor v) {
        modeDeactivationVisitors.add(v);
    }

    public Boolean removeModeActivationVisitor(ModeVisitor v) {
        return modeActivationVisitors.remove(v);
    }

    public Boolean removeModeDeactivationVisitor(ModeVisitor v) {
        return modeDeactivationVisitors.remove(v);
    }
}
