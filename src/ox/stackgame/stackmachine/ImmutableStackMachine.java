package ox.stackgame.stackmachine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import ox.stackgame.stackmachine.exceptions.*;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * Implementation of a simple stack machine
 * TODO For subroutines, read the wikipedia page on Forth
 * 
 * @author Greg
 */
public class ImmutableStackMachine {
    public final static int STACK_SIZE = 200;
    public final static int MAX_INSTRUCTIONS = 10000;
    public final static int STORE_SIZE = 4;

    private final StackProgram program; 
    private int programCounter; 		
    private final EvaluationStack stack; 
    private final StackValue<?>[] store; 		
    private int numInstructions;	
    
    private final List<StackValue<?>> input;
    private int inputIndex;
    private final List<StackValue<?>> output;
    
    private final List<ImmutableStackMachineListener> listeners;

    public ImmutableStackMachine(StackProgram program) {
	this(program, new ArrayList<StackValue<?>>());
    }
    
    /**
     * Class constructor
     * 
     * @param program
     *            The program to be executed on this machine Changing the
     *            program after passing it to the machine produces undefined
     *            behaviour
     */
    public ImmutableStackMachine(StackProgram program, List<StackValue<?>> input) {
	this.program = program;
	this.programCounter = 0;
	this.stack = new EvaluationStack();
	this.numInstructions = 0;
	this.store = new StackValue<?>[STORE_SIZE];
	this.input = input;
	this.inputIndex = 0;
	this.output = new ArrayList<StackValue<?>>();
	
	this.listeners = new ArrayList<ImmutableStackMachineListener>();

	if (program == null)
	    throw new IllegalArgumentException(
		    "Must provide a non-null program");
    }
    
    /**
     * Resets the stack machine to the state it was in before it started running the 
     * stack program
     */
    public void reset() {
	programCounter = 0;
	stack.clear();
	numInstructions = 0;;
	inputIndex = 0;
	output.clear();
	for (int i = 0; i < STORE_SIZE; i++)
	    store[i] = null;
    }

    /**
     * @return Whether there is at least one more instruction for the machine to
     *         execute
     */
    public boolean isRunning() {
	return 0 <= programCounter
		&& programCounter < program.countInstructions();
    }
    
    /**
     * Sets the program counter to the next instruction to be executed
     * @param programCounter	The line of the next instruction
     */
    private void setProgramCounter(int programCounter) {
	this.programCounter = programCounter;
	for (ImmutableStackMachineListener l : listeners)
	    l.programCounterChanged(programCounter);
    }
    
    /**
     * Get the current instruction being pointed to by programCounter
     * @return
     */
    public int getProgramCounter() {
	return programCounter;
    }
    
    /**
     * Consume a word of input (presumably to push on to the evaluation stack)
     * @return	The word of input
     * @throws EmptyInputException 
     */
    public StackValue<?> consumeInput() throws EmptyInputException {
	if (inputIndex < input.size()) {
	    for (ImmutableStackMachineListener l : listeners)
		l.inputConsumed(inputIndex);
	    return input.get(inputIndex++);
	}
	else
	    throw new EmptyInputException(programCounter);
    }
    
    /**
     * Append a value to the output buffer
     * @param value
     */
    public void output(StackValue<?> value) {
	output.add(value);
	for (ImmutableStackMachineListener l : listeners)
	    l.outputChanged();
    }
    
    /**
     * 
     * @return
     */
    public Iterator<StackValue<?>> getOutput() {
	return output.iterator();
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
	if (0 <= address && address < STORE_SIZE) {
	    store[address] = value;
	    for (ImmutableStackMachineListener l : listeners)
		l.storeChanged(address);
	}
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
	    setProgramCounter(nextInstruction.execute(this));
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
     * Add a listener to the stack machine
     * @param l
     */
    public void addListener(ImmutableStackMachineListener l) {
	listeners.add(l);
    }
    
    /**
     * Remove a listener from the stack machine
     * @param l
     */
    public void removeListener(ImmutableStackMachineListener l) {
	listeners.remove(l);
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
	
	public int size() {
	    return internalStack.size();
	}
	
	public void clear() {
	    internalStack.clear();
	}
    }
}
