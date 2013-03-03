package ox.stackgame.stackmachine;

import java.util.Stack;

import ox.stackgame.stackmachine.exceptions.*;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * Implementation of a simple stack machine
 * 
 * @author Greg
 */
public class StackMachine {
    public final static int STACK_SIZE = 200;
    public final static int MAX_INSTRUCTIONS = 10000;
    public final static int STORE_SIZE = 4;

    private final StackProgram program; 
    private int programCounter; 		
    private final EvaluationStack stack; 
    private final StackValue<?>[] store; 		
    private int numInstructions;		

    /**
     * Class constructor
     * 
     * @param program
     *            The program to be executed on this machine Changing the
     *            program after passing it to the machine produces undefined
     *            behaviour
     */
    public StackMachine(StackProgram program) {
	this.program = program;
	this.programCounter = 0;
	this.stack = new EvaluationStack();
	this.numInstructions = 0;
	this.store = new StackValue<?>[STORE_SIZE];

	if (program == null)
	    throw new IllegalArgumentException(
		    "Must provide a non-null program");
    }

    /**
     * 
     * @return Whether there is at least one more instruction for the machine to
     *         execute
     */
    public boolean isRunning() {
	return 0 <= programCounter
		&& programCounter < program.countInstructions();
    }
    
    public int getProgramCounter() {
	return programCounter;
    }

    /**
     * @return The evaluation stack of the machine
     */
    public EvaluationStack getStack() {
	return stack;
    }

    /**
     * Set a particular address in store
     * 
     * @param address 	The address to set
     * @param value 	The value to set the address to
     * @throws InvalidAddressException
     */
    public void setStore(int address, StackValue<?> value)
	    throws InvalidAddressException {
	if (0 <= address && address < STORE_SIZE)
	    store[address] = value;
	else
	    throw new InvalidAddressException(address, programCounter);
    }

    /**
     * Gets the value stored in a particular value in store
     * @param address	The address to get
     * @return		The value
     * @throws InvalidAddressException
     */
    public StackValue<?> getStore(int address) throws InvalidAddressException {
	if (0 <= address && address < STORE_SIZE)
	    return store[address];
	else
	    throw new InvalidAddressException(address, programCounter);
    }

    /**
     * Get the location of the next instruction in the program
     * @return
     */
    public int nextInstruction() {
	return programCounter + 1;
    }

    /**
     * Get the location in the program of a label
     * @param identifier	The identifier of the label
     * @return			The labels location
     * @throws NoSuchLabelException
     */
    public int getLabelLine(String identifier) throws NoSuchLabelException {
	int line = program.getLabelPosition(identifier);
	if (line > -1)
	    return line;
	else
	    throw new NoSuchLabelException(identifier, programCounter);
    }

    /**
     * Execute the next instruction in the program, and ready the 
     * programCounter, etc. to run the next instruction after that
     * @throws StackRuntimeException
     */
    public void step() throws StackRuntimeException {
	if (isRunning()) {
	    Instruction nextInstruction = program.instructionAt(programCounter);
	    programCounter = nextInstruction.execute(this);
	    numInstructions++;
	}
    }

    /**
     * Keep executing instructions until the end of the program, or until
     * more than MAX_INTRUCTIONS have been executed (and then exit with error)
     * @throws StackRuntimeException
     * @throws NotHaltingException
     */
    public void runAll() throws StackRuntimeException, NotHaltingException {
	while (isRunning() && numInstructions <= MAX_INSTRUCTIONS)
	    step();

	if (isRunning())
	    throw new NotHaltingException(numInstructions);
    }

    /**
     * A wrapper for a Stack<StackValue<?>> which lets us throw
     * StackRuntimeExceptions, rather than the normal Java exception
     * @author Greg
     */
    public class EvaluationStack {
	private final Stack<StackValue<?>> internalStack;
	
	public EvaluationStack() {
	    internalStack = new Stack<StackValue<?>>();
	}

	public StackValue<?> peek() throws EmptyStackException {
	    if (!internalStack.isEmpty())
		return internalStack.peek();
	    else
		throw new EmptyStackException(programCounter);
	}

	public StackValue<?> pop() throws EmptyStackException {
	    StackValue<?> val = this.peek();
	    internalStack.pop();
	    return val;
	}
	
	public void push(StackValue<?> val) throws FullStackException {
	    if (internalStack.size() < STACK_SIZE)
		internalStack.push(val);
	    else
		throw new FullStackException(programCounter);
	}
	
	public void clear() {
	    internalStack.clear();
	}
    }
}
