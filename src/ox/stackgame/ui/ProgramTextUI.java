/**
 * 
 */
package ox.stackgame.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;

import ox.stackgame.stackmachine.*;

/**
 * Allows the user to input text while in Challenge/FreeDesignMode.  When user presses 'Play', 
 * the plaintext is fed through a lexer to produce a StackMachine.  This is passed to RunMode, 
 * which is then activated.  The frame is not editable in RunMode, but displays the current 
 * program counter.
 * @author danfox
 *
 */
public class ProgramTextUI extends JPanel {
	private StackMachine activeMachine = null;
	private Highlighter highlighter;
	final JTextArea jta;
	
	private ModeVisitor modeActivationVisitor = new ModeVisitor(){
		public void visit(Mode m) {}	
		
		public void visit(DesignMode m){
			// TODO make JTextArea editable
		}
		
		public void visit(RunMode m){
			// start listening to newly active machine.
			activeMachine = m.getMachine();
			activeMachine.addListener(l);
			// change appearance to awaken. maybe redraw? 
		}	
	};
	
	private ModeVisitor modeDeactivationVisitor = new ModeVisitor(){
		public void visit(Mode m) {}
		
		public void visit(DesignMode m){
			// TODO make JTextArea not editable
		}
		
		public void visit(RunMode m){
			// stop listening.
			activeMachine.removeListener(l);
			activeMachine = null;
		}
	};

	private StackMachineListener l = new StackMachineListenerAdapter() { 
		public void programCounterChanged(int line) {
			try {
				highlighter.removeAllHighlights();
				highlighter.addHighlight(jta.getLineStartOffset(line), jta.getLineEndOffset(line), new DefaultHighlighter.DefaultHighlightPainter(Color.cyan));
			} catch (BadLocationException e){
				throw new RuntimeException ("pc shouldn't be out of bounds");
			}

		}
	};
	
	// I think this is unnecessary
//	private class MyHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
//		MyHighlighter(Color c) {
//			super(c);
//		}
//	}
	
	public ProgramTextUI(ModeManager m){
		super(new BorderLayout());
		
		// appearance
		this.setBackground(Color.red);
		this.setSize(new Dimension(450,ApplicationFrame.h));				
		
		// pay attention to mode changes
		m.registerModeActivationVisitor(modeActivationVisitor);
		m.registerModeDeactivationVisitor(modeDeactivationVisitor);
		
		// TODO add JTextArea from http://www.javaprogrammingforums.com/java-swing-tutorials/915-how-add-line-numbers-your-jtextarea.html
		JScrollPane jsp = new JScrollPane();
		jsp.setBorder(new EmptyBorder(0,0,0,0));
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.getViewport().setBackground(Color.red);
		//jsp.getViewport().setBackground(ApplicationFrame.codeacademyBlue);
		
		Font f = new Font(Font.MONOSPACED, Font.PLAIN, 15);
		Color caRed = new Color(186,96,96);
		
		jta = new JTextArea();
		jta.setBackground(ApplicationFrame.codeacademyBlue);
		jta.setForeground(caRed);
		jta.setMargin(new Insets(20,20,100,20));
		//jta.setBorder(new EmptyBorder (20,20,20,20));
		jta.setFont(f);
		jta.setCaretColor(new Color(150,150,150));
//		new Color(55,59,65)
		
		final JTextArea lines = new JTextArea("1");
		//lines.setBorder(new EmptyBorder (20,20,20,0));
		lines.setMargin(new Insets(20,20,100,0));
		lines.setForeground(new Color(150,150,150));
		lines.setBackground(ApplicationFrame.codeacademyBlue);
		lines.setFont(f);
		lines.setAlignmentX(RIGHT_ALIGNMENT);
		

		lines.setEditable(false);

		highlighter = jta.getHighlighter();		
		
		// listen for changes in jta and update lines.
		jta.getDocument().addDocumentListener(new DocumentListener(){
			public String getLinesText(){
				int caretPosition = jta.getDocument().getLength();
				Element root = jta.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty("line.separator");
				for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++){
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
		
		
		
		jsp.getViewport().add(jta);
		//jsp.getViewport().setComponentZOrder(jta, 0);
		jsp.setRowHeaderView(lines);
		
		// TODO sort out overlap problem. JTextArea is being painted above it's parent z order.
		// http://stackoverflow.com/questions/10093425/java-how-to-draw-non-scrolling-overlay-over-scrollpane-viewport
		
		this.add(jsp, BorderLayout.CENTER); // fills container
	}
}
