package ncpdp;

import static ncpdp.B1.FieldSeparator;
import static ncpdp.B1.createHeader;
import static org.junit.Assert.assertEquals;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncpdp.B1.COB_OtherPayments;
import ncpdp.B1.COB_OtherPayments.Detail;
import ncpdp.B1.Claim;
import ncpdp.B1.Clinical;
import ncpdp.B1.Compound;
import ncpdp.B1.DUR_PPS;
import ncpdp.B1.Header;
import ncpdp.B1.Insurance;
import ncpdp.B1.Patient;
import ncpdp.B1.PatientResponsibilityAmount;
import ncpdp.B1.Prescriber;
import ncpdp.B1.Pricing;
import ncpdp.B1.QualifiedAmount;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author tjkelly
 *
 * J:\tkelly\PHPNCPDP\SampleData
 * 
 * HEADER    01029951B112015520341011346307600     2012010200YY      
 * INSURANCE AM04C2A13680228CCBRANDICDFULLAM
 * PATIENT   AM01C419860710C52C701
 *
 * CLAIM OR SERVICE BILLING
 *     CLAIM              AM07EM1D24433298E103D700406035705E70000020000D300D5005D60D80DE20120102DF0C802CW120155203428EA
 *     PRESCRIBER         AM03EZ01DB1417052325
 *     COB/OTHER PAYMENTS AM054C25C016C997C0R59377271HB2HC04DV0000000{HC07DV0000000{5E16E1235C026C997C010299057HB6HC04DV0000012{HC07DV0000010IHC08DV0000022IHC99DV0000000{HC99DV0000000{HC99DV0000000{
 *     PRICING            AM11D90000010IDC0000012{DU0000022I
 * 
 */

public class TestB1 {

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
	public void testCreateHeader() {
		Header h = createHeader("01029951B112015520341011346307600     2012010200YY      ");

		assertEquals("010299", h.getBINNumber());
		assertEquals("51", h.getVersionReleaseNumber());
		assertEquals("B1", h.getTransactionCode());
		assertEquals("1201552034", h.getProcessorControlNumber());
		assertEquals("1", h.getTransactionCount());
		assertEquals("01", h.getServiceProviderIDQualifier());
		assertEquals("1346307600     ", h.getServiceProviderID());
		assertEquals("20120102", h.getDateOfService());
		assertEquals("00YY      ", h.getSoftwareVendorCertificationID());
	}


	@Test
	public void testSegment() {
		String fields = "\u001CAMField1"
			          + "\u001CC2Field2";
		Segment s = new Segment(fields);
		assertEquals("Field1", s.getValue("AM"));
		assertEquals("Field2", s.getValue("C2"));
		
	}
	@Test
	public void testPatient() {
		String fields = "\u001CAM01" + 
				"\u001CC419860710" + 
				"\u001CC52" + 
				"\u001CC701";
		Patient p = new Patient(fields);
		assertEquals("19860710", p.getDateOfBirth());
		assertEquals("2", p.getPatientGenderCode());
		assertEquals("01", p.getPlaceofService());
		
	}
	@Test
	public void testInsurance() {
		String fields = "\u001CAM04\u001CC2A13680228\u001CCCBRANDI\u001CCDFULLAM" ;
		Insurance i = new Insurance(fields);
		assertEquals("04", i.getSegmentId());
		assertEquals("A13680228", i.getCardholderID());
		assertEquals("BRANDI", i.getCardholderFirstName() );
		assertEquals("FULLAM", i.getCardholderLastName() );
	}
	/*
	 *     CLAIM              AM07EM1D24433298E103D700406035705E70000020000D300D5005D60D80DE20120102DF0C802CW120155203428EA
	 *     PRESCRIBER         AM03EZ01DB1417052325
	 *     COB/OTHER PAYMENTS AM054C25C016C997C0R59377271HB2HC04DV0000000{HC07DV0000000{5E16E1235C026C997C010299057HB6HC04DV0000012{HC07DV0000010IHC08DV0000022IHC99DV0000000{HC99DV0000000{HC99DV0000000{
	 *     PRICING            AM11D90000010IDC0000012{DU0000022I
	 */

	@Test
	public void testClaim() {
		String fields = "\u001CAM07"
			+ "\u001CEM1"
			+ "\u001CD24433298"
			+ "\u001CE103"
			+ "\u001CD700406035705"
			+ "\u001CE70000020000"
			+ "\u001CD300"
			+ "\u001CD5005"
			+ "\u001CD60"
			+ "\u001CD80"
			+ "\u001CDE20120102"
			+ "\u001CDF0"
			+ "\u001CC802"
			+ "\u001CCW1201552034"
			+ "\u001C28EA" ;
		Claim c = new Claim(fields);
		assertEquals("07", c.getSegmentId());
		assertEquals("1", c.getPrescriptionServiceReferenceNumberQualifier());
		assertEquals("4433298", c.getPrescriptionServiceReferenceNumber());
		assertEquals("03", c.getProductServiceIDQualifier());
		assertEquals("00406035705", c.getProductServiceID());
		assertEquals("0000020000", c.getQuantityDispensed());
		assertEquals("00", c.getFillNumber());
		assertEquals("005", c.getDaysSupply());
		assertEquals("0", c.getCompoundCode());
		assertEquals("0", c.getDispenseAsWrittenProductSelectionCode());
		assertEquals("20120102", c.getDatePrescriptionWritten());
		assertEquals("0", c.getNumberofRefillsAuthorized());
		assertEquals("02", c.getOtherCoverageCode());
		assertEquals("1201552034", c.getAlternateID());
		assertEquals("EA", c.getUnitOfMeasure());
		
	}
	@Test
	public void testPrescriber() {
		String fields = "\u001CAM03" + 
				"\u001CEZ01" + 
				"\u001CDB1417052325" ;
		Prescriber p = new Prescriber(fields);
		assertEquals("03", p.getSegmentId());
		assertEquals("01", p.getPrescriberIDQualifier());
		assertEquals("1417052325", p.getPrescriberID());
	}
	@Test
	public void testCobOtherPayments() {
		String fields =   "\u001CAM05" // Segment ID
			+ "\u001C4C2"              // Detail #count 
			+ "\u001C5C01"             // Detail 1 - Coverage Type
			+ "\u001C6C99"             // Detail 1 - Payer ID Qualifier 
			+ "\u001C7C0R59377271"     // Detail 1 - Payer ID 
			+ "\u001CHB2"              // Detail 1 - Amount Paid #count
			+ "\u001CHC04"             // Detail 1 - Amount Paid 1 Qualifier
			+ "\u001CDV0000000{"       // Detail 1 - Amount Paid 1
			+ "\u001CHC07"             // Detail 1 - Amount Paid 2 Qualifier
			+ "\u001CDV0000000{"       // Detail 1 - Amount Paid 2
			+ "\u001C5E1"              // Detail 1 - Reject #count 
			+ "\u001C6E123"            // Detail 1 - Reject 1 Reject Code
			+ "\u001CNR1"              // Detail 1 - Patient Responsibility Amount #count
			+ "\u001CNP05"             // Detail 1 - Patient Responsibility Amount 1 Qualifier
			+ "\u001CNQ0000345{"       // Detail 1 - Patient Responsibility Amount 1
			+ "\u001C5C02"             // Detail 2 - Coverage Type  
			+ "\u001C6C99"             // Detail 2 - Payer ID Qualifier   
			+ "\u001C7C010299057"      // Detail 2 - Payer ID             
			+ "\u001CHB6"              // Detail 2 - Amount Paid #count
			+ "\u001CHC04"             // Detail 2 - Amount Paid 1 Qualifier
			+ "\u001CDV0000012{"       // Detail 2 - Amount Paid 1 
			+ "\u001CHC07"             // Detail 2 - Amount Paid 2 Qualifier
			+ "\u001CDV0000010I"       // Detail 2 - Amount Paid 2 
			+ "\u001CHC08"             // Detail 2 - Amount Paid 3 Qualifier
			+ "\u001CDV0000022I"       // Detail 2 - Amount Paid 3 
			+ "\u001CHC99"             // Detail 2 - Amount Paid 4 Qualifier
			+ "\u001CDV0000000{"       // Detail 2 - Amount Paid 4 
			+ "\u001CHC99"             // Detail 2 - Amount Paid 5 Qualifier
			+ "\u001CDV0000001{"       // Detail 2 - Amount Paid 5 
			+ "\u001CHC99"             // Detail 2 - Amount Paid 6 Qualifier
			+ "\u001CDV0000002{" ;     // Detail 2 - Amount Paid 6 
		COB_OtherPayments c = new COB_OtherPayments(fields);
		assertEquals("05", c.getSegmentId());
		assertEquals("2", c.getCOB_OtherPaymentsCount());
		assertEquals(2, c.getDetails().size());
		Detail detail01 = c.getDetails().get(0);
		Detail detail02 = c.getDetails().get(1);
		{ // detail 01
			assertEquals(2, detail01.getAmountsPaid().size());
			QualifiedAmount amount1 = detail01.getAmountsPaid().get(0);
			QualifiedAmount amount2 = detail01.getAmountsPaid().get(1);
			{ // detail01  AmountPaid 01
				assertEquals("04", amount1.getQualifier());
				assertEquals("0000000{", amount1.getAmount());
			}
			{ // detail01  AmountPaid 02
				assertEquals("07", amount2.getQualifier());
				assertEquals("0000000{", amount2.getAmount());
			}
			{ // detail01 Reject 01
				assertEquals(1, detail01.getRejects().size());
				assertEquals("123", detail01.getRejects().get(0).getValue());
			}
			{ // detail01 Patient Responsibility Amount 01
				assertEquals(1, detail01.getPatientResponsibilityAmounts().size());
				PatientResponsibilityAmount patientResponsibilityAmount = detail01.getPatientResponsibilityAmounts().get(0);
				assertEquals("05", patientResponsibilityAmount.getQualifier());
				assertEquals("0000345{", patientResponsibilityAmount.getAmount());
			}
		}
		{ // detail 02
			assertEquals(6, detail02.getAmountsPaid().size());
			QualifiedAmount amount1 = detail02.getAmountsPaid().get(0);
			QualifiedAmount amount2 = detail02.getAmountsPaid().get(1);
			QualifiedAmount amount3 = detail02.getAmountsPaid().get(2);
			QualifiedAmount amount4 = detail02.getAmountsPaid().get(3);
			QualifiedAmount amount5 = detail02.getAmountsPaid().get(4);
			QualifiedAmount amount6 = detail02.getAmountsPaid().get(5);
			{ // detail02  AmountPaid 01
				assertEquals("04", amount1.getQualifier());
				assertEquals("0000012{", amount1.getAmount());
			}
			{ // detail02  AmountPaid 02
				assertEquals("07", amount2.getQualifier());
				assertEquals("0000010I", amount2.getAmount());
			}
			{ // detail02  AmountPaid 03
				assertEquals("08", amount3.getQualifier());
				assertEquals("0000022I", amount3.getAmount());
			}
			{ // detail02  AmountPaid 04
				assertEquals("99", amount4.getQualifier());
				assertEquals("0000000{", amount4.getAmount());
			}
			{ // detail02  AmountPaid 05
				assertEquals("99", amount5.getQualifier());
				assertEquals("0000001{", amount5.getAmount());
			}
			{ // detail02  AmountPaid 06
				assertEquals("99", amount6.getQualifier());
				assertEquals("0000002{", amount6.getAmount());
			}
		}
	}
	@Test
	public void testPricing() {
		String fields =  "\u001CAM11"
		+ "\u001CD90000010I"
		+ "\u001CDC0000012{"
		+ "\u001CDU0000022I" ;
		Pricing p = new Pricing(fields);
		assertEquals("11", p.getSegmentId());
		assertEquals("0000010I", p.getIngredientCostSubmitted());
		assertEquals("0000012{", p.getDispensingFeeSubmitted());
		assertEquals("0000022I", p.getGrossAmountDue());
	}
	@Test
	public void testRepeatingFields() {
/*
 * Patient              (No Repeating Fields)
 * Insurance            (No Repeating Fields)
 * Claim	            Procedure Modifier Code Count                     458-SE    459-ER
 * Claim	            Submission Clarification Code Count               354-NX    420-DK
 * Pharmacy Provider    (No Repeating Fields)
 * Prescriber           (No Repeating Fields)
 * COB/Other Payments	Coordination of Benefits/Other Payments Count     337-4C    338-5C (339-6C,340-7C) 443-E8 
 * COB/Other Payments 2 Other Payer Amount Paid Count                     341-HB   (342-HC,431-DV) 
 * COB/Other Payments 2 Other Payer Reject Count                          471-5E    472-6E 
 * COB/Other Payments 2 Other Payer-Patient Responsibility Amount Count   353-NR   (351-NP,352-NQ) 
 * Worker's Comp        (No Repeating Fields)
 * DUR/PPS              Code Counter                                      473-7E   439-E4 440-E5 441-E6 474-8E (475-J9, 476-H6)
 * Pricing	            Other Amount Claimed Submitted Count              478-H7   (479-H8,480-H9) 
 * Coupon               (No Repeating Fields)
 * Compound	            Compound Ingredient Component Count               447-EC   (488-RE,489-TE) 448-ED 449-EE 490-UE
 * Clinical	            Diagnosis Code Count                              491-VE   (492-WE,424-DO) 
 * Clinical	            Clinical Info Count                               493-XE    494-ZE 495-H1  496-H2 497-H3 499-H4
	
 */
		String tests[] = {""
			, " SE1 ER>Test1"                                              //  Claim	                Procedure Modifier Code 
			, " NX1 DK>Test2"                                              //  Claim	                Submission Clarification Code
			, " 4C1 5C>Test3a 6C>Test3b 7C>Test3c E8>Test3d"               //  COB/Other Payments	    Coordination of Benefits/Other Payments
			, " 4C1 5C__ HB1 HC>Test4a DV>Test4b"                          //  COB/Other Payments 2     Other Payer Amount Paid 
			, " 4C1 5C__ 5E1 6E>Test5"                                     //  COB/Other Payments 2     Other Payer Reject
			, " 4C1 5C__ NR1 NP>Test6a NQ>Test6b"                          //  COB/Other Payments 2     Other Payer-Patient Responsibility Amount
			, " H71 H8>Test7a H9>Test7b"                                   //  Pricing	                Other Amount Claimed Submitted Count
			, " EC1 RE>Test8a TE>Test8b ED>Test8c EE>Test8d UE>Test8e"     //  Compound	                Compound Ingredient Component Count
			, " VE1 WE>Test9a DO>Test9b"                                   //  Clinical	                Diagnosis Code
			, " XE1 ZE>Test10a H1>Test10b H2>Test10c H3>Test10d H4>Test10e" //  Clinical	            Clinical Info Count
			, " 7E#Test11# E4>Test11a E5>Test11b E6>Test11c 8E>Test11d"    //  DUR/PPS
			+ " J9>Test11e H6>Test11f" 
			
		};			
		for(int i =1; i < tests.length; i++) {
			tests[i] = tests[i].replace(' ', FieldSeparator);
		}
		Claim             test1  = new Claim(tests[1]);
		Claim             test2  = new Claim(tests[2]);
		COB_OtherPayments test3  = new COB_OtherPayments(tests[3]);
		COB_OtherPayments test4  = new COB_OtherPayments(tests[4]);
		COB_OtherPayments test5  = new COB_OtherPayments(tests[5]);
		COB_OtherPayments test6  = new COB_OtherPayments(tests[6]);
		Pricing           test7  = new Pricing(tests[7]);
		Compound          test8  = new Compound(tests[8]);
		Clinical          test9  = new Clinical(tests[9]);
		Clinical          test10 = new Clinical(tests[10]);
		DUR_PPS           test11 = new DUR_PPS(tests[11]);

		assertEquals(">Test1",  test1.getProcedureModifierCodes().get(0).getValue());
		assertEquals(">Test2",  test2.getSubmissionClarificationCode().get(0).getValue());
		assertEquals(">Test3a", test3.getDetails().get(0).getOtherPayerCoverageType());
		assertEquals(">Test3b", test3.getDetails().get(0).getOtherPayerIDQualifier());
		assertEquals(">Test3c", test3.getDetails().get(0).getOtherPayerID());
		assertEquals(">Test3d", test3.getDetails().get(0).getOtherPayerDate());
		assertEquals(">Test4a", test4.getDetails().get(0).getAmountsPaid().get(0).getQualifier());
		assertEquals(">Test4b", test4.getDetails().get(0).getAmountsPaid().get(0).getAmount());
		assertEquals(">Test5",  test5.getDetails().get(0).getRejects().get(0).getValue());
		assertEquals(">Test6a", test6.getDetails().get(0).getPatientResponsibilityAmounts().get(0).getQualifier());
		assertEquals(">Test6b", test6.getDetails().get(0).getPatientResponsibilityAmounts().get(0).getAmount());
		assertEquals(">Test7a", test7.getOtherAmountsClaimedSubmitted().getQualifier());
		assertEquals(">Test7b", test7.getOtherAmountsClaimedSubmitted().getAmount());
		assertEquals(">Test8a", test8.getIngredients().get(0).getCompoundProductIDQualifier());
		assertEquals(">Test8b", test8.getIngredients().get(0).getCompoundProductID());
		assertEquals(">Test8c", test8.getIngredients().get(0).getCompoundIngredientQuantity());
		assertEquals(">Test8d", test8.getIngredients().get(0).getCompoundIngredientDrugCost());
		assertEquals(">Test8e", test8.getIngredients().get(0).getCompoundIngredientBasisofCostDetermination());
		assertEquals(">Test9a", test9.getDiagnosisCodes().get(0).getDiagnosisCodeQualifier());
		assertEquals(">Test9b", test9.getDiagnosisCodes().get(0).getDiagnosisCode());
		assertEquals(">Test10a", test10.getMeasurements().get(0).getDate());
		assertEquals(">Test10b", test10.getMeasurements().get(0).getTime());
		assertEquals(">Test10c", test10.getMeasurements().get(0).getDimension());
		assertEquals(">Test10d", test10.getMeasurements().get(0).getUnit());
		assertEquals(">Test10e", test10.getMeasurements().get(0).getValue());	
		assertEquals(">Test11a",test11.getServices().get(0).getReasonForServiceCode());
		assertEquals(">Test11b",test11.getServices().get(0).getProfessionalServiceCode());
		assertEquals(">Test11c",test11.getServices().get(0).getResultofServiceCode());
		assertEquals(">Test11d",test11.getServices().get(0).getDURPPSLevelOfEffort());
		assertEquals(">Test11e",test11.getServices().get(0).getDURCoAgentIDQualifier());
		assertEquals(">Test11f",test11.getServices().get(0).getDURCoAgentID());	
	}
	@Test
	void testTransmission() {
		String testData = "01029951B112015520341011346307600     2012010200YY      ." + 
				":AM04:C2A13680228:CCBRANDI:CDFULLAM." + 
				":AM01:C419860710:C52:C701,." + 
				":AM07:EM1:D24433298:E103:D700406035705:E70000020000:D300:D5005:D60:D80:DE20120102:DF0:C802:CW1201552034:28EA." + 
				":AM03:EZ01:DB1417052325." + 
				":AM05:4C2:5C01:6C99:7C0R59377271:HB2:HC04:DV0000000{:HC07:DV0000000{:5E1:6E123:5C02:6C99:7C010299057:HB6:HC04:DV0000012{:HC07:DV0000010I:HC08:DV0000022I:HC99:DV0000000{:HC99:DV0000000{:HC99:DV0000000{." + 
				":AM11:D90000010I:DC0000012{:DU0000022I";
		
	}

}

