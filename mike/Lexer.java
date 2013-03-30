package mike;

import java.util.*;
import java.util.regex.*;

public class Lexer {
	public static Program lex( String program ) {
		ArrayList< String > ops = new ArrayList< String >();
		ArrayList< ArrayList< Value > > args = new ArrayList< ArrayList< Value > >();
		Hashtable< String, Integer > labels = new Hashtable< String, Integer >();

		Pattern linesPattern = Pattern.compile( "[^\n]+" );
		Pattern commentPattern = Pattern.compile( "\".*$" );
		Pattern trimlPattern = Pattern.compile( "^\\s*" );
		Pattern trimrPattern = Pattern.compile( "\\s*$" );
		Pattern wordsPattern = Pattern.compile( "[^\\s]+" );

		Matcher lines = linesPattern.matcher( program );

		while( lines.find() ) {
			String line = program.substring( lines.start(), lines.end() );

			Matcher comment = commentPattern.matcher( line );
			String commentless = comment.replaceFirst( "" );

			Matcher triml = trimlPattern.matcher( commentless );
			Matcher trimr = trimrPattern.matcher( triml.replaceFirst( "" ) );
			String trimmed = trimr.replaceFirst( "" );

			if( trimmed == "" ) {
				continue;
			}

			Matcher words = wordsPattern.matcher( trimmed );

			ArrayList< Value > params = new ArrayList< Value >();

			for( int i = 0; words.find(); i++ ) {
				String word = trimmed.substring( words.start(), words.end() );

				if( i == 0 ) {
					ops.add( word.toLowerCase() );
				}
				else {
					params.add( IValue.valueOf( word ) );
				}
			}

			args.add( params );
		}

		/* for( int i = 0; i < ops.size(); i++ ) { */
		/* 	if( ops.get( i ) == "label" ) { */
		/* 		labels.put( args.get( i ).get( 0 ).asString(); */
		/* 	} */
		/* } */

		return new Program( ops, args, labels );
	}
}
