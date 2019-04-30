/**
 * 
 */
package hipaa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hipaa.Histogram837.Handler837;
import hipaa.Histogram837.HistogramX12;
import hipaa.Histogram837.HistogramX12.LoopStatistics;
import hipaa.Histogram837.HistogramX12.SegmentStatistics;
import hipaa4010.Handler837P;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import x12.DefaultSegmentIterator;
import x12.X12Reader;

/**
 * @author tjkelly
 *
 */
public class TestHistogram837 {

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
	 * Test method for {@link hipaa.Histogram837.HistogramX12#addLoop(java.lang.String)}.
	 */
	@Test
	public final void testAddLoop() {
		HistogramX12 hist = new HistogramX12();
		String loop = "CLM|20110314318265501|110|||11::7|Y|A|Y|Y|C~" + 
		"REF|F8|111160366281~" + 
		"HI|BK:37991|BF:71941|BF:71946~" + 
		"SBR|P|18|||MC||||MC~" + 
		"AMT|D|75.04~" + 
		"AMT|B6|75.04~" + 
		"DMG|D8|19810514|M~" + 
		"OI|||Y|S||Y~" + 
		"NM1|IL|1|CLENDENEN|RICHARD||||MI|A12260057~" + 
		"N3|2208 W PINKLEY AVE~" + 
		"N4|COOLIDGE|AZ|85128~" + 
		"NM1|PR|2|PHOENIX HEALTH PLAN|||||PI|0102990572~" + 
		"DTP|573|D8|20110413~" + 
		"REF|F8|20110314318265501~" +
		""
		;
		//(SegmentFactory segmentFactory,Reader reader,  char segmentDelimiter, char fieldDelimiter, char subFieldDelimiter) {
//		DefaultSegmentIterator iterator = new DefaultSegmentIterator(
//		    new SegmentFactory("hipaa4010")
//		   ,new StringReader(loop)
//		   ,'~'
//		   ,'|'
//		   ,':');
		
		
		
		LoopStatistics loopStats = hist.addLoop("2300", loop, '~' , '|' , ':');
		Set<String> segmentName = loopStats.keySet();
		System.out.println(segmentName);
		assertTrue(segmentName.contains("CLM\t"));
		assertTrue(segmentName.contains("REF\tF8"));
		assertTrue(segmentName.contains("HI\tBK"));
		assertTrue(segmentName.contains("SBR\tP"));
		assertTrue(segmentName.contains("AMT\tD"));
		assertTrue(segmentName.contains("AMT\tB6"));
		assertTrue(segmentName.contains("DMG\tD8"));
		assertTrue(segmentName.contains("OI\t"));
		assertTrue(segmentName.contains("NM1\tIL"));
		assertTrue(segmentName.contains("N3\t"));
		assertTrue(segmentName.contains("N4\t"));
		assertTrue(segmentName.contains("NM1\tPR"));
		assertTrue(segmentName.contains("DTP\t573"));
		
		//CLM|20110314318265501|110|||11::7|Y|A|Y|Y|C
		SegmentStatistics segStats = loopStats.get("CLM\t");
		assertEquals("20110314318265501", segStats.getBiggest(1));
		assertEquals("110", segStats.getBiggest(2));
		assertEquals("", segStats.getBiggest(3));
		assertEquals("", segStats.getBiggest(4));
		assertEquals("11::7", segStats.getBiggest(5));
		assertEquals("Y", segStats.getBiggest(6));
		assertEquals("A", segStats.getBiggest(7));
		assertEquals("Y", segStats.getBiggest(8));
		assertEquals("Y", segStats.getBiggest(9));
		assertEquals("C", segStats.getBiggest(10));
		// REF|F8|20110314318265501
		assertEquals("20110314318265501", loopStats.get("REF\tF8").getBiggest(2));
		
		
	}


	@Test
	public final void testHandler837() throws Exception {
		String isa = "ISA|00|          |00|          |ZZ|PHP621831567   |ZZ|AHCCCS866004791|120210|0518|U|00401|120411718|0|P|:~" + 
		"GS|HC|010299057|AHCCCS866004791|20120210|0518|120411718|X|004010X098A1~" + 
		"ST|837|0001~" + 
		"BHT|0019|00|120411718|20120210|0518|RP~" + 
		"REF|87|004010X098A1~" + 
		"NM1|41|2|PHOENIX HEALTH PLAN|||||46|0102990572~" + 
		"PER|IC|CREIGHTON DONOVAN|ED|TOMYKNOWLEDGEINFORMATIONANDBELIEFTHEDATAINTHISFILEISACCURATECOMPLETEANDTRUE|EM|CDONOVAN@ABRAZONHEALTH.COM|TE|6028243734~" + 
		"PER|IC|EDI SUPPORT|TE|6026746951~" + 
		"NM1|40|2|AHCCCS|||||46|866004791~" + 
		"";
		X12Reader reader = new X12Reader(new DefaultSegmentIterator(isa));
		Handler837 handler = new Handler837(new HistogramX12());
		reader.setHandler(handler.getHandler837P());
		reader.readIsa();
		handler.getHistogram().report(System.out);

		
		
		
	}

}
