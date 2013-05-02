package ox.stackgame.stackmachine;

import ox.stackgame.stackmachine.instructions.*;

import java.util.*;
import java.util.regex.*;

public class Lexer {

    @SuppressWarnings("serial")
    public static class LexerException extends Exception {
        public final int lineNumber;

        public LexerException(int lineNumber, String message) {
            super(message);
            this.lineNumber = lineNumber;
        }
    }

    public static ArrayList<Instruction> lex(String source)
            throws LexerException {
        ArrayList<Instruction> program = new ArrayList<Instruction>();

        Pattern linesPattern = Pattern.compile("[^\n]+");
        Pattern commentPattern = Pattern.compile("--.*$");
        Pattern trimlPattern = Pattern.compile("^\\s*");
        Pattern trimrPattern = Pattern.compile("\\s*$");
        Pattern wordsPattern = Pattern.compile("[^\\s]+");

        Matcher lines = linesPattern.matcher(source);

        for (int lineno = 0; lines.find(); lineno++) {
            String line = source.substring(lines.start(), lines.end());

            Matcher comment = commentPattern.matcher(line);
            String commentless = comment.replaceFirst("");

            Matcher triml = trimlPattern.matcher(commentless);
            Matcher trimr = trimrPattern.matcher(triml.replaceFirst(""));
            String trimmed = trimr.replaceFirst("");

            if (trimmed == "") {
                continue;
            }

            Matcher words = wordsPattern.matcher(trimmed);

            if (words.find()) {
                // Op provided
                String opName = trimmed.substring(words.start(), words.end());
                Operation op = Operations.get(opName);

                if (op == null)
                    throw new LexerException(lineno, opName
                            + " is not a valid Instruction");

                StackValue<?> arg = null;
                List<Class<?>> argTypes = op.argTypes();

                if (words.find()) {
                    // Argument provided
                    String argString = trimmed.substring(words.start(),
                            words.end());

                    if (argTypes == null)
                        throw new LexerException(lineno, opName + " doesn't take an argument: Unexpected '" + argString + "'");

                    for (Class<?> argType : argTypes) {
                        StackValue<?> value = null;

                        try {
                            value = (StackValue<?>) argType.newInstance();
                        } catch (InstantiationException e) {
                            throw new RuntimeException(argType.getName() + " needs a 0 argument constructor");
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("type is not accessible");
                        }

                        if (value.init(argString)) {
                            arg = value;
                            break;
                        }
                    }
                    if (arg == null)
                        throw new LexerException(lineno, "couldn't find valid type for `" + argString + "` in op " + opName);
                }
                else
                    throw new LexerException(lineno, "Argument expected for " + opName);

                if (words.find())
                    throw new LexerException(lineno, "Too many arguments were provided");

                program.add(new Instruction(opName, arg));
            }
        }

        return program;
    }
}
