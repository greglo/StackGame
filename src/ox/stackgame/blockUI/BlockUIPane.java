package ox.stackgame.blockUI;

import java.awt.Color;

import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import ox.stackgame.ui.StateManager;

@SuppressWarnings("serial")
public class BlockUIPane extends JLayeredPane{
    
    
    public BlockUIPane(StateManager modeManager, int width, int height, Color bgColor) {

        //constants
//        final Color bgColor = ApplicationFrame.caBlue;
//        final int height = ApplicationFrame.PROGRAMTEXTUI_HEIGHT;
//        final int width = ApplicationFrame.CENTER_PANEL_WIDTH;
        final int scrollBarSize = ((Integer)UIManager.get("ScrollBar.width")).intValue();
        final int CREATEPANELWIDTH = BlockUI.CELLWIDTH+scrollBarSize;
        final int BLOCKUIWIDTH = BlockUI.CELLWIDTH+scrollBarSize;
        //gap between the two and on the sides
        final int BLOCKUIGAPWIDTH = (width - CREATEPANELWIDTH - BLOCKUIWIDTH)/3;
        
        //background colour
        setBackground(bgColor);
        setOpaque(true);
        
        //BlockManager
        BlockManager blockManager = new BlockManager();
        
        // CreatePanel
        CreatePanel createPanel = new CreatePanel(blockManager, modeManager);

        // A scroll pane for CreatePanel
        JScrollPane jsp1 = new JScrollPane();
        jsp1.setBounds(BLOCKUIGAPWIDTH, 0, CREATEPANELWIDTH, height);
        jsp1.setBorder(new EmptyBorder(0, 0, 0, 0));
        jsp1.setViewportView(createPanel);
        jsp1.getViewport().setBackground(bgColor);
        add(jsp1, new Integer(1));
        
        // BlockUI
        BlockUI blockUI = new BlockUI(blockManager, modeManager);

        // A scroll pane for BlockUI
        JScrollPane jsp2 = new JScrollPane();
        jsp2.setBounds(2*BLOCKUIGAPWIDTH+CREATEPANELWIDTH, 0, BlockUI.CELLWIDTH+scrollBarSize, height);
        jsp2.setBorder(new EmptyBorder(0, 0, 0, 0));
        jsp2.setViewportView(blockUI);
        jsp2.getViewport().setBackground(bgColor);
        add(jsp2, new Integer(1));
        
        
//TODO: add buttons for mode changes
//TODO: add Labels

    }
}
