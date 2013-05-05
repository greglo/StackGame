package ox.stackgame.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ox.stackgame.blockUI.BlockManager;
import ox.stackgame.blockUI.BlockUI;
import ox.stackgame.blockUI.CreatePanel;
import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.instructions.Instruction;

public class ApplicationFrame {
    final static int h = 590;
    final static int p = 15;
    final static int RIGHT_PANEL_WIDTH = 200;
    final static int CENTER_PANEL_WIDTH = 450;
    final static int LEFT_PANEL_WIDTH = 250;
    final static int BUTTONUI_HEIGHT = 30;
    final static int TAPE_HEIGHT = TapeUI.UIHeight;
    final static int STORE_HEIGHT = 180;
    final static int STACK_HEIGHT = h - TAPE_HEIGHT - STORE_HEIGHT;
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
        
        // UIs
        ErrorUI eui = new ErrorUI();

        // initialise modes
        StateManager modeManager = new StateManager(machine);
        RunMode runMode = new RunMode(machine,eui);
        FreeDesignMode freeDesignMode = new FreeDesignMode();
        ChallengeMode challengeMode = new ChallengeMode();


        // setup window to display
        final JFrame frame = new JFrame("Stack Game");
        frame.setBounds(200, 100, LEFT_PANEL_WIDTH+CENTER_PANEL_WIDTH+RIGHT_PANEL_WIDTH, h);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(caBlue);

        // window contents
        JLayeredPane contentPane = new JLayeredPane();
        contentPane.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH+CENTER_PANEL_WIDTH+RIGHT_PANEL_WIDTH, h));
        frame.add(contentPane, BorderLayout.CENTER);

        
        // ChallengeUI
        {
            ChallengeUI u = new ChallengeUI(modeManager, freeDesignMode, challengeMode, eui);
            u.setLocation(0, 0);
            contentPane.add(u, new Integer(0));
        }

        // StackUI
        {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBounds(LEFT_PANEL_WIDTH+CENTER_PANEL_WIDTH, STORE_HEIGHT, RIGHT_PANEL_WIDTH, STACK_HEIGHT );
            scrollPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
            scrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

            scrollPane.getViewport().add( new StackUI( modeManager ) );

            contentPane.add( scrollPane, new Integer( 0 ) );
        }

        // StoreUI
        {
            JComponent u = new StoreUI(modeManager);
            u.setBounds(LEFT_PANEL_WIDTH+CENTER_PANEL_WIDTH, 0, RIGHT_PANEL_WIDTH, STORE_HEIGHT);
            contentPane.add(u, new Integer(1));
        }

        // ProgramUI
        ProgramTextUI programUI = new ProgramTextUI(modeManager, runMode, eui);
        programUI.setLocation(LEFT_PANEL_WIDTH, BUTTONUI_HEIGHT);
        contentPane.add(programUI, new Integer(0));

        // CreatePanel
        BlockManager blockManager = new BlockManager();
        CreatePanel createPanel = new CreatePanel(blockManager, modeManager);
        createPanel.setLocation(LEFT_PANEL_WIDTH, BUTTONUI_HEIGHT);
        //contentPane.add(createPanel, new Integer(2));
        
        // BlockUI
        BlockUI blockUI = new BlockUI(blockManager, modeManager);
        blockUI.setLocation(LEFT_PANEL_WIDTH + CENTER_PANEL_WIDTH/2, BUTTONUI_HEIGHT);
        //contentPane.add(blockUI, new Integer(2));
        
        // ButtonUI
        {
            JComponent u = new ButtonUI(modeManager, programUI, runMode, eui);
            u.setBounds(LEFT_PANEL_WIDTH, 0, CENTER_PANEL_WIDTH, BUTTONUI_HEIGHT);
            contentPane.add(u,new Integer(1));
        }

        // TapeUI
        
        TapeUI tape = new TapeUI(modeManager, eui);
        tape.setLocation(LEFT_PANEL_WIDTH , h - TAPE_HEIGHT );
        contentPane.add(tape, new Integer(1));// layer 1 is on top of 0
    
        
        // ErrorUI
        {
            eui.setLocation(LEFT_PANEL_WIDTH+p, tape.getY()-eui.getHeight()-p);
            contentPane.add(eui, new Integer(1));
        }

        //challengeMode.setChallenge(ChallengeMode.challengeList.get(0));
        modeManager.setActiveMode(freeDesignMode);
        frame.pack();
        frame.setVisible(true);
    }

}
