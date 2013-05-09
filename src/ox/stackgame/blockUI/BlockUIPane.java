package ox.stackgame.blockUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import ox.stackgame.blockUI.BlockManager.BlockManagerListener;
import ox.stackgame.ui.ChallengeMode;
import ox.stackgame.ui.FreeDesignMode;
import ox.stackgame.ui.ModeVisitor;
import ox.stackgame.ui.RunMode;
import ox.stackgame.ui.StateManager;

@SuppressWarnings("serial")
public class BlockUIPane extends JLayeredPane{
    
    //constants
    static final int scrollBarSize = ((Integer)UIManager.get("ScrollBar.width")).intValue();
    static final int CREATEPANELWIDTH = BlockUI.CELLWIDTH+scrollBarSize;
    static final int BLOCKUIWIDTH = BlockUI.CELLWIDTH+scrollBarSize;
    
    final EditButton editButton;
//    final DeleteButton deleteButton;
    
    private final BlockManager blockManager;
    
    public BlockManager getBlockManager(){
        return blockManager;
    }
    
    public BlockUIPane(StateManager modeManager, int width, int height, Color bgColor) {

        //gap between the two and on the sides
        final int BLOCKUIGAPWIDTH = (width - CREATEPANELWIDTH - BLOCKUIWIDTH)/3;
        final int FRAME = 5;
        
        //background colour
        setBackground(bgColor);
        setOpaque(true);
        
        //BlockManager
        blockManager = new BlockManager();
        
        // CreatePanel
        CreatePanel createPanel = new CreatePanel(blockManager, modeManager);

        // A scroll pane for CreatePanel
        JScrollPane jsp1 = new JScrollPane();
        jsp1.setBounds(BLOCKUIGAPWIDTH, BlockUI.CELLHEIGHT+FRAME, CREATEPANELWIDTH, height-BlockUI.CELLHEIGHT-2*FRAME);
        jsp1.setBorder(new EmptyBorder(0, 0, 0, 0));
        jsp1.setViewportView(createPanel);
        jsp1.getViewport().setBackground(bgColor);
        add(jsp1, new Integer(1));
        
        // BlockUI
        BlockUI blockUI = new BlockUI(blockManager, modeManager);

        // A scroll pane for BlockUI
        JScrollPane jsp2 = new JScrollPane();
        jsp2.setBounds(2*BLOCKUIGAPWIDTH+CREATEPANELWIDTH, FRAME, BlockUI.CELLWIDTH+scrollBarSize, height-2*FRAME);
        jsp2.setBorder(new EmptyBorder(0, 0, 0, 0));
        jsp2.setViewportView(blockUI);
        jsp2.getViewport().setBackground(bgColor);
        add(jsp2, new Integer(1));
        
        
        //Buttons
        //editButton
        editButton = new EditButton("Edit Mode", blockManager);
        add(editButton);
        editButton.setBounds(BLOCKUIGAPWIDTH, 1+FRAME, CREATEPANELWIDTH, BlockUI.CELLHEIGHT-2);
        
        //deleteButton
/*        deleteButton = new DeleteButton("Delete Mode", blockManager);
        add(deleteButton);
        deleteButton.setBounds(BLOCKUIGAPWIDTH, BlockUI.CELLHEIGHT+1+FRAME, CREATEPANELWIDTH, BlockUI.CELLHEIGHT-2);
*/        
        //disable buttons on RunMode
        modeManager.registerModeActivationVisitor(new UpdateButtonVisitor());

        //default mode
        blockManager.setMode(BlockManager.EDIT);
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
            this.setEnabled(manager.getMode() != BlockManager.EDIT);
        
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    assert (manager.getMode() != BlockManager.EDIT);

                    // enable EditMode
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
            this.setEnabled(manager.getMode() != BlockManager.DELETE);
        
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    assert (manager.getMode() != BlockManager.DELETE);

                    // enable DeleteMode
                    manager.setMode(BlockManager.DELETE);
                }
            });
        }
    }
    
    //disables buttons on runMode
    class UpdateButtonVisitor implements ModeVisitor{
        public void visit(ChallengeMode m) {
//            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
        }

        public void visit(RunMode m) {
//            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        }

        public void visit(FreeDesignMode m) {
//            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
        }
        
    }
}
