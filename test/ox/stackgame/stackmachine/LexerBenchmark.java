package ox.stackgame.stackmachine;

import org.junit.Test;

import ox.stackgame.stackmachine.Lexer.LexerException;

public class LexerBenchmark {
    private String program;

    public LexerBenchmark() {
        StringBuilder sb = new StringBuilder();

        for( int i = 0; i < 10000; i++ ) {
            sb.append( "label " + i + "\n" );
        }

        program = sb.toString();
    }

    @Test
    public void bench() throws LexerException {
        Lexer.lex( program );
    }
}
