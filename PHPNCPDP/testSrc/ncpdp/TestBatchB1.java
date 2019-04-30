package ncpdp;

import static ncpdp.B1.*;
import static org.junit.Assert.*;

import java.io.StringReader;

import ncpdp.B1.Insurance;
import ncpdp.B1.Patient;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestBatchB1 {
	String headerRecord = "\u000200TPHP6218315670102990572  1203022201201300722P11AHCCCS866004791\u0003";
	String detailRecord[] 
    = {    "\u0002G10000000002" 
         , "{NCPDP Data}"
         , "\u0003"
      };
	String trailerRecord = "\u00029912030220000003179ATTESTED CREIGHTON DONOVAN CFO\u0003";
    String B1Segments 
    = "01029951B112015520341011346307600     2012010200YY      ." + 
	  ":AM04:C2A13680228:CCBRANDI:CDFULLAM." + 
	  ":AM01:C419860710:C52:C701." + 
	  ":AM07:EM1:D24433298:E103:D700406035705:E70000020000:D300:D5005:D60:D80:DE20120102:DF0:C802:CW1201552034:28EA." + 
	  ":AM03:EZ01:DB1417052325." + 
	  ":AM05:4C2:5C01:6C99:7C0R59377271:HB2:HC04:DV0000000{:HC07:DV0000000{:5E1:6E123:5C02:6C99:7C010299057:HB6:HC04:DV0000012{:HC07:DV0000010I:HC08:DV0000022I:HC99:DV0000000{:HC99:DV0000000{:HC99:DV0000000{." + 
	  ":AM11:D90000010I:DC0000012{:DU0000022I";
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
	public final void testBatchReadB1Read() {
		// Add B1 to Batch;
		
		Batch batch = new Batch();
		B1Segments = B1Segments.replace(':', FieldSeparator);
		B1Segments = B1Segments.replace('.', SegmentSeparator);
		B1Segments = B1Segments.replace(',', GroupSeparator);
		
		String dataRecords = headerRecord+detailRecord[0]+B1Segments+detailRecord[2]+trailerRecord;
		RecordIterator recIter = new DefaultRecordIterator(new RecordFactory()
				, new StringReader(dataRecords)
		        ,'\u0002'
		        ,'\u0003');

		MockSegmentHandler testSegmentHandler = new MockSegmentHandler();
		DefaultRecordHandler testRecordHandler = new DefaultRecordHandler(testSegmentHandler);
		RecordReader reader = new RecordReader(recIter,testRecordHandler);
		batch.read(reader);
		
		Patient p = testSegmentHandler.patient;
		assertEquals("19860710", p.getDateOfBirth());
		assertEquals("2", p.getPatientGenderCode());
		assertEquals("01", p.getPlaceofService());
		Insurance i = testSegmentHandler.insurance;
		assertEquals("04", i.getSegmentId());
		assertEquals("A13680228", i.getCardholderID());
		assertEquals("BRANDI", i.getCardholderFirstName() );
		assertEquals("FULLAM", i.getCardholderLastName() );
		
		
	}

}
