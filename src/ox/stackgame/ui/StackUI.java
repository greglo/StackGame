/**
 * 
 */
package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.StackValue;
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

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void stackChanged(EvaluationStack stack) {
            updateLab(stack);
        }
        
    };
    
    private final JLabel lab;
    
    private void updateLab(EvaluationStack stack){
        StringBuilder sb = new StringBuilder();
        // iterate through stack, display StackValues
        for(StackValue<?> v : stack){
            sb.append(v.toString() + "\n");
        }
        lab.setText("STACK:\n\n" + sb.toString());
    }
    
    public StackUI(StateManager m) {

        // pay attention to mode changes
        m.registerModeActivationVisitor(modeActivationVisitor);
        m.registerModeDeactivationVisitor(modeDeactivationVisitor);

        // listen to the stack machine
        m.stackMachine.addListener(l);
        
        // draw a label to store stack information
        lab = new JLabel("HELLO");
        lab.setForeground(Color.WHITE);
        this.add(lab);
        
        updateLab(m.stackMachine.getStack());
        
        this.setBackground(ApplicationFrame.caBlueL);
        this.setSize(new Dimension(300, ApplicationFrame.h));
    }
}
