package ncpdp;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;

import ncpdp.B1.Insurance;
import ncpdp.B1.Patient;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSegmentIterator {

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

	/*
	 * String fields = "\u001CAM01" + 
				"\u001CC419860710" + 
				"\u001CC52" + 
				"\u001CC701";
		Patient p = new Patient(fields);
		assertEquals("19860710", p.getDateOfBirth());
		assertEquals("2", p.getPatientGenderCode());
		assertEquals("01", p.getPlaceofService());
		
		String fields = "*AM04*C2A13680228*CCBRANDI*CDFULLAM" ;
		Insurance i = new Insurance(fields);
		assertEquals("04", i.getSegmentId());
		assertEquals("A13680228", i.getCardholderID());
		assertEquals("BRANDI", i.getCardholderFirstName() );
		assertEquals("FULLAM", i.getCardholderLastName() );
	 */
	
	@Test
	public final void testPeek() {
		//SegmentFactory segmentFactory,Reader reader,  char segmentSeparator, char fieldSeparator, char groupSeparator
		
		String fields = "*AM01" + 
		"*C419860710" + 
		"*C52" + 
		"*C701~" +
		"*AM04*C2A13680228*CCBRANDI*CDFULLAM~"
		;
		
		SegmentIterator tester = new DefaultSegmentIterator(new SegmentFactory()
		,new StringReader(fields)
		,'~'
		,'*'
		,':');
		Patient p = (Patient) tester.next();
		assertEquals("19860710", p.getDateOfBirth());
		assertEquals("2", p.getPatientGenderCode());
		assertEquals("01", p.getPlaceofService());
		Insurance i = (Insurance)tester.next();
		assertEquals("04", i.getSegmentId());
		assertEquals("A13680228", i.getCardholderID());
		assertEquals("BRANDI", i.getCardholderFirstName() );
		assertEquals("FULLAM", i.getCardholderLastName() );
		
	}
	
	@Test
	public final void testParse() {
		B1 b1 = new B1();
		String fields = "*AM01" + 
		"*C419860710" + 
		"*C52" + 
		"*C701~" +
		"*AM04*C2A13680228*CCBRANDI*CDFULLAM~"
		;
		
		SegmentIterator iterator = new DefaultSegmentIterator(new SegmentFactory()
		,new StringReader(fields)
		,'~'
		,'*'
		,':');
		MockSegmentHandler testHandler = new MockSegmentHandler();
		
		NcpdpReader reader = new NcpdpReader(iterator, testHandler);
		b1.read(reader);
		
		Patient p = testHandler.patient;
		assertEquals("19860710", p.getDateOfBirth());
		assertEquals("2", p.getPatientGenderCode());
		assertEquals("01", p.getPlaceofService());
		Insurance i = testHandler.insurance;
		assertEquals("04", i.getSegmentId());
		assertEquals("A13680228", i.getCardholderID());
		assertEquals("BRANDI", i.getCardholderFirstName() );
		assertEquals("FULLAM", i.getCardholderLastName() );
		
		
		
	}


}
