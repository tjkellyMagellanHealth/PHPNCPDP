/**
 * 
 */
package ncpdp;

import static org.junit.Assert.*;

import java.io.StringReader;

import ncpdp.Batch.Detail;
import ncpdp.Batch.Header;
import ncpdp.Batch.Record;
import ncpdp.Batch.Trailer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



import static ncpdp.Batch.createHeader;
import static ncpdp.Batch.createDetail;
import static ncpdp.Batch.createTrailer;
/**
 * @author tjkelly
 *
 */
public class TestBatch {

	String headerRecord = "\u000200TPHP6218315670102990572  1203022201201300722P11AHCCCS866004791\u0003";
	String detailRecord = "\u0002G10000000002{NCPDP Data}\u0003";
	String trailerRecord = "\u00029912030220000003179ATTESTED CREIGHTON DONOVAN CFO\u0003";
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ncpdp.Batch#createHeader(java.lang.String)}.
	 */
	@Test
	public void testCreateHeader() {
		Header h = createHeader(headerRecord);
		assertEquals("00", h.getSegmentIdentifier());
		assertEquals("T", h.getTransmissionType());
		assertEquals("PHP6218315670102990572  ", h.getSenderID());
		assertEquals("1203022", h.getBatchNumber());
		assertEquals("20120130", h.getCreationDate());
		assertEquals("0722", h.getCreationTime());
		assertEquals("P", h.getFileType());
		assertEquals("11", h.getVersionReleaseNumber());
		assertEquals("AHCCCS866004791", h.getRecieverID());
	}
	/**
	 * Test method for {@link ncpdp.Batch#createHeader(java.lang.String)}.
	 */
	@Test
	public void testCreateDetail() {
		Detail d = createDetail(detailRecord);
		assertEquals("G1", d.getSegmentIdentifier());
		assertEquals("0000000002", d.getTransactionReferenceNumber());
		assertEquals("{NCPDP Data}", d.getNCPDPDataRecord());
	}

	/**
	 * Test method for {@link ncpdp.Batch#createHeader(java.lang.String)}.
	 */
	@Test
	public void testCreateTrailer() {
		
		Trailer t = createTrailer(trailerRecord);
		assertEquals("99", t.getSegmentIdentifier());
		assertEquals("1203022", t.getBatchNumber());
		assertEquals("0000003179", t.getRecordCount());
		assertEquals("ATTESTED CREIGHTON DONOVAN CFO", t.getMessage());
	}
	
	@Test
	public void testRecordIterator() {
		String dataRecords = headerRecord+detailRecord+trailerRecord;
		RecordFactory factory = new RecordFactory();
		RecordIterator recIter = new DefaultRecordIterator(factory
				, new StringReader(dataRecords)
		        ,'\u0002'
		        ,'\u0003');
		
		
		assertEquals(true, recIter.hasNext());
		Record record1 = recIter.next();
		
		assertEquals(true, recIter.hasNext());
		Record record2 = recIter.next();
		
		assertEquals(true, recIter.hasNext());
		Record record3 = recIter.next();

		assertEquals("00",record1.getSegmentIdentifier());
		assertEquals("G1",record2.getSegmentIdentifier());
		assertEquals("99",record3.getSegmentIdentifier());
		
		Header h = (Header) record1;
		assertEquals("00", h.getSegmentIdentifier());
		assertEquals("T", h.getTransmissionType());
		assertEquals("PHP6218315670102990572  ", h.getSenderID());
		assertEquals("1203022", h.getBatchNumber());
		assertEquals("20120130", h.getCreationDate());
		assertEquals("0722", h.getCreationTime());
		assertEquals("P", h.getFileType());
		assertEquals("11", h.getVersionReleaseNumber());
		assertEquals("AHCCCS866004791", h.getRecieverID());
		
		Detail d = (Detail) record2;
		assertEquals("G1", d.getSegmentIdentifier());
		assertEquals("0000000002", d.getTransactionReferenceNumber());
		assertEquals("{NCPDP Data}", d.getNCPDPDataRecord());
		
		Trailer t = (Trailer)record3;
		assertEquals("99", t.getSegmentIdentifier());
		assertEquals("1203022", t.getBatchNumber());
		assertEquals("0000003179", t.getRecordCount());
		assertEquals("ATTESTED CREIGHTON DONOVAN CFO", t.getMessage());
		
	}
	public class MockRecordHandler implements RecordHandler {

		public Header header;
		public Detail detail;
		public Trailer trailer;
		@Override
		public void startBatch() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void endBatch() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void handleHeader(Header header) {
			this.header = header;
			
		}
		@Override
		public void handleDetail(Detail detail) {
			this.detail = detail;
			
		}
		@Override
		public void handleTrailer(Trailer trailer) {
			this.trailer = trailer;
			
		}


	}
	
    @Test
    public final void testBatchRead() {
    	Batch batch = new Batch();
    	String dataRecords = headerRecord+detailRecord+trailerRecord;
    	RecordFactory factory = new RecordFactory();
		RecordIterator recIter = new DefaultRecordIterator(factory
				, new StringReader(dataRecords)
		        ,'\u0002'
		        ,'\u0003');
		MockRecordHandler testHandler = new MockRecordHandler();
		
		RecordReader reader = new RecordReader(recIter,testHandler);
		batch.read(reader);
		
		Header h = testHandler.header;
		assertEquals("00", h.getSegmentIdentifier());
		assertEquals("T", h.getTransmissionType());
		assertEquals("PHP6218315670102990572  ", h.getSenderID());
		assertEquals("1203022", h.getBatchNumber());
		assertEquals("20120130", h.getCreationDate());
		assertEquals("0722", h.getCreationTime());
		assertEquals("P", h.getFileType());
		assertEquals("11", h.getVersionReleaseNumber());
		assertEquals("AHCCCS866004791", h.getRecieverID());
		
		Detail d = testHandler.detail;
		assertEquals("G1", d.getSegmentIdentifier());
		assertEquals("0000000002", d.getTransactionReferenceNumber());
		assertEquals("{NCPDP Data}", d.getNCPDPDataRecord());
		
		Trailer t = testHandler.trailer;
		assertEquals("99", t.getSegmentIdentifier());
		assertEquals("1203022", t.getBatchNumber());
		assertEquals("0000003179", t.getRecordCount());
		assertEquals("ATTESTED CREIGHTON DONOVAN CFO", t.getMessage());
		
		
    	
    }
	
	
	
}
