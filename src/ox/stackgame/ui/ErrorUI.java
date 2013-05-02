package ox.stackgame.ui;

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

    private JScrollPane jsp = new JScrollPane();
    
    public void displayError(String message){
        jsp.add(new JLabel(message));
    }
    
    public void clearErrors(){
        jsp.removeAll();
    }
    
    public ErrorUI(){
        this.setSize(100, 100);
        this.setBackground(Color.RED);
        this.add(jsp);
    }
}
