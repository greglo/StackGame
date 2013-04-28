package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import org.junit.Test;

import ox.stackgame.stackmachine.exceptions.*;

public class CharStackValueTest {
    @Test
    public void testAdd() throws TypeException, InvalidCharException {
        CharStackValue a = new CharStackValue('A');
        CharStackValue c = new CharStackValue('C');

        assertEquals(c.getValue(), a.add(c).getValue());
    }

    @Test
    public void testModuloAdd() throws TypeException, InvalidCharException {
        CharStackValue b = new CharStackValue('B');
        CharStackValue z = new CharStackValue('Z');
        assertEquals('A', b.add(z).getValue());
    }

    @Test
    public void testSubInt() throws TypeException, InvalidCharException {
        CharStackValue a = new CharStackValue('A');
        IntStackValue x = new IntStackValue(1);
        assertEquals('Z', a.sub(x).getValue());
    }

    @Test
    public void testSubChar() throws TypeException, InvalidCharException {
        CharStackValue a = new CharStackValue('A');
        CharStackValue c = new CharStackValue('C');
        assertEquals('Y', a.sub(c).getValue());
    }
}
