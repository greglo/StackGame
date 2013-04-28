/**
 * 
 */
package ox.stackgame.ui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * @author danfox
 * Visualisation of the evaluation stack of the machine. Scolls.
 */
public class StackUI extends JPanel {
    
    private ModeVisitor modeActivationVisitor = new ModeVisitor() {
        public void visit(RunMode m) {
            // TODO
        }

        public void visit(ChallengeMode m) {
            // TODO Auto-generated method stub            
        }
        public void visit(FreeDesignMode m) {
            // TODO Auto-generated method stub            
        }
    }; 
    private ModeVisitor modeDeactivationVisitor = new ModeVisitor() {
        public void visit(RunMode m) {
            // TODO
        }

        public void visit(ChallengeMode m) {
            // TODO Auto-generated method stub            
        }
        public void visit(FreeDesignMode m) {
            // TODO Auto-generated method stub            
        }
    }; 

    private StackMachineListener l = new StackMachineListener() {

        public void programCounterChanged(int line) {
            // TODO Auto-generated method stub
            
        }

        public void storeChanged(int address) {
            // TODO Auto-generated method stub
            
        }

        public void inputConsumed(int startIndex) {
            // TODO Auto-generated method stub
            
        }

        public void outputChanged() {
            // TODO Auto-generated method stub
            
        }

        public void programChanged(List<Instruction> instructions) {
            // TODO Auto-generated method stub
            
        }
        
    };
    
    public StackUI(StateManager m) {

        // pay attention to mode changes
        m.registerModeActivationVisitor(modeActivationVisitor);
        m.registerModeDeactivationVisitor(modeDeactivationVisitor);

        // listen to the stack machine
        m.stackMachine.addListener(l);
        
        this.setBackground(ApplicationFrame.caBlueL);
        this.setSize(new Dimension(300, ApplicationFrame.h));
    }
}
