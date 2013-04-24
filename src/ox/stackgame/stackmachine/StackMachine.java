package ox.stackgame.stackmachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ox.stackgame.stackmachine.exceptions.*;
import ox.stackgame.stackmachine.instructions.*;

/**
 * Implementation of a simple stack machine
 * TODO For subroutines, read the wikipedia page on Forth
 * 
 * @author Greg
 */
public class StackMachine {
    public final static int STACK_SIZE = 200;
    public final static int MAX_INSTRUCTIONS = 10000;
    public final static int STORE_SIZE = 4;

    private List<Instruction> instructions; 
    private StackProgram program;
    private int programCounter; 		
    private EvaluationStack stack; 
    private StackValue<?>[] store; 		
    private int numInstructions;	
    
    private final List<StackValue<?>> originalInput;
    private List<StackValue<?>> input;
    private int inputIndex;
    private List<StackValue<?>> output;
    
    private final List<StackMachineListener> listeners;

    public StackMachine(StackProgram program) {
	this(program, new ArrayList<StackValue<?>>());
    }
    
    public StackMachine(List<Instruction> instructions, List<StackValue<?>> input) {
	this.originalInput = input;
	loadInstructions(instructions);
	this.listeners = new ArrayList<StackMachineListener>();
    }
    
    /**
     * Class constructor
     * 
     * @param program
     *            The program to be executed on this machine Changing the
     *            program after passing it to the machine produces undefined
     *            behaviour
     */
    public StackMachine(StackProgram program, List<StackValue<?>> input) {
	this.originalInput = input;
	loadInstructions(program.getInstructions());

	this.listeners = new ArrayList<StackMachineListener>();
    }
    
    /**
     * Resets the stack machine to the state it was in before it started running the 
     * stack program
     */
    public void reset() {
	this.programCounter 	= 0;
	this.stack 		= new EvaluationStack();
	this.numInstructions 	= 0;
	this.store 		= new StackValue<?>[STORE_SIZE];
	this.input 		= originalInput;
	this.inputIndex 	= 0;
	this.output 		= new ArrayList<StackValue<?>>();
    }
    
    
    public void loadInstructions(List<Instruction> instructions) {
	this.instructions = instructions;
	this.program = new StackProgram(instructions);
	reset();
    }
    
    
    public void addInstruction(int line, Instruction instruction) {
	if (line < 0)
	    line = 0;
	if (line > instructions.size())
	    line = instructions.size();
	instructions.add(line, instruction);
	loadInstructions(instructions);
    }
    
    public void removeInstruction(int line) {
	if (line < 0)
	    line = 0;
	if (line > instructions.size())
	    line = instructions.size();
	instructions.remove(line);
	loadInstructions(instructions);
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
	for (StackMachineListener l : listeners)
	    l.programCounterChanged(programCounter);
    }
    
    /**
     * Get the current instruction being pointed to by programCounter
     * @return index of the instruction that will be executed next time step() is called
     */
    public int getProgramCounter() {
	return programCounter;
    }
    
    /**
     * Consume a word of input (presumably to push on to the evaluation stack)
     * @return	The word of input
     */
    public StackValue<?> consumeInput() {
	if (inputIndex < input.size()) {
	    for (StackMachineListener l : listeners)
		l.inputConsumed(inputIndex);
	    return input.get(inputIndex++);
	}
	else
	    return null;
	// TODO
    }
    
    /**
     * Append a value to the output buffer
     * @param value
     */
    public void output(StackValue<?> value) {
	output.add(value);
	for (StackMachineListener l : listeners)
	    l.outputChanged();
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
	    for (StackMachineListener l : listeners)
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
	    Operation op = Operations.get( nextInstruction.name );
	    setProgramCounter( op.execute( this, nextInstruction.arg ) );
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
    public void addListener(StackMachineListener l) {
	listeners.add(l);
    }
    
    /**
     * Remove a listener from the stack machine
     * @param l
     */
    public void removeListener(StackMachineListener l) {
	listeners.remove(l);
    }
    
    /**
     * Should only be used by views.  (Not for execution).
     * @return the instructions in this StackMachine.
     */
    public List<Instruction> getInstructions(){
    	return this.instructions;
    }

    /**
     * A wrapper for a Stack< StackValue< ?>> which lets us throw
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
	
	public boolean equals(Object other){
		return false;
	}
	
	public boolean equals(EvaluationStack other){
		return internalStack.equals(other.internalStack);
	}
    }
}
