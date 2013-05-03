package ox.stackgame.stackmachine;

import static org.junit.Assert.*;

import org.junit.Test;

import ox.stackgame.stackmachine.exceptions.*;

public class IntStackValueTest {
    @Test
    public void testAdd() throws TypeException {
        IntStackValue a = new IntStackValue(2);
        IntStackValue b = new IntStackValue(3);
        assertEquals(new Integer(5), a.add(b).getValue());
    }

    @Test
    public void testSub() throws TypeException {
        IntStackValue a = new IntStackValue(2);
        IntStackValue b = new IntStackValue(3);
        assertEquals(new Integer(1), b.sub(a).getValue());
    }

    @Test
    public void testMul() throws TypeException {
        IntStackValue a = new IntStackValue(2);
        IntStackValue b = new IntStackValue(3);
        assertEquals(new Integer(6), a.mul(b).getValue());
    }

    @Test
    public void testDiv() throws TypeException, DivisionByZeroException {
        IntStackValue a = new IntStackValue(2);
        IntStackValue b = new IntStackValue(5);
        assertEquals(new Integer(2), b.div(a).getValue());
    }

    @Test(expected=TypeException.class)
    public void testTypeException() throws TypeException, InvalidCharException, DivisionByZeroException {
        IntStackValue a = new IntStackValue(2);
        CharStackValue b = new CharStackValue('A');
        a.div(b);
    }
}
