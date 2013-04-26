package ox.stackgame.stackmachine;

import ox.stackgame.stackmachine.instructions.*;

import java.util.*;
import java.util.regex.*;

public class Lexer {
	
	public static class LexerException extends Exception{
		public final int lineNumber;
		public LexerException(int lineNumber, String message){
			super(message);
			this.lineNumber=lineNumber;
		}
	}
	
    public static ArrayList< Instruction > lex( String source ) throws LexerException {
        ArrayList< Instruction > program = new ArrayList< Instruction >();

        Pattern linesPattern = Pattern.compile( "[^\n]+" );
        Pattern commentPattern = Pattern.compile( "\".*$" );
        Pattern trimlPattern = Pattern.compile( "^\\s*" );
        Pattern trimrPattern = Pattern.compile( "\\s*$" );
        Pattern wordsPattern = Pattern.compile( "[^\\s]+" );

        Matcher lines = linesPattern.matcher( source );

        for(int lineno =0; lines.find(); lineno++ ) {
            String line = source.substring( lines.start(), lines.end() );

            Matcher comment = commentPattern.matcher( line );
            String commentless = comment.replaceFirst( "" );

            Matcher triml = trimlPattern.matcher( commentless );
            Matcher trimr = trimrPattern.matcher( triml.replaceFirst( "" ) );
            String trimmed = trimr.replaceFirst( "" );

            if( trimmed == "" ) {
                continue;
            }

            Matcher words = wordsPattern.matcher( trimmed );

            String name = null;
            Operation op = null;
            StackValue< ? > arg = null;

            for( int i = 0; words.find(); i++ ) {
                String word = trimmed.substring( words.start(), words.end() );

                // first word on the line is the name of an operation
                if( i == 0 ) {
                    name = word;
                    op = Operations.get( name );

                    //assert op != null : name + " is bad opname";
                    if (op == null) throw new LexerException(lineno, name + " is bad opname");
                }
                // other words must be arguments (or invalid code)
                else {
                    List< Class< ? > > types = op.argTypes();

                    //assert types != null : name + " takes no args";
                    if (types == null) throw new LexerException(lineno, name + " takes no args");

                    for( Class< ? > type : types ) {
                        StackValue< ? > value = null;

                        try {
                            value = ( StackValue< ? > ) type.newInstance();
                        }
                        catch( InstantiationException e ) {
                            //assert false : type.getName() + " needs a 0 argument constructor";
                        	throw new LexerException(lineno, type.getName() + " needs a 0 argument constructor");
                        }
                        catch( IllegalAccessException e ) {
                            throw new RuntimeException("type is not accessible"); // this shouldn't ever happen.
                        }

                        if( value.init( word ) ) {
                            arg = value;

                            break;
                        }
                    }

                    //assert arg != null : "couldn't find valid type for `" + word + "` in op " + name;
                	if(arg==null) throw new LexerException(lineno, "couldn't find valid type for `" + word + "` in op " + name);

                }
            }

            program.add( new Instruction( name, arg ) );
        }

        return program;
    }
}
