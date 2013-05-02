package ox.stackgame.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Display errors in a user friendly way.
 * @author danfox
 *
 */
@SuppressWarnings("serial")
public class ErrorUI extends JPanel {

    private final JScrollPane jsp = new JScrollPane();
    private final JPanel errorList = new JPanel();
    
    public void displayError(String message){
        errorList.add(new JLabel(message));
    }
    
    public void clearErrors(){
        errorList.removeAll();
    }
    
    public ErrorUI(){
        super (new BorderLayout());
        this.setSize(250, 150);
        this.add(jsp, BorderLayout.CENTER);
        
        jsp.getViewport().add(errorList);
        
        JLabel l = new JLabel("<html><div style='text-align:left;'>yo mama <br />yo mama <br />yo mama <br />yo mama <br />yo mama <br />yo mama <br />yo mama <br />yo mama<br />yo mama</div></html>");
        l.setForeground(Color.RED);
        errorList.add(l);
        
        jsp.getViewport().setBackground(ApplicationFrame.caBlue2L);
    }
}
