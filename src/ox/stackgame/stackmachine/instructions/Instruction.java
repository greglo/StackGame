package ox.stackgame.stackmachine.instructions;

import java.util.*;

import ox.stackgame.stackmachine.StackValue;

public class Instruction {
    public final String name;
    public final List<StackValue<?>> args;

    public Instruction(String name) {
        this(name, new ArrayList<StackValue<?>>());
    }

    public Instruction(String name, StackValue<?>... args) {
        List<StackValue<?>> argsList = new ArrayList<StackValue<?>>();
        for (StackValue<?> value : args)
            argsList.add(value);

        assert name != null : "name == null";
        assert Operations.get(name) != null : name + " isnt a real instruction";
        assert argsList != null : "args == null";

        this.name = name;
        this.args = argsList;
    }

    public Instruction(String name, List<StackValue<?>> args) {
        assert name != null : "name == null";
        assert Operations.get(name) != null : name + " isnt a real instruction";
        assert args != null : "args == null";

        this.name = name;
        this.args = args;
    }
}
