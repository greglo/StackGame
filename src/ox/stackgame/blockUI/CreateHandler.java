package ox.stackgame.blockUI;

import java.awt.Point;

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
	    
	    String[] strs = str.split(" ");
        if(strs.length == 0)throw new IllegalArgumentException("No Instruction");
	    
	    if(strs.length == 1){
	        return new Instruction(strs[0]);
	    }else{
	        String toLex = strs[0];
	        System.out.println("strs[1] == '" + strs[1] + "'");
	        if(strs[1].equals("*")){
	            System.out.println("It is *");
//TODO: prompt for argument
	            String input = JOptionPane.showInputDialog(null, "Enter argument: ", "", 1);
	            toLex += " " + input;
	            
	        }else toLex = str;
	        Instruction instruction = null;
            try {
                instruction = Lexer.lex(toLex).get(0);
            } catch (LexerException e) {
//TODO: maybe some better way of handling user-input errors
                throw new IllegalArgumentException("Illegal argument");
            }
	        
	        return instruction;    

	    }
	        
	
	}
	
	
	protected void boxStretchingFinished(Point boxOrigin, Point boxTarget) {
		int height = Math.abs(boxOrigin.y - boxTarget.y);
		
		System.out.println("adding new instructions");
		
		for(int curY = 0; curY <= height; curY++)
    		blockUI.getCurrentStackMachine().addInstruction(boxOrigin.y + curY, newInstruction());
 	}
	
}
