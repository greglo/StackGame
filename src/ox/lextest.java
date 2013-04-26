package ox;

import java.util.*;

import ox.stackgame.stackmachine.*;
import ox.stackgame.stackmachine.instructions.*;
import ox.stackgame.stackmachine.exceptions.*;

public class lextest {
    private static final String prog = ""
        + "jump lab\n"
        + "const 5\n"
        + "label lab\n"
        + "const 7\n"
        + "const 100\n"
        + "add\n"
        + "dump\n"
        + "nop\n";

    public static void main( String[] args ) {
        List< Instruction > ops = Lexer.lex( prog );

        // for( Instruction op : ops ) {
        //     System.out.println( op.name + " " + ( op.arg == null ? "" : op.arg.getValue().toString() ) );
        // }

        StackMachine m = new StackMachine( new StackProgram( ops ) );

        try {
            m.step();
            m.step();
            m.step();
            m.step();
            m.step();
            m.step();
            m.step();
            m.step();
            m.step();
        }
        catch( StackRuntimeException e ) {
            System.out.println( "it all blew up: " + e.toString() );
        }
    }
}
