package ox.stackgame.blockUI;

import java.awt.Point;

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
//TODO: deal with arguments
	    String str = blockUI.getBlockManager().getInstruction();
	    if(str == null)throw new IllegalArgumentException("No Instruction");
	    
	    String[] strs = str.split(" ");
        if(strs.length == 0)throw new IllegalArgumentException("No Instruction");
	    
	    if(strs.length == 1){
	        return new Instruction(strs[0]);
	    }else{
	        String toLex = strs[0];
	        if(strs[1] == "*"){
//TODO: prompt for argument
	            //toLex += " " + input;
	            
	        }else toLex = str;
	        Instruction instruction = null;
            try {
                instruction = Lexer.lex(toLex).get(0);
            } catch (LexerException e) {
//TODO: maybe some better way of handling user input
                throw new IllegalArgumentException("Bad Constant");
            }
	        
	        return instruction;    

	    }
	        
	
	}
	
	
//TODO: review this
	protected void boxStretchingFinished(Point boxOrigin, Point boxTarget) {
//		int x = Math.min(boxOrigin.x, boxTarget.x);
//		int y = Math.min(boxOrigin.y, boxTarget.y);
//		int width = Math.abs(boxOrigin.x - boxTarget.x);
		int height = Math.abs(boxOrigin.y - boxTarget.y);
		
		for(int curY = 0; curY <= height; curY++)
//			for(int curX = 0; curX <= width; curX++)
				blockUI.getCurrentStackMachine().addInstruction(curY, newInstruction());
	}
	
}
