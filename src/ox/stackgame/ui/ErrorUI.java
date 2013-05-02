package ox.stackgame.ui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Display errors in a user friendly way.
 * @author danfox
 *
 */
@SuppressWarnings("serial")
public class ErrorUI extends JPanel {

    public void displayError(String message){
       
    }
    
    public void clearErrors(){
       
    }
    
    public ErrorUI(){
        this.setSize(300, 50);
        JLabel l = new JLabel("<html><div style='text-align:left;'>yo mama yo mama yo mama yo mama yo mama yo mama yo mama yo mamayo mama</div></html>");
        l.setForeground(Color.RED);
        this.add(l);
    }
}
