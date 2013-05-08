package ox.stackgame.blockUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import ox.stackgame.blockUI.BlockManager.BlockManagerListener;
import ox.stackgame.ui.StateManager;

@SuppressWarnings("serial")
public class BlockUIPane extends JLayeredPane{
    
    //constants
    static final int scrollBarSize = ((Integer)UIManager.get("ScrollBar.width")).intValue();
    static final int CREATEPANELWIDTH = BlockUI.CELLWIDTH+scrollBarSize;
    static final int BLOCKUIWIDTH = BlockUI.CELLWIDTH+scrollBarSize;
    
    
    public BlockUIPane(StateManager modeManager, int width, int height, Color bgColor) {

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
        jsp1.setBounds(BLOCKUIGAPWIDTH, 2*BlockUI.CELLHEIGHT, CREATEPANELWIDTH, height-2*BlockUI.CELLHEIGHT);
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
        
        
//TODO: disable buttons on RunMode
        
        //Buttons
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
        
        //editButton
        final JButton editButton = new EditButton("Edit Mode", blockManager);
        editButton.setLayout(null);
        buttonPane.add(editButton);
        
        //deleteButton
        final JButton deleteButton = new DeleteButton("Delete Mode", blockManager);
        editButton.setLayout(null);
        buttonPane.add(deleteButton);
        
        add(buttonPane);
        buttonPane.setBounds(BLOCKUIGAPWIDTH, 0, CREATEPANELWIDTH, 2*BlockUI.CELLHEIGHT);

//TODO: add Labels

    }
    
    class EditButton extends JButton implements BlockManagerListener{
        public void instructionChanged(String s) {}
        public void instructionCleared() {}
        public void instructionUsed(String s) {}
        public void modeChanged(String s) {
            this.setEnabled(s != BlockManager.EDIT);
        }
        
        public EditButton(String text, final BlockManager manager){
            super(text);
            manager.addListener(this);
            this.setForeground(new Color(0, 133, 200));
            this.setSize(new Dimension(CREATEPANELWIDTH,BlockUI.CELLHEIGHT));
            this.setEnabled(manager.getMode() != BlockManager.EDIT);
        
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    // enable EditMode
                    assert (manager.getMode() != BlockManager.EDIT);

                    manager.setMode(BlockManager.EDIT);
                }
            });
        }
    }

    class DeleteButton extends JButton implements BlockManagerListener{
        public void instructionChanged(String s) {}
        public void instructionCleared() {}
        public void instructionUsed(String s) {}
        public void modeChanged(String s) {
            this.setEnabled(s != BlockManager.DELETE);
        }
        
        public DeleteButton(String text, final BlockManager manager){
            super(text);
            manager.addListener(this);
            this.setForeground(new Color(0, 133, 200));
            this.setPreferredSize(new Dimension(CREATEPANELWIDTH,BlockUI.CELLHEIGHT));
            this.setEnabled(manager.getMode() != BlockManager.DELETE);
        
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    // enable DeleteMode
                    assert (manager.getMode() != BlockManager.DELETE);

                    manager.setMode(BlockManager.DELETE);
                }
            });
        }
    }
}
