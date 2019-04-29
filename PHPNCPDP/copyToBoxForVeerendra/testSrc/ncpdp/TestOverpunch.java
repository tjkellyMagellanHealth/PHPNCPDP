package ncpdp;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestOverpunch {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSetDecimal() {
		Overpunch op = new Overpunch();  
		op.setDecimal();
		assertEquals("-1234.50", op.toString("12345}  "))  ;  
		assertEquals("1234.50", op.toString("12345{"  ))    ; 
		assertEquals("1234.56", op.toString("123456"  ))    ; 
		assertEquals("12345z", op.toString("12345z"  ))    ; 
	}

	@Test
	public final void testSetDecimalInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetInteger() {
		Overpunch op = new Overpunch();  
		op.setInteger();
		assertEquals("-123450", op.toString("12345}  "))  ;  
		assertEquals("123450", op.toString("12345{"  ))    ; 
		assertEquals("123456", op.toString("123456"  ))    ; 
		assertEquals("12345z", op.toString("12345z"  ))    ; 
	}

	@Test
	public final void testSetPrecision() {
		Overpunch op = new Overpunch();  
		op.setDecimal();
		op.setPrecision(3);
		assertEquals("-123.450", op.toString("12345}  "))  ;  
		assertEquals("123.450", op.toString("12345{"  ))    ; 
		assertEquals("123.456", op.toString("123456"  ))    ; 
		assertEquals("12345z", op.toString("12345z"  ))    ; 
	}

	@Test
	public final void testToDouble() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToFloat() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToStringString() {
		Overpunch op = new Overpunch();
		assertEquals("-123450", op.toString("12345}  "))  ;
		assertEquals("123450", op.toString("12345{"  ))    ;
		assertEquals("123456", op.toString("123456"  ))    ;
		assertEquals("12345z", op.toString("12345z"  ))    ;
	}
	@Test
	public final void testIndexOf() {

        String pos = "{ABCDEFGHI";
        String neg = "}JKLMNOPQR";
        for(char test :pos.toCharArray()) {
        	int offset = pos.indexOf(test) + neg.indexOf(test) +1;
            System.out.println(offset);
        }
        for(char test :neg.toCharArray()) {
        	int offset = pos.indexOf(test) + neg.indexOf(test) +1;
            System.out.println(offset);
        }
        
	}

}
