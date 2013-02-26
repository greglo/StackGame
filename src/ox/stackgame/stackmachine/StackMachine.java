package ox.stackgame.stackmachine;

import java.util.Stack;

import ox.stackgame.stackmachine.instructions.Instruction;

public class StackMachine<E> {
    public final static int STACK_SIZE = 200;
    public final static int MAX_INSTRUCTIONS = 10000;
    public final static int STORE_SIZE = 4;
    
    private final StackProgram<E> program;
    private int 		programCounter;
    private final Stack<E>	stack;
    private final E[]		store;
    private int 		numInstructions;
    
    public StackMachine(StackProgram<E> program) {
	this.program = program;
	this.programCounter = 0;
	this.stack = new Stack<E>();
	this.numInstructions = 0;
	this.store = (E[])(new Object[STORE_SIZE]);
	
	if (program == null)
	    throw new IllegalArgumentException("Must provide a non-null program");
    }
    
    
    public boolean isRunnable() {
	return programCounter < program.countInstructions();
    }
    
    public Stack<E> getStack() {
	return stack;
    }
    
    public void incrProgramCounter() {
	programCounter++;
    }
    
    public void setProgramCounter(int programCounter) {
	assert(programCounter >= 0);
	this.programCounter = programCounter;
    }
    
    public void jumpToLabel(String identifier) {
	int line = program.getLabelPosition(identifier);
	if (line > -1)
	    setProgramCounter(line);
	else
	    incrProgramCounter();
	    //throw new NoSuchLabelException();
    }
    
    public void step() {
	if (isRunnable()) {
	    Instruction<E> nextInstruction = program.instructionAt(programCounter);
	    nextInstruction.execute(this);
	    numInstructions++;
	}
    }
    
    public void runAll() throws NotHaltingException {
	while (isRunnable() && numInstructions <= MAX_INSTRUCTIONS)
	    step();
	
	if (isRunnable())
	    throw new NotHaltingException(numInstructions);
    }
}
