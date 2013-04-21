package ox.stackgame.stackmachine;

import java.util.List;

import ox.stackgame.stackmachine.instructions.Instruction;

public interface StackMachineListener {
    public void stackInstructionsChanged(List<Instruction> instructions);
    public void programCounterChanged(int line);
    public void storeChanged(int address);
    public void inputConsumed(int startIndex);
    public void outputChanged();
}
