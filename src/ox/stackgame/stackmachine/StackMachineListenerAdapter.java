/**
 * 
 */
package ox.stackgame.stackmachine;

import java.util.Iterator;
import java.util.List;

import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * Blank listener to make overriding one listener method easier.
 * @author danfox
 *
 */
public class StackMachineListenerAdapter implements StackMachineListener {
    public void programCounterChanged(int line, Instruction instruction) { }

    public void storeChanged(int address) { }

    public void inputConsumed(int startIndex) { }

    public void outputChanged(Iterator<StackValue<?>> output) { }

    public void programChanged(List<Instruction> instructions) { }

    public void stackChanged(EvaluationStack stack) { }

    public void machineReset() { }
}
