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
    private Mode oldMode;
    private ErrorUI eui;
    
    // appearance stuff
    private final CardLayout cardLayout =new CardLayout();
    private final SelectorPanel selectorPanel;
    private final DetailPanel detailPanel;
    

    public ChallengeUI(final StateManager m, final FreeDesignMode freeDesignMode, final ChallengeMode cm, ErrorUI eui) {
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
        JLabel challengeLabel = new JLabel("Title"){{
            setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
            setForeground(new Color(66, 66, 66));  
            setBorder(new EmptyBorder(15, 15, 15, 15));
            setPreferredSize(new Dimension(ApplicationFrame.LEFT_PANEL_WIDTH, 540));
            setVerticalAlignment(JLabel.TOP);
        }};
        
        DetailPanel(){
            this.setBackground(Color.white);
            this.add(challengeLabel);
            this.add(new JButton("Choose Challenge"){{
                addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        stateManager.setActiveMode(freeDesignMode);
                    }
                });
            }});
            // TODO make this button disabled when challenge not finished, and enabled when you do
            this.add(new JButton("Next"){{
                setEnabled(false);
                addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        
                    }
                });
            }});
        }
        
        void updateFromChallenge(AbstractChallenge c){
            String text = String.format(
                "<html>" +
                    "<div style='font-size: 20pt; font-weight: bold; padding-bottom: 8px'>%s</div>" +
                    "%s" +

                    "<div style='font-size: 16pt; font-weight: bold; padding: 8px 0'>Allowed Instructions</div>" +
                    "<table style='background: #eeeeee; width: 170px' cellspacing='0' cellpadding='0'>"
            , c.title, c.description );

            StringBuilder sb = new StringBuilder();
            sb.append( text );

            for (String line : c.instructionSet.keySet()){
                Integer count = c.instructionSet.get(line);
                
                sb.append( String.format(
                    "<tr>" +
                        "<td><code>%s</code></td>" +
                        "<td style='font-weight: bold; text-align: right'><code>%s</code></td>" +
                    "</tr>"
                , line, count == null ? "\u221e" : ( "x" + count ) ) );
            }

            sb.append(
                    "</table>" +
                "</html>"
            );

            challengeLabel.setText( sb.toString() );
        }
    }

    // Test if the user has passed the challenge
    private StackMachineListener listener = new StackMachineListenerAdapter() {
        public void programCounterChanged(int line) {
            // when the machine terminates, evaluate machine against challenge's hasSucceeded() function
            if (machine.isRunning()==false) {
                AbstractChallenge currChallenge = challengeMode.getChallenge();
                Boolean hasSucceeded = currChallenge.hasSucceeded(machine);
                String message = currChallenge.getMessage();
                System.out.println(hasSucceeded ? "Challenge hasSucceeded=true" : "Challenge hasSucceeded=false");
                System.out.println(message);
                // TODO display message in GUI
            }
        }
    };

    private ModeVisitor modeActivationVisitor = new ModeVisitor() {

        /**
         * When switching to RunMode, check the program against allowedInstructions.
         */
        public void visit(RunMode m) {
            System.out.println("ChallengeUI noticed RunMode has been enabled");
            // moving from ChallengeMode -> RunMode
            if (oldMode == challengeMode){ // TODO never being called
                // challengeMode should never be enabled without an active challenge.
                assert(challengeMode.getChallenge() != null); 
                // check the program is allowed by the challenge
                if (challengeMode.getChallenge().checkProgram(stateManager.stackMachine.getInstructions())==false){
                    System.out.println("Program doesn't conform to Challenge's instructionSet");
                    eui.displayError("Your program must use only the allowed instructions");
                }
                // start listening for completion
                stateManager.stackMachine.addListener(listener);
            } 
        }

        // this mode is activated!
        public void visit(ChallengeMode m) {
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
            oldMode = stateManager.getActiveMode();
            stateManager.stackMachine.removeListener(listener); // stop evaluating the machine against challenge
        }

        public void visit(ChallengeMode m) {
            oldMode = stateManager.getActiveMode();
        }

        public void visit(FreeDesignMode m) {
            oldMode = stateManager.getActiveMode();
        }
    };

}
