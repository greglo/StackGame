package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.Instruction;

public class ButtonUI extends JPanel {   
    
    
    public ButtonUI(final StateManager modeManager, final ProgramTextUI tui, final RunMode runMode){
        
        this.setBackground(Color.BLACK);
        this.setSize(new Dimension(80,300));
        
     // create buttons
        int r = 60;
        int p = ApplicationFrame.p;
        int h = ApplicationFrame.h;
        int buttonStartY = p;

        // TODO position the buttons in a more sensible way. Get rid of
        // +p+h+ldfklsdkf....

        // create step1 button
        final JButton step1Button = new JButton("Step1");
        step1Button.setForeground(new Color(0, 133, 200));
        step1Button.setSize(r, r);
        this.add(step1Button);

        // create stepAll Button
        // allows the user to see each command animated until the machine halts.
        final JButton stepAllButton = new JButton("StepAll");
        stepAllButton.setForeground(new Color(0, 133, 200));
        stepAllButton.setSize(r, r);
        this.add(stepAllButton);

        // pauseButton
        // only relevant to stepAll Button
        final JButton pauseButton = new JButton("Pause");
        pauseButton.setForeground(new Color(0, 133, 200));
        pauseButton.setEnabled(false);
        pauseButton.setSize(r, r);
        this.add(pauseButton);

        // create runAll button
        final JButton runAllButton = new JButton("RunAll");
        runAllButton.setForeground(new Color(0, 133, 200));
        runAllButton.setSize(r, r);
        this.add(runAllButton);

        // create reset button
        final JButton resetButton = new JButton("Reset");
        resetButton.setEnabled(false);
        resetButton.setSize(r, r);
        this.add(resetButton);

        // button logic
        step1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // when not in runMode already, feed the text through the lexer,
                // switch to RunMode (storing the current mode), pc:=0, call
                // 'step'
                if (modeManager.getActiveMode() != runMode) {
                    // feed text through lexer
                    // update modeManager.stackMachine
                    modeManager.stackMachine.loadInstructions(tui.getProgram());
                    // switch to RunMode
                    modeManager.setActiveMode(runMode);
                }
                // call step
                try {
                    modeManager.stackMachine.step();
                } catch (StackRuntimeException e) {
                    // TODO Handle machine errors
                }
                
                // should become disabled when pc is past the end of the program.
            }
        });
        
        runAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // for a dirty textarea, lex the text, switch to RunMode
                // (storing the current mode), call runAll on the stackMachine.
                // when clicked in RunMode, call runAll
                // disable this button
            }
        });
        
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // disabled in designMode.
                // enabled if machine is being animated
                // when clicked, stop the stepAll timer from calling step
                // enable stepAllButton, enable runAllButton
                
                runMode.pause();
                pauseButton.setEnabled(false); // cant repause
            }
        });
        
        stepAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // for a dirty textarea, lex the text, switch to RunMode
                // (storing the current mode), create a timer, call step on the
                // timer until machine terminates.
                // when clicked in runMode, just do the timer stuff
                // disable this button
                // enable pausebutton
                modeManager.setActiveMode(runMode);
                runMode.run();
                
                pauseButton.setEnabled(true);
            }
        });
        
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // (should not be enabled in DesignMode)
                // switch back to old mode, call reset on the stackmachine
                
            }
        });
        
        // do button enabling/disabling
        modeManager.stackMachine.addListener( new StackMachineListenerAdapter() {
            public void programCounterChanged(int line) {
                // isRunning == true if there are more exceptions to execute
                Boolean b = modeManager.stackMachine.isRunning();
                step1Button.setEnabled(b);
                stepAllButton.setEnabled(b);
                runAllButton.setEnabled(b);
                
                resetButton.setEnabled(modeManager.stackMachine.getProgramCounter() != 0);
                
            }
        });
        
    }
}
