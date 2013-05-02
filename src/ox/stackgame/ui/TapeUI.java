package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import ox.stackgame.stackmachine.CharStackValue;
import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.StackValue;
import ox.stackgame.stackmachine.exceptions.InvalidCharException;

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
    private final int boxSize = 50;
    private final int padding = 10;

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
        
        inputTape = new LinkedList<StackValue<?>>();
        activeMachine = m.stackMachine;

        // listen to the stack machine
        m.stackMachine.addListener(l);

        // sort out appearance
        this.setBackground(ApplicationFrame.caBlue2L);
        this.setSize(new Dimension(750 - 2 * ApplicationFrame.p, boxSize + 2*padding));
        
        inputTape.add(new IntStackValue(4));
        try {
            inputTape.add(new CharStackValue('d'));
        } catch (InvalidCharException e) {
        }
        
        resetCursors();

        // TODO create scrolling (two scrollbars?) monotype font etc, different
        // colours for input/output

    }
    
    protected void paintComponent( Graphics graphics ) {
        super.paintComponent( graphics );

        Graphics2D g = ( Graphics2D ) graphics;

        int i = 0;
        for( StackValue< ? > v : inputTape ) {
            int x = i * ( boxSize + padding );

            g.setColor( Color.gray );
            g.fillRect( x + padding, padding, boxSize, boxSize );

            g.setColor( Color.WHITE );
            g.drawString( v.toString(), x + padding + boxSize/3, padding + boxSize/2 );

            i++;
        }
    }

    protected void resetCursors() {
        // TODO Auto-generated method stub
        
    }
}
