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
    final JLabel l;

    public void displayError(String message){
       l.setText("<html><div>"+message+"</div></html>");
       this.setVisible(true);
    }
    
    public void clearErrors(){
       l.setText("[no errors]");
    }
    
    public ErrorUI(){
        this.setBackground(ApplicationFrame.caBlue);
        this.setSize(ApplicationFrame.CENTER_PANEL_WIDTH-2*ApplicationFrame.p, 30);
        l = new JLabel();
        l.setForeground(Color.RED);
        this.add(l);
    }
}
