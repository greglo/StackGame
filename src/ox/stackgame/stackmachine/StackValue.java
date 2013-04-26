package ox.stackgame.stackmachine;

import ox.stackgame.stackmachine.exceptions.TypeException;


public abstract class StackValue<T> {
    public abstract boolean init( String str );

    /**
     * Get the value stored within the StackValue
     * @return
     */
    public abstract T getValue();
    
    public abstract StackValue<?> add(StackValue<?> y) throws TypeException;
    public abstract StackValue<?> sub(StackValue<?> y) throws TypeException;
    public abstract StackValue<?> mul(StackValue<?> y) throws TypeException;
    public abstract StackValue<?> div(StackValue<?> y) throws TypeException;
    
    public abstract boolean equals(Object other);
}
