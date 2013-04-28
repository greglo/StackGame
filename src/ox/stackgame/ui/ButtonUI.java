package ox.stackgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.Instruction;

public class ButtonUI extends JPanel {
    public ButtonUI(final StateManager modeManager){

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
//        step1Button.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                // when not in runMode already, feed the text through the lexer,
//                // switch to RunMode (storing the current mode), pc:=0, call
//                // 'step'
//                if (modeManager.getActiveMode() != runMode) {
//                    // feed text through lexer
//                    String text = jta.getText();
//                    List<Instruction> instructions = lex(text);
//                    // update modeManager.stackMachine
//                    modeManager.stackMachine.loadInstructions(instructions);
//                    // switch to RunMode
//                    modeManager.setActiveMode(runMode);
//                }
//                // call step
//                try {
//                    modeManager.stackMachine.step();
//                } catch (StackRuntimeException e) {
//                    // TODO Handle machine errors
//                }
//            }
//        });
        this.add(step1Button);

        // create stepAll Button
        // allows the user to see each command animated until the machine halts.
        final JButton stepAllButton = new JButton("StepAll");
        stepAllButton.setForeground(new Color(0, 133, 200));
        stepAllButton.setSize(r, r);
//        stepAllButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                // for a dirty textarea, lex the text, switch to RunMode
//                // (storing the current mode), create a timer, call step on the
//                // timer until machine terminates.
//                // when clicked in runMode, just do the timer stuff
//                // disable this button
//                // enable pausebutton
//                modeManager.setActiveMode(runMode);
//                runMode.run();
//            }
//        });
        this.add(stepAllButton);

        // pauseButton
        // only relevant to stepAll Button
        final JButton pauseButton = new JButton("Pause");
        pauseButton.setEnabled(false);
        pauseButton.setForeground(new Color(0, 133, 200));
        pauseButton.setSize(r, r);
//        pauseButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                // disabled in designMode.
//                // enabled if machine is being animated
//                // when clicked, stop the stepAll timer from calling step
//                // enable stepAllButton, enable runAllButton
//            }
//        });
        this.add(pauseButton);

        // create runAll button
        final JButton runAllButton = new JButton("RunAll");
        runAllButton.setForeground(new Color(0, 133, 200));
        runAllButton.setSize(r, r);
//        runAllButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                // for a dirty textarea, lex the text, switch to RunMode
//                // (storing the current mode), call runAll on the stackMachine.
//                // when clicked in RunMode, call runAll
//                // disable this button
//            }
//        });
        this.add(runAllButton);

        // create reset button
        final JButton resetButton = new JButton("Reset");
        resetButton.setEnabled(false);
        resetButton.setSize(r, r);
//        resetButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                // (should not be enabled in DesignMode)
//                // switch back to old mode, call reset on the stackmachine
//            }
//        });
        this.add(resetButton);
    }
}
