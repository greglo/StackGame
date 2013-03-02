package ox.stackgame.stackmachine;


public abstract class StackValue<T> {
    public abstract T getValue();
    public abstract StackValue<?> add(StackValue<?> y);
    public abstract StackValue<?> sub(StackValue<?> y);
    public abstract StackValue<?> mul(StackValue<?> y);
    public abstract StackValue<?> div(StackValue<?> y);
}
