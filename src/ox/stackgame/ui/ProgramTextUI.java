/**
 * 
 */
package ox.stackgame.ui;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.*;

import ox.stackgame.stackmachine.*;
import ox.stackgame.stackmachine.Lexer.LexerException;
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

@SuppressWarnings("serial")
public class ProgramTextUI extends JLayeredPane {
    public static Font font = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    public static Color editableTextColor = new Color(186, 96, 96);
    public static Color frozenTextColor = new Color(222, 147, 95);
    
    private final JTextArea jta = new JTextArea();
    public final Document document = jta.getDocument();
   
    private Highlighter highlighter;
    private boolean dirtyText = true;
    private final StateManager sm;
    private ErrorUI eui;

    private ModeVisitor modeActivationVisitor = new ModeVisitor() {
        public void visit(RunMode m) {  }

        public void visit(ChallengeMode m) {
            jta.setEditable(true);
            jta.setForeground(editableTextColor);
            ProgramTextUI.this.antiLex(sm.stackMachine.getInstructions());
        }

        public void visit(FreeDesignMode m) {
            jta.setEditable(true);
            jta.setForeground(editableTextColor);
            ProgramTextUI.this.antiLex(sm.stackMachine.getInstructions());
        }
    };

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
        @Override
        public void programChanged(List<Instruction> instructions){
            if (instructions.size()==0 && !eui.hasError()){ // program was cleared
                System.out.println("clearing text");
                jta.setText("");
                dirtyText=false;
                jta.requestFocus();
            }
        }
        
        @Override
        public void programCounterChanged(int line, Instruction instruction) {
            highlight(instruction.line == -1 ? line - 1 : instruction.line);
        }
    };
    
    /**
     * 
     * @param stateManager
     * @param runMode
     * @param eui               Output stream for errors
     */
    public ProgramTextUI(final StateManager stateManager, final RunMode runMode, ErrorUI eui) {
        super();

        this.eui = eui;
        this.sm = stateManager;

        stateManager.registerModeActivationVisitor(modeActivationVisitor);
        stateManager.registerModeDeactivationVisitor(modeDeactivationVisitor);
        stateManager.stackMachine.addListener(l);

        this.add(createScrollPane(), new Integer(0)); // fills container
        jta.setText(antiLex(stateManager.stackMachine.getInstructions()));
    }
    

    private void highlight(int line) {
        try {
            highlighter.removeAllHighlights();
            highlighter.addHighlight(jta.getLineStartOffset(line), jta.getLineEndOffset(line), new DefaultHighlighter.DefaultHighlightPainter(
                    new Color(129, 162, 190)));
        } catch (BadLocationException e) {
            throw new RuntimeException("pc shouldn't be out of bounds");
        }
    }

    public boolean isTextDirty() {
        return dirtyText;
    }

    private void redHighlight(int line, int start, int end) {
        try {
            highlighter.removeAllHighlights();
            highlighter.addHighlight(jta.getLineStartOffset(line) + start, jta.getLineStartOffset(line) + end,
                    new DefaultHighlighter.DefaultHighlightPainter(new Color(150, 30, 30)));
        } catch (BadLocationException e) {
            throw new RuntimeException("pc shouldn't be out of bounds");
        }
    }

    private JScrollPane createScrollPane() {
        // create a scroll pane
        JScrollPane jsp = new JScrollPane();
        jsp.setBounds(0, 0, ApplicationFrame.CENTER_PANEL_WIDTH, ApplicationFrame.PROGRAMTEXTUI_HEIGHT);
        jsp.setBorder(new EmptyBorder(5, 0, 5, 0));
        jsp.setBackground(ApplicationFrame.caBlue);
        // jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // create an editable textarea
        jta.setBackground(ApplicationFrame.caBlue);
        jta.setForeground(editableTextColor);
        jta.setMargin(new Insets(0, 20, 0, 20)); // compensates for the
                                                    // height of the stackUI
        jta.setFont(font);
        jta.setCaretColor(new Color(150, 150, 150));
        highlighter = jta.getHighlighter();

        // create textarea to display linenumbers
        final JTextArea lines = new JTextArea("1");
        lines.setMargin(new Insets(0, 20, 0, 0));
        lines.setForeground(new Color(150, 150, 150));
        lines.setBackground(ApplicationFrame.caBlue);
        lines.setFont(font);
        lines.setEditable(false);

        // listen for changes in jta and update linenumbers
        jta.getDocument().addDocumentListener(new DocumentListener() {
            public String getLinesText() {
                int caretPosition = jta.getDocument().getLength();
                Element root = jta.getDocument().getDefaultRootElement();

                StringBuilder sb = new StringBuilder();
                sb.append( "1" );

                for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    sb.append( System.getProperty("line.separator") + i );
                }
                return sb.toString();
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
        List<Instruction> program = lex(jta.getText());
        return program != null ? program : new ArrayList<Instruction>();
    }

    private List<Instruction> lex(String text) {
        ArrayList<Instruction> p = new ArrayList<Instruction>();
        try {
            p = Lexer.lex(text);
            eui.clearError();
            dirtyText = false;
            System.out.println("Dirty text false: lexed successfully.");
        } catch (LexerException e) {
            redHighlight(e.lineNumber, e.wordStart, e.wordEnd);
            eui.displayError("Lexer Error on line " + (e.lineNumber + 1) + ": " + e.getMessage());
            System.err.println("Lexer error on line " + (e.lineNumber + 1) + ": " + e.getMessage());
            dirtyText = true;
        }
        return p;
    }

    private String antiLex(List<Instruction> program) {
        StringBuilder b = new StringBuilder();
        
        for (Instruction instr : program) {
            if (b.length() != 0)
                b.append("\n");
            
            b.append(instr.name);
            
            if (instr.arg != null)
                b.append(" " + instr.arg.getValue());
        }
        
        dirtyText = false;
        return b.toString();
    }
}
