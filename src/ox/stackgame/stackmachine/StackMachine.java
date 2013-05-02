package ox.stackgame.stackmachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ox.stackgame.stackmachine.exceptions.*;
import ox.stackgame.stackmachine.instructions.*;

import ox.stackgame.ui.VT102;

/**
 * @author Greg
 */
public class StackMachine {
    public final static int STACK_SIZE = 200;
    public final static int MAX_INSTRUCTIONS = 10000;
    public final static int STORE_SIZE = 4;

    private int programCounter;
    private EvaluationStack stack;
    private StackValue<?>[] store;
    private List<Instruction> instructions;
    private final Map<String, Integer> labels;

    // Track the number of instructions being run on the machine
    private int numInstructions;

    private List<StackValue<?>> input;
    private int inputIndex;
    private List<StackValue<?>> output;

    private final List<StackMachineListener> listeners;

    /**
     * Create a new StackMachine with nothing on the input tape
     * 
     * @param instructions
     *            The program loaded into the machine
     */
    public StackMachine(List<Instruction> instructions) {
        this(instructions, new ArrayList<StackValue<?>>());
    }

    /**
     * Create a new StackMachine
     * 
     * @param instructions
     *            The program loaded into the machine
     * @param input
     *            The values on the input tape
     */
    public StackMachine(List<Instruction> instructions, List<StackValue<?>> input) {
        this.listeners = new ArrayList<StackMachineListener>();
        this.labels = new HashMap<String, Integer>();
        this.input = input;
        this.stack = new EvaluationStack();
        loadInstructions(instructions);
    }

    /**
     * Resets the stack machine to the state it was in before it started running
     * the stack program
     */
    public void reset() {
        this.programCounter = 0;
        this.stack.clear();
        this.numInstructions = 0;
        this.store = new StackValue<?>[STORE_SIZE];
        this.inputIndex = 0;
        this.output = new ArrayList<StackValue<?>>();
        for (int i = 0; i < STORE_SIZE; i++) {
            store[i] = new IntStackValue(0);
            for (StackMachineListener l : listeners)
                l.storeChanged(i);
        }
    }

    /**
     * Load a new program into the machine and reset it
     * 
     * @param instructions
     *            The program to load into the machine
     */
    public void loadInstructions(List<Instruction> instructions) {
        this.instructions = instructions;

        if (instructions != null) {
            int i = 0;
            for (Instruction op : instructions) {
                if (op.name.equals("label"))
                    labels.put((String) op.arg.getValue(), i);
                i++;
            }
        } else
            instructions = new ArrayList<Instruction>();

        for (StackMachineListener l : listeners)
            l.programChanged(instructions);
        reset();
    }

    /**
     * Add an instruction into the currently loaded program, and reset
     * 
     * @param line
     *            Line to insert the instruction at
     * @param instruction
     *            Instruction to insert
     */
    public void addInstruction(int line, Instruction instruction) {
        if (line < 0)
            line = 0;
        if (line > instructions.size())
            line = instructions.size();
        instructions.add(line, instruction);
        loadInstructions(instructions);
    }

    /**
     * Remove an instruction from the currently loaded program, and reset
     * 
     * @param line
     *            Line to remove the instruction at
     */
    public void removeInstruction(int line) {
        if (line < 0)
            line = 0;
        if (line > instructions.size())
            line = instructions.size();
        instructions.remove(line);
        loadInstructions(instructions);
    }

    /**
     * Gets the value stored in a particular value in store
     * 
     * @param address
     *            The address to get
     * @return The value
     * @throws InvalidAddressException
     */
    public StackValue<?> getStore(int address) throws InvalidAddressException {
        if (0 <= address && address < STORE_SIZE)
            return store[address];
        else
            throw new InvalidAddressException(address, programCounter);
    }

    /**
     * Set a particular address in store
     * 
     * @param address
     *            The address to set
     * @param value
     *            The value to set the address to
     * @throws InvalidAddressException
     */
    public void setStore(int address, StackValue<?> value) throws InvalidAddressException {
        if (0 <= address && address < STORE_SIZE) {
            store[address] = value;
            for (StackMachineListener l : listeners)
                l.storeChanged(address);
        } else
            throw new InvalidAddressException(address, programCounter);
    }

    public boolean hasInput() {
        return inputIndex < input.size();
    }

    /**
     * Consume the first work on the input tape
     * 
     * @return The word of input
     * @throws EmptyInputException
     */
    public StackValue<?> consumeInput() throws EmptyInputException {
        if (hasInput()) {
            for (StackMachineListener l : listeners)
                l.inputConsumed(inputIndex);
            return input.get(inputIndex++);
        } else
            throw new EmptyInputException(programCounter);
    }

    /**
     * Add a value to the end of the output tape
     * 
     * @param value
     *            The value to add to the output tape
     */
    public void addOutput(StackValue<?> value) {
        output.add(value);
        for (StackMachineListener l : listeners)
            l.outputChanged();
    }

    /**
     * @return Whether there is at least one more instruction for the machine to
     *         execute
     */
    public boolean isRunning() {
        return 0 <= programCounter && programCounter < instructions.size();
    }

    /**
     * Get the current instruction being pointed to by programCounter
     * 
     * @return index of the instruction that will be executed next time step()
     *         is called
     */
    public int getProgramCounter() {
        return programCounter;
    }

    /**
     * Sets the program counter to the next instruction to be executed
     * 
     * @param programCounter
     *            The line of the next instruction
     */
    private void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
        for (StackMachineListener l : listeners)
            l.programCounterChanged(programCounter);
    }

    /**
     * Get the location of the next instruction in the program
     * 
     * @return
     */
    public int nextInstruction() {
        return getProgramCounter() + 1;
    }

    /**
     * @return The evaluation stack of the machine
     */
    public EvaluationStack getStack() {
        return stack;
    }

    /**
     * Get the location in the program of a label
     * 
     * @param identifier
     *            The identifier of the label
     * @return The labels location
     * @throws NoSuchLabelException
     */
    public int getLabelLine(String identifier) throws NoSuchLabelException {
        int line = getLabelPosition(identifier);
        if (line > -1)
            return line;
        else
            throw new NoSuchLabelException(identifier, programCounter);
    }

    /**
     * Get the line number of a label
     * 
     * @param identifier
     *            The identifier of the label
     * @return The labels line number (-1 if it does not exist)
     */
    private int getLabelPosition(String identifier) {
        if (labels.containsKey(identifier))
            return labels.get(identifier);
        else
            return -1;
    }

    /**
     * Execute the next instruction in the program, and ready the
     * programCounter, etc. to run the next instruction after that
     * 
     * @throws StackRuntimeException
     */
    public void step() throws StackRuntimeException {
        if (isRunning()) {
            Instruction nextInstruction = instructions.get(programCounter);
            Operation op = Operations.get(nextInstruction.name);
            if (op != null)
                setProgramCounter(op.execute(this, nextInstruction.arg));
            else
                throw new RuntimeException("Invalid instruction passed to StackMachine");
            numInstructions++;
        }
    }

    /**
     * Keep executing instructions until the end of the program, or until more
     * than MAX_INTRUCTIONS have been executed (and then exit with error)
     * 
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
     * 
     * @param l
     */
    public void addListener(StackMachineListener l) {
        listeners.add(l);
    }

    /**
     * Remove a listener from the stack machine
     * 
     * @param l
     */
    public void removeListener(StackMachineListener l) {
        listeners.remove(l);
    }

    /**
     * Should only be used by views. (Not for execution).
     * 
     * @return the instructions in this StackMachine.
     */
    public List<Instruction> getInstructions() {
        return this.instructions;
    }

    /**
     * A wrapper for a Stack<StackValue<?>> which lets us throw
     * StackRuntimeExceptions, rather than the normal Java exception
     * 
     * @author Greg
     */
    public class EvaluationStack implements Iterable<StackValue<?>> {
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
            notifyListeners();
            return val;
        }

        public void push(StackValue<?> val) throws FullStackException {
            if (internalStack.size() < STACK_SIZE)
                internalStack.push(val);
            else
                throw new FullStackException(programCounter);

            notifyListeners();
        }

        public int size() {
            return internalStack.size();
        }

        public void clear() {
            internalStack.clear();
            notifyListeners();
        }

        public boolean equals(Object other) {
            return false;
        }

        public boolean equals(EvaluationStack other) {
            return internalStack.equals(other.internalStack);
        }

        private void notifyListeners() {
            for (StackMachineListener l : listeners)
                l.stackChanged(this);
        }

        public Iterator<StackValue<?>> iterator() {
            return internalStack.iterator();
        }
    }

    public void dump() {
        System.out.println("=== PROGRAM ===");

        for (int i = 0; i < instructions.size(); i++) {
            Instruction op = instructions.get(i);

            System.out.print("  " + VT102.e());
            System.out.print(i == programCounter ? "[1m> " : "[0m  ");
            System.out.print(op.toString());
            System.out.print("  " + VT102.e());

            System.out.println(VT102.e() + "[0m");
        }

        System.out.println();
        System.out.println("=== STACK ===");
        for (int i = 0; i < stack.size(); i++)
            System.out.println("  " + stack.internalStack.get(i).getValue().toString());

        System.out.println();
        System.out.println("=== STORE ===");
        for (int i = 0; i < STORE_SIZE; i++)
            System.out.println("  " + i + ": " + (store[i] == null ? "null" : store[i].getValue().toString()));

        System.out.println();
        System.out.println("=== INPUT ===");
        for (StackValue<?> val : input)
            System.out.print(val.toString() + " ");

        System.out.println();
        System.out.println("=== OUTPUT ===");
        for (StackValue<?> val : output)
            System.out.print(val.toString() + " ");

        System.out.println();
        System.out.println();
    }
}
