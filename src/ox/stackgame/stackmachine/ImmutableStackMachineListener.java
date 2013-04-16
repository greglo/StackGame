package ox.stackgame.stackmachine;

public interface ImmutableStackMachineListener {
    public void programCounterChanged(int line);
    public void storeChanged(int address);
    public void inputConsumed(int startIndex);
    public void outputChanged();
}
