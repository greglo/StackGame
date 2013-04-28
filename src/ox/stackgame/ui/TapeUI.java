package ox.stackgame.ui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.*;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.StackValue;

/**
 * Visualisation of the input and output tapes. Allows user input in design mode
 * only. Displays current read head during RunMode.
 * 
 * @author danfox
 * 
 */
public class TapeUI extends JPanel {

    private List<StackValue<?>> inputTape = null;
    private StackMachine activeMachine = null;
    private static int boxSize = 50;

    private ModeVisitor modeActivationVisitor = new ModeVisitor() {
     // TODO make input tape editable on DesignMode visitors

        public void visit(RunMode m) {
            activeMachine = m.machine;
            resetCursors();
        }

        @Override
        public void visit(ChallengeMode m) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void visit(FreeDesignMode m) {
            // TODO Auto-generated method stub
            
        }
    };

    private ModeVisitor modeDeactivationVisitor = new ModeVisitor() {
        // TODO make input tape uneditable on design modes

        public void visit(RunMode m) {
            // TODO hide cursor
        }

        @Override
        public void visit(ChallengeMode m) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void visit(FreeDesignMode m) {
            // TODO Auto-generated method stub
            
        }
    };

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void inputConsumed(int startIndex) {
            // TODO update input tape, move cursor
        }

        public void outputChanged() {
            // TODO update output tape
        }
    };

    public TapeUI(StateManager m) {

        // pay attention to mode changes
        m.registerModeActivationVisitor(modeActivationVisitor);
        m.registerModeDeactivationVisitor(modeDeactivationVisitor);

        // listen to the stack machine
        m.stackMachine.addListener(l);

        // sort out appearance
        this.setBackground(ApplicationFrame.caBlue2L);
        this.setSize(new Dimension(750 - 2 * ApplicationFrame.p, 50));
        
        resetCursors();

        // TODO create scrolling (two scrollbars?) monotype font etc, different
        // colours for input/output

    }

    protected void resetCursors() {
        // TODO Auto-generated method stub
        
    }
}
