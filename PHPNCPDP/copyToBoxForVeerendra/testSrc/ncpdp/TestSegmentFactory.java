package ncpdp;

import static ncpdp.B1.*;
import static org.junit.Assert.*;

import ncpdp.B1.Patient;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSegmentFactory {

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
	public final void testBuild() {
		SegmentFactory factory = new SegmentFactory();
		Segment s = factory.build("AM07", FieldSeparator, GroupSeparator);
		assertEquals("Claim", s.getClass().getSimpleName());
		String fields = "\u001CAM01" + 
						"\u001CC419860710" + 
						"\u001CC52" + 
						"\u001CC701";
		Patient patient = (Patient) factory.build(fields, FieldSeparator, GroupSeparator);
		assertEquals("19860710", patient.getDateOfBirth());
		assertEquals("2", patient.getPatientGenderCode());
		assertEquals("01", patient.getPlaceofService());
	}

}
