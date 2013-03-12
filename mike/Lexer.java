import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
	public static Program lex( String program ) {
		ArrayList< Instruction > ops = new ArrayList< Instruction >();
		ArrayList< Value > args = new ArrayList< Value >();

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

			for( int i = 0; words.find(); i++ ) {
				String word = trimmed.substring( words.start(), words.end() );

				if( i == 0 ) {
					ops.add( Instruction.valueOf( word.toUpperCase() ) );
				}
				else {
					args.add( GoodLanguage.ivalueOf( word ) );
				}
			}
		}

		return new Program( ops, args );
	}
}
