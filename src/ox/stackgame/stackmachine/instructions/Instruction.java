package ox.stackgame.stackmachine.instructions;

import ox.stackgame.stackmachine.StackValue;

public class Instruction {
    public final String name;
    public final StackValue<?> arg;

    public Instruction(String name) {
        this(name, null);
    }

    public Instruction(String name, StackValue<?> arg) {
        if( name == null ) {
            throw new RuntimeException( "name == null" );
        }

        if( Operations.get( name ) == null ) {
            throw new RuntimeException( name + " isn't a real instruction" );
        }

        if( ( arg == null ) != ( Operations.get(name).argTypes() == null ) ) {
            throw new RuntimeException( "wrong number of args for " + name );
        }

        this.name = name;
        this.arg = arg;
    }

    public String toString() {
        String str = name.toLowerCase();
        if (arg != null)
            str += " " + arg.getValue().toString();
        return str;
    }
}
