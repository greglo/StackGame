package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharStackValueTest {

    @Test
    public void creation() {
	CharStackValue a1 = new CharStackValue('A');
	CharStackValue a2 = new CharStackValue(0);
	assertEquals(a1.getValue(), a2.getValue());
    }
    
    @Test
    public void additionByA() {
	CharStackValue a = new CharStackValue('A');
	System.out.println(new CharStackValue(0));
	for (int i = 0; i < 1; i++) {
	    CharStackValue c = new CharStackValue(i);
	    assertEquals(c.getValue(), a.add(c).getValue());
	}
    }
    
    @Test
    public void moduloAddition() {
	CharStackValue b = new CharStackValue('B');
	CharStackValue z = new CharStackValue('Z');
	assertEquals('A', b.add(z).getValue());
    }

}
