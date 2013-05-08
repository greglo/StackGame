package ox.stackgame.blockUI;

import java.awt.Point;
import java.util.Map;

import javax.swing.JOptionPane;

import ox.stackgame.stackmachine.Lexer;
import ox.stackgame.stackmachine.Lexer.LexerException;
import ox.stackgame.stackmachine.instructions.Instruction;

/** Handles events responsible for creation of WallElements.
 * 	Methods here are a part of the Controller. 
 **/
public class CreateHandler extends AbstractStretchBoxHandler{
	public CreateHandler(BlockUI blockUI) {
		super(blockUI);
	}

	protected Instruction newInstruction() {
	    String str = blockUI.getBlockManager().getInstruction();
	    if(str == null)throw new IllegalArgumentException("No Instruction");
	    
	    if(blockUI.getBlockManager().availableInstructions.get(str) == 0){
	        JOptionPane.showMessageDialog(null, "No more '"+str+"' available.");
	        return null;
	    }
	    
	    String[] strs = str.split(" ");
        if(strs.length == 0)throw new IllegalArgumentException("No Instruction");
	    
	    if(strs.length == 1){
	        return new Instruction(strs[0]);
	    }else{
	        String toLex = strs[0];
	        if(strs[1].equals("*")){
	            String input = JOptionPane.showInputDialog(null, "Enter argument: ", "", 1);
	            toLex += " " + input;
	            
	        }else toLex = str;
	        
	        Instruction instruction = null;
            try {
                instruction = Lexer.lex(toLex).get(0);
            } catch (LexerException e) {
                JOptionPane.showMessageDialog(null, "'"+toLex+"' is not a valid instruction.");
                return null;
            }
            
            //decrease the amount of this instruction available
            Map<String, Integer> available = blockUI.getBlockManager().availableInstructions;
            int count = available.get(str);
            if(count>0)
                available.put(str, count-1);
            
            //inform about its use
            blockUI.getBlockManager().useInstruction();
	        
	        return instruction;    

	    }
	        
	
	}
	
	
	protected void boxStretchingFinished(Point boxOrigin, Point boxTarget) {
		int height = Math.abs(boxOrigin.y - boxTarget.y);
		
		for(int curY = 0; curY <= height; curY++){
		    Instruction i = newInstruction();
		    if(i == null) break;
		    blockUI.getCurrentStackMachine().addInstruction(boxOrigin.y + curY, i);
		}
		
		//Evoke synchronisation with TextUI
		blockUI.getBlockManager().sync();
 	}
	
}
