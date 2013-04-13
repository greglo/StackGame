/**
 * 
 */
package ox.stackgame.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.Element;

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
			// TODO Change highlighting based on pc
		}
	};
	
	public ProgramTextUI(ModeManager m){
		super(new BorderLayout());
		
		// appearance
		this.setBackground(Color.red);
		this.setPreferredSize(new Dimension(450,ApplicationFrame.h));		

		// pay attention to mode changes
		m.registerModeActivationVisitor(modeActivationVisitor);
		m.registerModeDeactivationVisitor(modeDeactivationVisitor);
		
		// TODO add JTextArea from http://www.javaprogrammingforums.com/java-swing-tutorials/915-how-add-line-numbers-your-jtextarea.html
		JScrollPane jsp = new JScrollPane();
		jsp.setBorder(new EmptyBorder(0,0,0,0));
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.getViewport().setBackground(Color.red);
		//jsp.getViewport().setBackground(ApplicationFrame.codeacademyBlue);
		final JTextArea jta = new JTextArea();
		jta.setBackground(ApplicationFrame.codeacademyBlue);
		jta.setForeground(Color.red);
		final JTextArea lines = new JTextArea("1");
		
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);
		
		
//		jta.getDocument().addDocumentListener(new DocumentListener(){
//			public String getText(){
//				int caretPosition = jta.getDocument().getLength();
//				Element root = jta.getDocument().getDefaultRootElement();
//				String text = "1" + System.getProperty("line.separator");
//				for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++){
//					text += i + System.getProperty("line.separator");
//				}
//				return text;
//			}
//
//			public void changedUpdate(DocumentEvent de) {
//				lines.setText(getText());
//			}
// 
//			public void insertUpdate(DocumentEvent de) {
//				lines.setText(getText());
//			} 
//
//			public void removeUpdate(DocumentEvent de) {
//				lines.setText(getText());
//			} 
//		});
		
		jsp.getViewport().add(jta);
		//jsp.getViewport().setComponentZOrder(jta, 0);
//		jsp.setRowHeaderView(lines);
		
		// TODO sort out overlap problem. JTextArea is being painted above it's parent z order.
		// http://stackoverflow.com/questions/10093425/java-how-to-draw-non-scrolling-overlay-over-scrollpane-viewport
		
		this.add(jsp, BorderLayout.CENTER); // fills container
	}
}
