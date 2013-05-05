package ox.stackgame.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ox.stackgame.challenge.AbstractChallenge;
import ox.stackgame.stackmachine.IntStackValue;
import ox.stackgame.stackmachine.StackMachine;
import ox.stackgame.stackmachine.StackMachineListener;
import ox.stackgame.stackmachine.StackMachineListenerAdapter;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * Allow the user to select a challenge from a predefined list. Listen to the
 * machine during RunMode, evaluating it against the current challenge success
 * criteria and displaying either success or failure messages as appropriate.
 * Also constrains execution to the challenge's specified maxInstructions,
 * stackSize and allowedInstructions.
 * 
 * @author danfox
 * @author rgossiaux
 * 
 */

@SuppressWarnings("serial")
public class ChallengeUI extends JPanel {

    /**
     * currentChallenge stores the active challenge. This can be null; denoting
     * the user has not yet selected a challenge.
     */
    //protected AbstractChallenge currChallenge = null;
    //protected JLabel descLabel;
    private StackMachine machine;
    private final StateManager stateManager;
    private final ChallengeMode challengeMode;
    private ErrorUI eui;
    
    // appearance stuff
    private final CardLayout cardLayout =new CardLayout(); 

    public ChallengeUI(StateManager m, final ChallengeMode cm, ErrorUI eui) {
        m.stackMachine.addListener(l);
        this.machine = m.stackMachine;
        this.stateManager = m;
        this.challengeMode = cm;
        this.eui = eui;

        m.registerModeActivationVisitor(modeActivationVisitor);
        m.registerModeDeactivationVisitor(modeDeactivationVisitor);

        // appearance
        this.setLayout(cardLayout);
        this.setSize(ApplicationFrame.LEFT_PANEL_WIDTH, ApplicationFrame.h);

        
        
        
        // set up selectorpanel
        final JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)){{
            setBackground(Color.white);
        }};
        this.add(selectorPanel, "selectorPanel");
        // title
        selectorPanel.add(new JLabel("Choose a Challenge"){{
            setFont(new Font("Helvetica Neue", Font.BOLD, 20));
            setForeground(new Color(66, 66, 66));  
            setBorder(new EmptyBorder(15, 15, 15, 15));
        }});
        selectorPanel.add(new JLabel("Or just try out the machine."){{
            setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
            setForeground(new Color(66, 66, 66));  
            setBorder(new EmptyBorder(5, 15, 25, 15));
        }});
        
        // challenge list
        JPanel listContainer = new JPanel(new GridLayout(ChallengeMode.challengeList.size(), 1, 0, 1)){{
            setBackground(Color.white);
        }};
        selectorPanel.add(listContainer);
        final Color listItemBg =  new Color(250,250,250);
        final Color listItemBgHover =  new Color(250,210,250);
        for (int i=0; i<ChallengeMode.challengeList.size();i++){
            final AbstractChallenge c = ChallengeMode.challengeList.get(i);           
            listContainer.add(new JLabel((i+1)+". " + c.title, JLabel.LEFT){{
                setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
                setForeground(new Color(66, 66, 66));
                setBorder(new EmptyBorder(5, 15, 5, 5));
                setPreferredSize(new Dimension(ApplicationFrame.LEFT_PANEL_WIDTH, 40));
                setBackground(listItemBg);
                setOpaque(true);    
                final JLabel l = this;
                addMouseListener(new MouseListener(){
                    public void mouseEntered(MouseEvent arg0) {
                        l.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        l.setBackground(listItemBgHover);
                    }

                    public void mouseExited(MouseEvent arg0) {
                        l.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        l.setBackground(listItemBg);
                    }

                    public void mousePressed(MouseEvent arg0) {
                        System.out.println("clicked "+c.title);
                        cm.setChallenge(c);
                    }

                    public void mouseReleased(MouseEvent arg0) {  }
                    public void mouseClicked(MouseEvent arg0) {  }                    
                });
            }});
        }
        

        
        
        
        
        
        // set up detailPanel
        final JPanel detailPanel = new JPanel();
        detailPanel.setBackground(Color.green);
        this.add(detailPanel, "detailPanel");
        
        
        
        
        // Switching
        cardLayout.show(this,"selectorPanel");
        
//        this.setLayout(new FlowLayout());
//        this.setBackground(Color.white);
//        JLabel l = new JLabel("Challenge");
//        l.setFont(new Font("Helvetica Neue", Font.BOLD, 23));
//        l.setForeground(new Color(66, 66, 66));
//        this.add(l);
//        descLabel = new JLabel();
//        this.add(descLabel);
        
        this.setVisible(true);
    }

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void programCounterChanged(int line) {
//            if (stateManager.getActiveMode() instanceof ChallengeMode && challengeMode.getChallenge() != null){
//                // when the machine terminates, evaluate machine against challenge's hasSucceeded() function
//                if (machine.isRunning()==false) {
//                    Boolean hasSucceeded = challengeMode.getChallenge().hasSucceeded(machine);
//                    String message = challengeMode.getChallenge().getMessage();
//                    System.out.println(hasSucceeded ? "Passed" : "Failed");
//                    System.out.println(message);
//                    // TODO display message in GUI
//                }
//            }
        }
    };
    
    private void checkMachineInstructions(StackMachine m){
        if (challengeMode.getChallenge() != null) {
                        
            // check the program is allowed by the challenge
            if (challengeMode.getChallenge().checkProgram(m.getInstructions())==false){
                System.out.println("Program doesn't conform to Challenge's instructionSet");
                eui.displayError("Your program must use only the allowed instructions");
            }
        }
    }

    private ModeVisitor modeActivationVisitor = new ModeVisitor() {

        /**
         * When switching to RunMode, check the program against allowedInstructions.
         */
        public void visit(RunMode m) {
//            if (stateManager.getActiveMode() instanceof ChallengeMode)
//                ChallengeUI.this.checkMachineInstructions(machine);
        }

        // this mode is activated!
        public void visit(ChallengeMode m) {
            //descLabel.setText("<html>"+challengeMode.getChallenge().description+"</html>");
            //ChallengeUI.this.setVisible(true);
        }

        public void visit(FreeDesignMode m) {
        }
    };

    // code to be executed when a mode is deactivated
    private ModeVisitor modeDeactivationVisitor = new ModeVisitor() {
        public void visit(RunMode m) {
        }

        // this mode is deactivated
        public void visit(ChallengeMode m) {
            //ChallengeUI.this.setVisible(false);
        }

        public void visit(FreeDesignMode m) {
        }
    };

}
