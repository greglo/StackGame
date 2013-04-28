/**
 * 
 */
package ox.stackgame.ui;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.*;

import ox.stackgame.stackmachine.*;
import ox.stackgame.stackmachine.Lexer.LexerException;
import ox.stackgame.stackmachine.exceptions.StackRuntimeException;
import ox.stackgame.stackmachine.instructions.*;

/**
 * Allows the user to input text while in Challenge/FreeDesignMode. Also
 * displays control buttons. When user presses 'Play', the plaintext is fed
 * through a lexer and updates the StackMachine. The frame is not editable in
 * RunMode, but displays the current program counter.
 * 
 * @author danfox
 * 
 */
public class ProgramTextUI extends JLayeredPane {
    private Mode oldMode = null;
    private Highlighter highlighter, redHighlighter;
    private JTextArea jta;
    private boolean dirtyText = true;
    public static Color editableTextColor = new Color(186, 96, 96);
    public static Color frozenTextColor = new Color(222, 147, 95);
    public static Font f = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    public static int width = 450;

    // code to be executed when a mode is activated.
    private ModeVisitor modeActivationVisitor = new ModeVisitor() {
        public void visit(RunMode m) {

        }

        public void visit(ChallengeMode m) {
            jta.setEditable(true);
            jta.setForeground(editableTextColor);
        }

        public void visit(FreeDesignMode m) {
            jta.setEditable(true);
            jta.setForeground(editableTextColor);
        }
    };

    // code to be executed when a mode is deactivated
    private ModeVisitor modeDeactivationVisitor = new ModeVisitor() {
        public void visit(RunMode m) {
            highlighter.removeAllHighlights();
        }

        public void visit(ChallengeMode m) {
            jta.setEditable(false);
            jta.setForeground(frozenTextColor);
        }

        public void visit(FreeDesignMode m) {
            jta.setEditable(false);
            jta.setForeground(frozenTextColor);
        }
    };

    private StackMachineListener l = new StackMachineListenerAdapter() {
        public void programCounterChanged(int line) {
            highlight(line -1);
        }

        public void programChanged(List<Instruction> p) {
            jta.setText(antiLex(p));
        }
    };

    private void highlight(int line) {
        try {
            highlighter.removeAllHighlights();
            highlighter.addHighlight(jta.getLineStartOffset(line), jta
                    .getLineEndOffset(line),
                    new DefaultHighlighter.DefaultHighlightPainter(new Color(
                            129, 162, 190)));
        } catch (BadLocationException e) {
            throw new RuntimeException("pc shouldn't be out of bounds");
        }
    }
    
    public boolean isTextDirty() { return dirtyText; }
    
    private void redHighlight(int line) {
        try {
            highlighter.removeAllHighlights();
            highlighter.addHighlight(jta.getLineStartOffset(line), jta
                    .getLineEndOffset(line),
                    new DefaultHighlighter.DefaultHighlightPainter(Color.red));
        } catch (BadLocationException e) {
            throw new RuntimeException("pc shouldn't be out of bounds");
        }
    }

    private JScrollPane createScrollPane() {
        // create a scroll pane
        JScrollPane jsp = new JScrollPane();
        jsp.setBounds(0, 0, this.width, ApplicationFrame.h);
        jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
        // jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // create an editable textarea
        jta = new JTextArea();
        jta.setBackground(ApplicationFrame.caBlue);
        jta.setForeground(editableTextColor);
        jta.setMargin(new Insets(20, 20, 100, 20)); // compensates for the
                                                    // height of the stackUI
        jta.setFont(f);
        jta.setCaretColor(new Color(150, 150, 150));
        highlighter = jta.getHighlighter();
        
        

        // create textarea to display linenumbers
        final JTextArea lines = new JTextArea("1");
        lines.setMargin(new Insets(20, 20, 100, 0));
        lines.setForeground(new Color(150, 150, 150));
        lines.setBackground(ApplicationFrame.caBlue);
        lines.setFont(f);
        lines.setEditable(false);

        // listen for changes in jta and update linenumbers
        jta.getDocument().addDocumentListener(new DocumentListener() {
            public String getLinesText() {
                int caretPosition = jta.getDocument().getLength();
                Element root = jta.getDocument().getDefaultRootElement();
                String text = "1" + System.getProperty("line.separator");
                for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text += i + System.getProperty("line.separator");
                }
                return text;
            }
            
            private void textChanged() {
                highlighter.removeAllHighlights();
                dirtyText = true;
            }

            public void changedUpdate(DocumentEvent de) {
                textChanged();
                lines.setText(getLinesText());
            }

            public void insertUpdate(DocumentEvent de) {
                textChanged();
                lines.setText(getLinesText());
            }

            public void removeUpdate(DocumentEvent de) {
                textChanged();
                lines.setText(getLinesText());
            }
        });

        // place the textarea in the scrollable window
        jsp.getViewport().add(jta);
        jsp.setRowHeaderView(lines); // keeps the line numbers in sync.

        return jsp;
    }
    
    public List<Instruction> getProgram() {
        return lex(jta.getText());
    }

    private List<Instruction> lex(String text) {
        ArrayList<Instruction> p = null;
        try {
            p = Lexer.lex(text);
            dirtyText = false;
        } catch (LexerException e) {
            // TODO: decide the best way to handle lexer exceptions
            redHighlight(e.lineNumber);
            System.err.println("Lexer error on line " + e.lineNumber + ": "
                    + e.getMessage());
        }
        return p;
    }

    private String antiLex(List<Instruction> program) {
        StringBuilder b = new StringBuilder();
        for (Instruction i : program) {
            if (b.length() != 0)
                b.append("\n");
            b.append(i.name);
            if (i.arg != null)
                b.append(" " + i.arg.getValue());
        }
        return b.toString();
    }

    public ProgramTextUI(final StateManager modeManager, final RunMode runMode) {
        super();

        // appearance
        this.setSize(new Dimension(width, ApplicationFrame.h));

        // pay attention to mode changes
        modeManager.registerModeActivationVisitor(modeActivationVisitor);
        modeManager.registerModeDeactivationVisitor(modeDeactivationVisitor);

        // listen to the stack machine
        modeManager.stackMachine.addListener(l);

        // add scrollpane
        this.add(createScrollPane(), new Integer(0)); // fills container
        jta.setText(antiLex(modeManager.stackMachine.getInstructions()));
    }
}
