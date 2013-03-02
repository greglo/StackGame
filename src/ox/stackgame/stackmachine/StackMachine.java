package ox.stackgame.stackmachine;

import java.util.Stack;

import ox.stackgame.stackmachine.exceptions.*;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * Implementation of a simple stack machine
 * @author Greg
 */
public class StackMachine {
    public final static int STACK_SIZE = 200;
    public final static int MAX_INSTRUCTIONS = 10000;	// Maximum number of executed instructions before termination
    public final static int STORE_SIZE = 4;
    
    private final StackProgram 		program;	// 
    private int 			programCounter;	// Line number of next instruction to be executed
    private final Stack<StackValue<?>>	stack;		// 
    private final StackValue<?>[]	store;		// 
    private int 			numInstructions;// Number of instructions run
   
    
    /**
     * Class constructor
     * @param program
     * 		The program to be executed on this machine
     * 		Changing the program after passing it to the machine produces undefined behaviour
     */
    public StackMachine(StackProgram program) {
	this.program = program;
	this.programCounter = 0;
	this.stack = new Stack<StackValue<?>>();
	this.numInstructions = 0;
	this.store = new StackValue<?>[STORE_SIZE];
	
	if (program == null)
	    throw new IllegalArgumentException("Must provide a non-null program");
    }
    
    /**
     * 
     * @return Whether there is at least one more instruction for the machine to execute
     */
    public boolean isRunning() {
	return 0 <= programCounter && programCounter < program.countInstructions();
    }
    
    /**
     * @return The evaluation stack of the machine
     */
    public Stack<StackValue<?>> getStack() {
	return stack;
    }
    
    /**
     * Set a particular address in store
     * @param address	The address to set
     * @param value	The value to set the address to
     * @throws InvalidAddressException 
     */
    public void setStore(int address, StackValue<?> value) throws InvalidAddressException {
	if (0 <= address && address < STORE_SIZE)
	    store[address] = value;
	else
	    throw new InvalidAddressException(address, programCounter);
    }
    
    public StackValue<?> getStore(int address) throws InvalidAddressException {
	if (0 <= address && address < STORE_SIZE)
	    return store[address];
	else
	    throw new InvalidAddressException(address, programCounter);
    }
    
    public int nextInstruction() {
	return programCounter + 1;
    }
    
    public int getLabelLine(String identifier) throws NoSuchLabelException {
	int line = program.getLabelPosition(identifier);
	if (line > -1)
	    return line;
	else
	    throw new NoSuchLabelException(identifier, programCounter);
    }
    
    public void step() throws StackRuntimeException {
	if (isRunning()) {
	    Instruction nextInstruction = program.instructionAt(programCounter);
	    programCounter = nextInstruction.execute(this);
	    numInstructions++;
	}
    }
    
    public void runAll() throws StackRuntimeException, NotHaltingException {
	while (isRunning() && numInstructions <= MAX_INSTRUCTIONS)
	    step();
	
	if (isRunning())
	    throw new NotHaltingException(numInstructions);
    }
}
