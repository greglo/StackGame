package ox.stackgame.stackmachine;

import java.util.Iterator;
import java.util.List;

import ox.stackgame.stackmachine.StackMachine.EvaluationStack;
import ox.stackgame.stackmachine.instructions.Instruction;

public interface StackMachineListener {
    public void programChanged(List<Instruction> instructions);
    public void programCounterChanged(int line);
    public void stackChanged(EvaluationStack stack);
    public void storeChanged(int address);
    public void inputConsumed(int startIndex);
    public void outputChanged(Iterator<StackValue<?>> outputs);
    public void machineReset();
}
