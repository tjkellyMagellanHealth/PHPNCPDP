package hipaa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.TreeSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import x12.DefaultSegmentIterator;
import x12.SegmentFactory;
import x12.SegmentIterator;
import x12.X12Reader;

public class TestSimpleReport837 {

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
	public final void testFormatStringObjectArray() {
		String s = String.format("DiagnosisCode%1$02d", 1);
		System.out.println(s);
		s = String.format("DiagnosisCode%1$02d", 12);
		System.out.println(s);
		
	}
	@Test
	public final void testGetDates() {
		{
		    String results[]  = SimpleReport837.getDates("20120131-20120201");
		    assertEquals("20120131", results[0]);
		    assertEquals("20120201", results[1]);
		}
		{
		    String results[]  = SimpleReport837.getDates("2012013120120201");
		    assertEquals("20120131", results[0]);
		    assertEquals("20120201", results[1]);
		}
		{
		    String results[]  = SimpleReport837.getDates("-20120201");
		    assertEquals("", results[0]);
		    assertEquals("", results[1]);
		}
		{
		    String results[]  = SimpleReport837.getDates("20120201");
		    assertEquals("20120201", results[0]);
		    assertEquals("", results[1]);
		}
		
	}
	@Test
	public final void testReportCreate() {
		SimpleReport837 report = new SimpleReport837();
		report.set("SubmitterFirstName", "Joe");
		assertEquals("Joe", report.get("SubmitterFirstName"));
		report.set("Receiver", "Tom");
		assertEquals("Tom", report.get("Receiver"));
		report.set("2000A", "Blank");
		report.createSubmitterReceiver();
		assertEquals("", report.get("SubmitterFirstName"));
		assertEquals("", report.get("Receiver"));
		assertEquals("", report.get("2000A"));

	}
	@Test
	public final void testUniqueFieldNames() {
		TreeSet<String>fields = new TreeSet<String>();
		for(String field : new SimpleReport837().fieldNames) {
			if(fields.contains(field)) {
				fail("duplicate field (" + field +")");
				
			}else {
				fields.add(field);
				System.out.println(field);
			}
		}
		
	}
    @Test
    public final void test837IToFields() throws Exception {
    	String loop =
    		    "ISA|00|          |00|          |ZZ|PHP621831567   |ZZ|AHCCCS866004791|120210|0629|U|00401|120411829|0|P|:~" + 
    		    "GS|HC|010299057|AHCCCS866004791|20120210|0629|120411829|X|004010X096A1~" + 
    		    "ST|837|0001~" +                                            // ST       
    		    "BHT|0019|00|120411829|20120210|0629|RP~" + 
    		    "REF|87|004010X096A1~" + 
    		    "NM1|41|2|PHOENIX HEALTH PLAN|||||46|0102990572~" +         // 1000A
    		    "PER|IC|CREIGHTON DONOVAN|ED|TOMYKNOWLEDGEINFORMATIONANDBELIEFTHEDATAINTHISFILEISACCURATECOMPLETEANDTRUE|EM|CDONOVAN@ABRAZONHEALTH.COM|TE|6028243734~" + 
    		    "NM1|40|2|AHCCCS|||||46|866004791~" +                       // 1000B
    		    "HL|1||20|1~" +                                             // 2000A
    		    "NM1|85|2|CHANDLER REG HOSPITAL|||||XX|1700910189~" +       // 2010AA
    		    "N3|475 S. DOBSON RD.~" + 
    		    "N4|CHANDLER|AZ|85224~" + 
    		    "REF|EI|721561132~" +
    		    "PER|IC|EDI SUPPORT|TE|6026746951|FX|6026746952~" +  
    		    "HL|2|1|22|0~" +                                            // 2000B
    			"SBR|S|18|010299||||||MC~" + 
    			"NM1|IL|1|SMITH|JOAN|J|||MI|A23130000~" + 
    			"N3|TILGAS MANOR~" + 
    			"N4|A CITY|AZ|85298~" + 
    			"DMG|D8|19820417|F~" + 
    			"NM1|PR|2|AHCCCS|||||PI|866004791~" + 
    			"N3|801 E JEFFERSON~" + 
    			"N4|PHOENIX|AZ|81516~" +
    			"CLM|2011113036368222|3925.68|||13:A:1|Y|A|Y|Y|||||||||N~" +     //2300 Claim Info
    			"DTP|434|D8|20111130~" + 
    			"DTP|435|DT|201111300000~" + 
    			"CL1|1|2|01~" + 
    			"REF|EA|M000450430~" + 
    			"HI|BK:78659|ZZ:78659~" + 
    			"HI|BF:2948~" + 
    			"HI|BH:11:D8:20111129|BH:18:D8:19900101~" + 
    			"NM1|71|1|JONES|JOHN||||XX|12345678~" +                         // 2310A Attending Physician Name
    			"PRV|AT|ZZ|363LP0200N~"+
    			"SBR|P|18|||||||MB~" +                                          // 2320 Other Subscriber Info
    			"CAS|PR|1|11||2|12||3|13||4|14||~" + 
    			"CAS|PR|1|21||2|22||3|23||4|24||~" + 
    			"AMT|N1|235.54~" + 
    			"AMT|B6|285.54~" + 
    			"DMG|D8|19420417|F~" + 
    			"OI|||Y|||Y~" + 
    			"NM1|IL|1|YARBOR|JOAN|J|||MI|A23130001~" +                     // 2330A Other Subscriber Name
    			"N3|TILGAS MANOR~" + 
    			"N4|A CITY|AZ|81516~" + 
    			"NM1|PR|2|AAHP|||||PI|M20010715~" +                             // 2330B Other Payer Name 
    			"DTP|573|D8|20120118~" + 
    			"REF|F8|2011113036368222~" + 
    			"SBR|S|18|||||||MC~" +                                          // 2320 Other Subscriber Info
    			"AMT|A8|49.5~" + 
    			"AMT|B6|425.82~" + 
    			"DMG|D8|19420417|F~" + 
    			"OI|||Y|||Y~" + 
    			"NM1|IL|1|SMITH|JOAN|J|||MI|A23130002~" +                     // 2330A Other Subscriber Name
    			"N3|TILGAS MANOR~" + 
    			"N4|A CITY|AZ|81516~" + 
    			"NM1|PR|2|PHOENIX HEALTH PLAN|||||PI|0102990572~" +             // 2330B Other Payer Name 
    			"DTP|573|D8|20120118~" + 
    			"REF|F8|2011113036368222~" + 
    			"LX|1~" +                                                       // 2400 Service Line  
    			"SV2|0300|HC:36415|33|UN|1~" + 
    			"DTP|472|D8|20111130~" + 
    			"SVD|0102990572|0|HC:36415|0300|1~" + 
    			"CAS|OA|23|1||96|32|1~" +
    			"CAS|CO|45|665.76~" + 
    			"CAS|PR|1|10||2|20||3|30||~" +
    			"DTP|573|D8|20120118~" + 
    			"SVD|M20010715|1|HC:36415|0300|1~" + 
    			"CAS|CO|45|32~" + 
    			"CAS|PR|2|50~" +
    			"DTP|573|D8|20120118~" + 
    			"";
    	
    	SimpleReport837 tester = new SimpleReport837();
//		SegmentIterator iterator = new DefaultSegmentIterator(new SegmentFactory("hipaa4010")
//		    , new StringReader(loop)
//		    ,'~','|',':');
//		X12Reader x12Reader = new X12Reader(iterator);
//		x12Reader.setHandler(tester);
//		
//    	hipaa4010.X837I_2000b x2000b = new hipaa4010.X837I_2000b(x12Reader);

		SegmentIterator iterator = new DefaultSegmentIterator(loop);
		X12Reader x12Reader = new X12Reader(iterator);
		x12Reader.setHandler(tester);
		x12Reader.readIsa();
		assertEquals("721561132",tester.get("BillingProviderTaxIdNumber"));
		assertEquals("6026746951",tester.get("BillingProviderTelephoneNumber"));
		assertEquals("6026746952",tester.get("BillingProviderFaxNumber"));
    	assertEquals("18", tester.get("PatientRelationshipToSubscriber"));

    	assertEquals("1",tester.get("TypeOfAdmission"));
    	assertEquals("2",tester.get("SourceOfAdmission"));
    	assertEquals("MC", tester.get("PlanType"));
    	assertEquals("13", tester.get("FacilityTypeCode"));
    	assertEquals("20120118", tester.get("OtherSubscriber01ClaimPaidDate"));
    	assertEquals("32", tester.get("OtherSubscriber01Deductible"));
    	assertEquals("34", tester.get("OtherSubscriber01CoInsurance"));
    	assertEquals("36", tester.get("OtherSubscriber01CoPay"));
    	assertEquals("38", tester.get("OtherSubscriber01AmountNotCovered"));
    	assertEquals("49.5", tester.get("OtherSubscriber02AmountNotCovered"));
    	assertEquals("363LP0200N", tester.get("AttendingProviderTaxonomy"));
    	assertEquals("10",tester.get("Adjudication01Deductible"));
    	assertEquals("20",tester.get("Adjudication01CoInsurance"));
    	assertEquals("30",tester.get("Adjudication01CoPay"));
    	
    	
    	
    	
    }
    
    @Test
    public final void test837PToFields() throws Exception {
    	String loop =
    	    "ISA|00|          |00|          |ZZ|PHP621831567   |ZZ|AHCCCS866004791|120210|0518|U|00401|120411718|0|P|:~" + 
    		"GS|HC|010299057|AHCCCS866004791|20120210|0518|120411718|X|004010X098A1~" + 
    		"ST|837|0001~" +                                                // ST
    		"BHT|0019|00|120411718|20120210|0518|RP~" + 
    		"REF|87|004010X098A1~" + 
    		"NM1|41|2|PHOENIX HEALTH PLAN|||||46|0102990572~" +              // 1000A
    		"PER|IC|CREIGHTON DONOVAN|ED|TOMYKNOWLEDGEINFORMATIONANDBELIEFTHEDATAINTHISFILEISACCURATECOMPLETEANDTRUE|EM|CDONOVAN@ABRAZONHEALTH.COM|TE|6028243734~" + 
    		"PER|IC|EDI SUPPORT|TE|6026746951~" + 
    		"NM1|40|2|AHCCCS|||||46|866004791~" +                            // 1000B
    		"HL|1||20|1~" +                                                  // 2000A
    		"NM1|85|2|SONORAN FAM PRACTICE|||||XX|1588734750~" +             // 2010AA 
    		"N3|171 W. CENTRAL AVE.~" + 
    		"N4|COOLIDGE|AZ|85128~" + 
    		"REF|EI|860721018~" +     		
    		"PER|IC|EDI SUPPORT|TE|6026746951|FX|FAX6026746951~" + 

    		"HL|2|1|22|0~" +                                                 // 2000B
    		"SBR|S|18|010299||||||MC~" +                                     //
    		"NM1|IL|1|CLENDENEN|RICHARD||||MI|A12260057~" +                  // 2010BA
    		"N3|2208 W PINKLEY AVE~" +                                       //
    		"N4|COOLIDGE|AZ|85128~" +                                        //
    		"DMG|D8|19810514|M~" +                                           //
    		"NM1|PR|2|AHCCCS|||||PI|866004791~" +                            //
    		"N3|801 E JEFFERSON~" +                                          // 2010BB
    		"N4|PHOENIX|AZ|85034~" +                                         //
    		"CLM|20110314318265501|110|||11::7|Y|A|Y|Y|C~" +                 // 2300 Claim Info
    		"REF|F8|111160366281~" +                                         //
    		"HI|BK:37991|BF:71941|BF:71946~" +                               //
    		"NM1|82|2|SONORAN FAM PRACTICE|||||XX|1588734750~" +             // 2310B Rendering Provider 
    		"PRV|PE|ZZ|363LP0200N~"+
    		"SBR|P|18|||MC||||MC~" +                                         // 2320
			"CAS|PR|1|11||2|12||3|13||4|14||~" + 
			"CAS|PR|1|31||2|32||3|33||4|34||~" + 
    		"AMT|D|75.04~" +                                                 //
    		"AMT|B6|75.04~" +                                                //
    		"DMG|D8|19810514|M~" +                                           //
    		"OI|||Y|S||Y~" +                                                 //
    		"NM1|IL|1|CLENDENEN|RICHARD||||MI|A12260057~" +                  // 2330A
    		"N3|2208 W PINKLEY AVE~" +                                       //
    		"N4|COOLIDGE|AZ|85128~" +                                        //
    		"NM1|PR|2|PHOENIX HEALTH PLAN|||||PI|0102990572~" +              // 2330B
    		"DTP|573|D8|20110413~" +                                         //
    		"REF|F8|20110314318265501~" +                                    //
    		"LX|1~" +                                                        //2400
    		"SV1|HC:99203|110|UN|1|||1~" +                                   //
    		"DTP|472|D8|20110314~" +                                         //
    		"REF|6R|31826550~" +                                             //
    		"AMT|AAE|75.04~" +                                               //
    		"SVD|0102990572|75.04|HC:99203||1~" +                            //
    		"CAS|CO|45|34.96~" +                                             //
    		"DTP|573|D8|20110413~";                                          //
    	
    	SimpleReport837 tester = new SimpleReport837();
		SegmentIterator iterator = new DefaultSegmentIterator(loop);
		X12Reader x12Reader = new X12Reader(iterator);
		x12Reader.setHandler(tester);
		x12Reader.readIsa();
		assertEquals("860721018",tester.get("BillingProviderTaxIdNumber"));
		assertEquals("6026746951",tester.get("BillingProviderTelephoneNumber"));
		assertEquals("FAX6026746951",tester.get("BillingProviderFaxNumber"));
    	assertEquals("18", tester.get("PatientRelationshipToSubscriber"));

//    	assertEquals("1",tester.get("TypeOfAdmission"));
//    	assertEquals("2",tester.get("SourceOfAdmission"));
    	assertEquals("MC", tester.get("PlanType"));
    	assertEquals("11", tester.get("FacilityTypeCode"));
    	assertEquals("20110413", tester.get("OtherSubscriber01ClaimPaidDate"));
    	assertEquals("42", tester.get("OtherSubscriber01Deductible"));
    	assertEquals("44", tester.get("OtherSubscriber01CoInsurance"));
    	assertEquals("46", tester.get("OtherSubscriber01CoPay"));
    	assertEquals("48", tester.get("OtherSubscriber01AmountNotCovered"));
//    	assertEquals("49.5", tester.get("OtherSubscriber02AmountNotCovered"));
    	assertEquals("363LP0200N", tester.get("RenderingProviderTaxonomy"));
    	
    	
    	
    	
    	
    }
}
