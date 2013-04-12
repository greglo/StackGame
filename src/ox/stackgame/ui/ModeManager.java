package ox.stackgame.ui;

import java.util.Collection;
import java.util.HashSet;

public class ModeManager {
	private Mode activeMode = null;
	
	private Collection<ModeVisitor> modeDeactivationVisitors = new HashSet<ModeVisitor>();
	private Collection<ModeVisitor> modeActivationVisitors = new HashSet<ModeVisitor>();
	
	public void setActiveMode(Mode newMode){
		// TODO check new mode is allowed?
		if (newMode!=null){
			if (newMode != activeMode){
				// allow each ControllerComponent to react to the imminent deactivation of the current activeMode
				if(activeMode!=null){
					for (ModeVisitor v : modeDeactivationVisitors)	activeMode.accept(v);			
				}
				// activate new mode
				activeMode = newMode;
				// allow each ControllerComponent to react to the activation of the newMode
				for (ModeVisitor v : modeActivationVisitors)	activeMode.accept(v);
			}
		} else {
			throw new RuntimeException("setActiveMode(null) not allowed");
		}
	}
	
	public Mode getActiveMode() {
		return activeMode;
	}
	
	public void registerModeActivationVisitor(ModeVisitor v){
		modeActivationVisitors.add(v);
	}
	
	public void registerModeDeactivationVisitor(ModeVisitor v){
		modeDeactivationVisitors.add(v);
	}
	
	public Boolean removeModeActivationVisitor(ModeVisitor v){
		return modeActivationVisitors.remove(v);
	}

	public Boolean removeModeDeactivationVisitor(ModeVisitor v){
		return modeDeactivationVisitors.remove(v);
	}
}
