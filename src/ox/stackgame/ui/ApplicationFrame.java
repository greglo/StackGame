package ox.stackgame.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ox.stackgame.blockUI.BlockUIPane;
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
    final static int STORE_HEIGHT = 200;
    final static int STACK_HEIGHT = h - TAPE_HEIGHT - STORE_HEIGHT;
    final static int PROGRAMTEXTUI_PADDING = 5;
    final static int PROGRAMTEXTUI_HEIGHT = h - ( BUTTONUI_HEIGHT + TAPE_HEIGHT );

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
        programUI.setBounds( LEFT_PANEL_WIDTH, BUTTONUI_HEIGHT,
                CENTER_PANEL_WIDTH, PROGRAMTEXTUI_HEIGHT );
        contentPane.add(programUI, new Integer(0));
        
        // ChallengeUI
        {
            ChallengeUI u = new ChallengeUI(modeManager, freeDesignMode, challengeMode, eui, programUI);
            u.setLocation(0, 0);
            contentPane.add(u, new Integer(0));
        }

        
        // BlockUI
        int BlockUIWidth = CENTER_PANEL_WIDTH;
        int BlockUIHeight = PROGRAMTEXTUI_HEIGHT;
        Color BlockUIBGColor = caBlue;
        BlockUIPane BlockUI= new BlockUIPane(modeManager,BlockUIWidth,BlockUIHeight,BlockUIBGColor);
        BlockUI.setBounds(LEFT_PANEL_WIDTH, BUTTONUI_HEIGHT, BlockUIWidth, BlockUIHeight);
        contentPane.add(BlockUI, new Integer(2));
        //Whether or not BlockUI starts off as visible
        Boolean blockUIVisible = false;
        BlockUI.setVisible(blockUIVisible);
        modeManager.setBlockUIActive(blockUIVisible);
        
        
        // BlockUI switch
        int SwitchWidth = 100;
        int SwitchHeight = 40;
        BlockUIButton BlockUIButton = new BlockUIButton(BlockUI,modeManager);
        contentPane.add(BlockUIButton,new Integer(100));
        BlockUIButton.setBounds(LEFT_PANEL_WIDTH+CENTER_PANEL_WIDTH+(RIGHT_PANEL_WIDTH - SwitchWidth)/2, PROGRAMTEXTUI_HEIGHT-SwitchHeight, SwitchWidth, SwitchHeight);

        
        // ButtonUI
        {
            JComponent u = new ButtonUI(modeManager, programUI, runMode, eui);
            u.setBounds(LEFT_PANEL_WIDTH, 0, CENTER_PANEL_WIDTH, BUTTONUI_HEIGHT);
            contentPane.add(u,new Integer(1));
        }

        // TapeUI
        
        TapeUI tape = new TapeUI(modeManager, eui);
        tape.setLocation(LEFT_PANEL_WIDTH , h - TAPE_HEIGHT );

    
        JScrollPane tapePane = new JScrollPane();
        tapePane.setBounds(LEFT_PANEL_WIDTH, h - TAPE_HEIGHT, CENTER_PANEL_WIDTH + RIGHT_PANEL_WIDTH, TAPE_HEIGHT );
        tapePane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        tapePane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );

        tapePane.getViewport().add( tape);
        contentPane.add(tapePane, new Integer(1));// layer 1 is on top of 0
        
        
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
    
    @SuppressWarnings("serial")
    private static class BlockUIButton extends JButton{
        private final StateManager manager;
        
        public BlockUIButton(final BlockUIPane blockUI, final StateManager manager){
            super();
            this.manager=manager;
            this.setForeground(new Color(0, 133, 200));
            updateText();
        
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
//TODO: check if text successfully lexes before switching
                    manager.toggleBlockUIActive();
                    blockUI.setVisible(manager.isBlockUIActive());
                    updateText();
                }
            });
        }
        
        public void updateText(){
            this.setText((manager.isBlockUIActive() ? "Text Mode" : "Block Mode"));
        }
    }

}
