package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackValue;

public class Instruction {
    public final String name;
    public final StackValue<?> arg;

    public Instruction(String name) {
        this(name, null);
    }

    public Instruction(String name, StackValue<?> arg) {
        assert name != null : "name == null";
        assert Operations.get(name) != null : name + " isnt a real instruction";
        assert ( arg == null ) == ( Operations.get( name ).argTypes() == null ) : "wrong number of args for " + name;

        this.name = name;
        this.arg = arg;
    }
}
