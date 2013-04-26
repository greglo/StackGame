package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import org.junit.Test;

import ox.stackgame.stackmachine.exceptions.TypeException;

public class CharStackValueTest {

    @Test
    public void testConstructor() {
	CharStackValue a1 = new CharStackValue('A');
	CharStackValue a2 = new CharStackValue(-26);
	assertEquals(a1.getValue(), a2.getValue());
    }
    
    @Test
    public void testAdd() throws TypeException {
	CharStackValue a = new CharStackValue('A');
	for (int i = 0; i < 1; i++) {
	    CharStackValue c = new CharStackValue(i);
	    assertEquals(c.getValue(), a.add(c).getValue());
	}
    }
    
    @Test
    public void testModuloAdd() throws TypeException {
	CharStackValue b = new CharStackValue('B');
	CharStackValue z = new CharStackValue('Z');
	assertEquals('A', b.add(z).getValue());
    }
    
    @Test
    public void testSubInt() throws TypeException {
	CharStackValue a = new CharStackValue('A');
	IntStackValue x = new IntStackValue(1);
	assertEquals('Z', a.sub(x).getValue());
    }
    
    @Test
    public void testSubChar() throws TypeException {
	CharStackValue a = new CharStackValue('A');
	CharStackValue c = new CharStackValue('C');
	assertEquals('Y', a.sub(c).getValue());
    }

}
