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
@SuppressWarnings("serial")
public class ProgramTextUI extends JLayeredPane {
    private Mode oldMode = null;
    private Highlighter highlighter;
    private JTextArea jta;
    public static Color editableTextColor = new Color(186, 96, 96);
    public static Color frozenTextColor = new Color(222, 147, 95);
    public static Font f = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    public static int width = 450;

    // code to be executed when a mode is activated.
    private ModeVisitor modeActivationVisitor = new ModeVisitor() {
	public void visit(Mode m) {
	}

	public void visit(DesignMode m) {
	    jta.setEditable(true);
	    jta.setForeground(editableTextColor);
	}

	public void visit(RunMode m) {
	    highlight(0); // TODO check whether this is superfluous
	}
    };

    // code to be executed when a mode is deactivated
    private ModeVisitor modeDeactivationVisitor = new ModeVisitor() {
	public void visit(Mode m) {
	}

	public void visit(DesignMode m) {
	    jta.setEditable(false);
	    jta.setForeground(frozenTextColor);
	}

	public void visit(RunMode m) {

	}
    };

    private StackMachineListener l = new StackMachineListenerAdapter() {
	public void programCounterChanged(int line) {
	    highlight(line);
	}

	public void stackInstructionsChanged(List<Instruction> p) {
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

	    public void changedUpdate(DocumentEvent de) {
		lines.setText(getLinesText());
	    }

	    public void insertUpdate(DocumentEvent de) {
		lines.setText(getLinesText());
	    }

	    public void removeUpdate(DocumentEvent de) {
		lines.setText(getLinesText());
	    }
	});

	// place the textarea in the scrollable window
	jsp.getViewport().add(jta);
	jsp.setRowHeaderView(lines); // keeps the line numbers in sync.

	return jsp;
    }

    private List<Instruction> lex(String text) {
	ArrayList<Instruction> p = new ArrayList<Instruction>();
	// TODO write a real lexer, connect it here.
	p.add(new Instruction("load", new IntStackValue(5)));
	p.add(new Instruction("load", new IntStackValue(3)));
	p.add(new Instruction("add"));
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

	// create buttons
	int r = 60;
	int p = ApplicationFrame.p;
	int h = ApplicationFrame.h;
	int buttonStartY = p;

	// TODO position the buttons in a more sensible way. Get rid of
	// +p+h+ldfklsdkf....

	// create step1 button
	final JButton step1Button = new JButton("Step1");
	step1Button.setForeground(new Color(0, 133, 200));
	step1Button.setBounds(width - r - p, buttonStartY, r, r);
	step1Button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		// when not in runMode already, feed the text through the lexer,
		// switch to RunMode (storing the current mode), pc:=0, call
		// 'step'
		if (modeManager.getActiveMode() != runMode) {
		    // feed text through lexer
		    String text = jta.getText();
		    List<Instruction> instructions = lex(text);
		    // update modeManager.stackMachine
		    modeManager.stackMachine.loadInstructions(instructions);
		    // switch to RunMode
		    modeManager.setActiveMode(runMode);
		}
		// call step
		try {
		    modeManager.stackMachine.step();
		} catch (StackRuntimeException e) {
		    // TODO Handle machine errors
		}
	    }
	});
	this.add(step1Button, new Integer(1));

	// create stepAll Button
	// allows the user to see each command animated until the machine halts.
	final JButton stepAllButton = new JButton("StepAll");
	stepAllButton.setForeground(new Color(0, 133, 200));
	stepAllButton.setBounds(width - r - p, buttonStartY + p + r, r, r);
	stepAllButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		// for a dirty textarea, lex the text, switch to RunMode
		// (storing the current mode), create a timer, call step on the
		// timer until machine terminates.
		// when clicked in runMode, just do the timer stuff
		// disable this button
		// enable pausebutton
	    }
	});
	this.add(stepAllButton, new Integer(1));

	// pauseButton
	// only relevant to stepAll Button
	final JButton pauseButton = new JButton("Pause");
	pauseButton.setEnabled(false);
	pauseButton.setForeground(new Color(0, 133, 200));
	pauseButton
		.setBounds(width - r - p, buttonStartY + p + r + p + r, r, r);
	pauseButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		// disabled in designMode.
		// enabled if machine is being animated
		// when clicked, stop the stepAll timer from calling step
		// enable stepAllButton, enable runAllButton
	    }
	});
	this.add(pauseButton, new Integer(1));

	// create runAll button
	final JButton runAllButton = new JButton("RunAll");
	runAllButton.setForeground(new Color(0, 133, 200));
	runAllButton.setBounds(width - r - p, buttonStartY + p + r + p + r + p
		+ r, r, r);
	runAllButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		// for a dirty textarea, lex the text, switch to RunMode
		// (storing the current mode), call runAll on the stackMachine.
		// when clicked in RunMode, call runAll
		// disable this button
	    }
	});
	this.add(runAllButton, new Integer(1));

	// create reset button
	final JButton resetButton = new JButton("Reset");
	resetButton.setEnabled(false);
	resetButton.setBounds(width - r - p, buttonStartY + p + r + p + r + p
		+ r, r, r);
	resetButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		// (should not be enabled in DesignMode)
		// switch back to old mode, call reset on the stackmachine
	    }
	});
	this.add(resetButton, new Integer(1));
    }
}
