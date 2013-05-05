package ox.stackgame.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
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
    private final FreeDesignMode freeDesignMode;
    private final ChallengeMode challengeMode;
    private ErrorUI eui;
    
    // appearance stuff
    private final CardLayout cardLayout =new CardLayout();
    private final SelectorPanel selectorPanel;
    private final DetailPanel detailPanel;
    

    public ChallengeUI(final StateManager m, final FreeDesignMode freeDesignMode, final ChallengeMode cm, ErrorUI eui) {
        m.stackMachine.addListener(l);
        this.machine = m.stackMachine;
        this.stateManager = m;
        this.challengeMode = cm;
        this.freeDesignMode = freeDesignMode;
        this.eui = eui;

        m.registerModeActivationVisitor(modeActivationVisitor);
        m.registerModeDeactivationVisitor(modeDeactivationVisitor);

        // appearance
        this.setLayout(cardLayout);
        this.setSize(ApplicationFrame.LEFT_PANEL_WIDTH, ApplicationFrame.h);
        selectorPanel =new SelectorPanel(); 
        this.add(selectorPanel, "selectorPanel");     
        detailPanel = new DetailPanel();
        this.add(detailPanel, "detailPanel");
        
        this.setVisible(true);
    }
    
    private class SelectorPanel extends JPanel{
        final Color listItemBg =  new Color(250,250,250);
        final Color listItemBgHover =  new Color(250,210,250);

        SelectorPanel(){
            this.setBackground(Color.white);
            
            // title
            this.add(new JLabel("Choose a Challenge"){{
                setFont(new Font("Helvetica Neue", Font.BOLD, 20));
                setForeground(new Color(66, 66, 66));  
                setBorder(new EmptyBorder(15, 15, 15, 15));
            }});
            this.add(new JLabel("Or just try out the machine."){{
                setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
                setForeground(new Color(66, 66, 66));  
                setBorder(new EmptyBorder(5, 15, 25, 15));
            }});
            
            // challenge list
            JPanel listContainer = new JPanel(new GridLayout(ChallengeMode.challengeList.size(), 1, 0, 1)){{
                setBackground(Color.white);
            }};
            this.add(listContainer);
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

                        public void mousePressed(MouseEvent arg0) {  }

                        public void mouseReleased(MouseEvent arg0) {  }
                        public void mouseClicked(MouseEvent arg0) {  
                            challengeMode.setChallenge(c); // set current challenge
                            stateManager.setActiveMode(challengeMode); // switch to challengemode
                        }                    
                    });
                }});
            }
        }
    }
    
    private class DetailPanel extends JPanel{
        JLabel titleLabel = new JLabel("Title"){{
            setFont(new Font("Helvetica Neue", Font.BOLD, 20));
            setForeground(new Color(66, 66, 66));  
            setBorder(new EmptyBorder(15, 15, 15, 15));
        }};
        JLabel descLabel = new JLabel(){{ // detailPanel text must remain up to date.
            setText("<html>An arithmetic instruction <b>pops</b> two values from the stack, performs some operation and pushes the result back. An example of such an instruction is ADD, which does exactly what you think it does. CONST simply pushes the value after it onto the stack.</html>");
            setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
            setForeground(new Color(66, 66, 66));  
            setBorder(new EmptyBorder(15, 15, 15, 15));
            this.setPreferredSize(new Dimension(ApplicationFrame.LEFT_PANEL_WIDTH,250));
            setBackground(new Color(245,245,245));
            setOpaque(true);
            setVerticalAlignment(JLabel.TOP);
            setVerticalTextPosition(JLabel.TOP);
        }};
        JLabel allowedInstructions = new JLabel(){{ // detailPanel text must remain up to date.
            setText("<html>lakshdkahskdjklsajd</html>");
            setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
            setForeground(new Color(66, 66, 66));  
            setBorder(new EmptyBorder(15, 15, 15, 15));
            this.setPreferredSize(new Dimension(ApplicationFrame.LEFT_PANEL_WIDTH,100));
            setBackground(Color.red);
            setOpaque(true);
            setVerticalAlignment(JLabel.TOP);
            setVerticalTextPosition(JLabel.TOP);
        }};
        
        DetailPanel(){
            this.setBackground(Color.white);
            // title TODO must remain uptodate
            this.add(titleLabel);
            this.add(descLabel);
            this.add(new JLabel("Allowed Instructions:"){{
                setFont(new Font("Helvetica Neue", Font.BOLD, 14));
                setForeground(new Color(66, 66, 66));  
                setBorder(new EmptyBorder(15, 15, 15, 15));
            }});
            this.add(allowedInstructions);
            
            this.add(new JButton("Back"){{
                addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        stateManager.setActiveMode(freeDesignMode);
                    }
                });
            }});
        }
        
        void updateFromChallenge(AbstractChallenge c){
            titleLabel.setText(c.title);
            descLabel.setText("<html>"+c.description+"</html>");
        }
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
            
            // display detail panel
            detailPanel.updateFromChallenge(challengeMode.getChallenge());
            cardLayout.show(ChallengeUI.this,"detailPanel");
        }

        public void visit(FreeDesignMode m) {
            // display selector panel
            cardLayout.show(ChallengeUI.this,"selectorPanel");
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
