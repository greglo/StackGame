package ox.stackgame.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.instructions.Instruction;

public class ApplicationFrame {
    final static int MAGIC = 13;
    final static int h = 590;
    final static int p = 15;
    final static int RIGHT_PANEL_WIDTH = 200;
    final static int CENTER_PANEL_WIDTH = 450;
    final static int TAPE_HEIGHT = TapeUI.UIHeight;
    final static int STORE_HEIGHT = 180;
    final static int STACK_HEIGHT = h - 2 * p - TAPE_HEIGHT - STORE_HEIGHT;
    final static Color caBlue = new Color(35, 44, 49);
    final static Color caBlueL = new Color(50, 57, 60);
    final static Color caBlue2L = new Color(82, 88, 92);
    final static Color codeacademyBlack = new Color(33, 33, 33);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        // instantiate the application with a trivial example machine: (5 + 3A)
        // * 2
        List<Instruction> instructions = new ArrayList<Instruction>();
        instructions.add(new Instruction("const", new IntStackValue(5)));
        instructions.add(new Instruction("const", new IntStackValue(3)));
        instructions.add(new Instruction("add"));
        instructions.add(new Instruction("const", new IntStackValue(2)));
        instructions.add(new Instruction("mul"));
        instructions.add(new Instruction("output"));
        StackMachine machine = new StackMachine(instructions);

        // initialise modes
        StateManager modeManager = new StateManager(machine);
        RunMode runMode = new RunMode(machine);
        Mode freeDesignMode = new FreeDesignMode();
        ChallengeMode challengeMode = new ChallengeMode();
        challengeMode.setChallenge(ChallengeMode.challengeList.get(0));


        // setup window to display
        final JFrame frame = new JFrame("Stack Game");
        frame.setBounds(200, 100, 900, h);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // window contents
        JLayeredPane contentPane = new JLayeredPane();
        contentPane.setPreferredSize(new Dimension(900, h));
        frame.add(contentPane, BorderLayout.CENTER);

        // UI's =======
        ErrorUI eui = new ErrorUI();
        
        // ChallengeUI
        {
            ChallengeUI u = new ChallengeUI(modeManager, challengeMode, eui);
            u.setLocation(0, 0);
            contentPane.add(u, new Integer(0));
        }

        // StackUI
        {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBounds(700, STORE_HEIGHT, RIGHT_PANEL_WIDTH, STACK_HEIGHT );
            scrollPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
            scrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

            scrollPane.getViewport().add( new StackUI( modeManager ) );

            contentPane.add( scrollPane, new Integer( 0 ) );
        }

        // StoreUI
        {
            JComponent u = new StoreUI(modeManager);
            u.setBounds(700, 0, RIGHT_PANEL_WIDTH, STORE_HEIGHT);
            contentPane.add(u, new Integer(1));
        }

        // ProgramUI
        ProgramTextUI programUI = new ProgramTextUI(modeManager, runMode, eui);
        programUI.setLocation(250, 0);
        contentPane.add(programUI, new Integer(0));
    
        
        // ButtonUI
        {
            JComponent u = new ButtonUI(modeManager, programUI, runMode, eui);
            u.setLocation(700-p-u.getWidth(),p);
            contentPane.add(u,new Integer(1));
        }

        // TapeUI
        
        TapeUI tape = new TapeUI(modeManager);
        tape.setLocation(250 + p, h - p - TAPE_HEIGHT - MAGIC);
        contentPane.add(tape, new Integer(1));// layer 1 is on top of 0
    
        
        // ErrorUI
        {
            eui.setLocation(250+p, tape.getY()-eui.getHeight()-p);
            contentPane.add(eui, new Integer(1));
        }

        modeManager.setActiveMode(freeDesignMode);

        frame.setVisible(true);
    }

}
