package ncpdp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

import acumen.Acumen;
import acumen.InterruptedException;

import magellan.util.CSV;
import magellan.util.PipedString;
import magellan.util.StringFactory;
import ncpdp.B1.COB_OtherPayments;
import ncpdp.B1.COB_OtherPayments.Detail;
import ncpdp.B1.Claim;
import ncpdp.B1.Clinical;
import ncpdp.B1.Compound;
import ncpdp.B1.Compound.Ingredient;
import ncpdp.B1.Coupon;
import ncpdp.B1.DUR_PPS;
import ncpdp.B1.Header;
import ncpdp.B1.Insurance;
import ncpdp.B1.Patient;
import ncpdp.B1.PharmacyProvider;
import ncpdp.B1.Prescriber;
import ncpdp.B1.Pricing;
import ncpdp.B1.QualifiedAmount;
import ncpdp.B1.WorkersCompensation;
import ncpdp.Batch.Record;

public class SimpleReportB1 implements B1SegmentHandler, acumen.FileSubscriber {
	/*
	 * 
	 * 
	 */
	StringFactory joiner = new PipedString();
	int transmissionCount = 0;
	int claimCount = 0;
	static Overpunch overPunch = new Overpunch();
	static {
		overPunch.setDecimal();
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			System.out.println("syntax error: java ncpdp.SimpleReportB1 <fileName>");
			System.out.println("----- or ----------------------------------------");
			System.out.println("syntax error: java ncpdp.SimpleReportB1 <SimpleReportB1.properties>");
			System.exit(1);
		}
		if(args.length > 1 || args[0].contains(".properties")) {
			Acumen.main(args);
		}else {
	        SimpleReportB1 simpleReportB1 = new SimpleReportB1();
	        simpleReportB1.read(new File(args[0]));
		}
	}
	final static String[] fieldNames =
		{   // Transaction Header
			"TransactionHeader"
			,"BINNumber"
			,"VersionReleaseNumber"
			,"TransactionCode"
			,"ServiceProviderIDQualifier"
			,"ServiceProviderID"
			,"DateOfService"

			// Patient
			,"Patient"
			,"PatientIdQualifier" // Jun 19
			,"PatientId"          // Jun 19
			,"DateOfBirth"
			,"PatientGenderCode"
			,"PlaceOfService"     // Jun 19
			,"PatientFirstName"
			,"PatientLastName"
			,"PatientStreetAddress"
			,"PatientCityAddress"
			,"PatientStateProvinceAddress"
			,"PatientZipPostalZone"
			,"PatientPhoneNumber"
			
			//Insurance
			,"Insurance"
			,"CardholderID"
			,"CardholderFirstName"
			,"CardholderLastName"
			,"GroupID"

			// Prescriber
			,"Prescriber"
			,"PrescriberIDQualifier"
			,"PrescriberID"
			,"PrescriberLastName"
			,"PrescriberPhoneNumber"
			,"PrimaryCareProviderIDQualifier"
			,"PrimaryCareProviderID"
			,"PrimaryCareProviderLastName"
			
			// COB
			,"COB"
			,"OtherPayer01CoverageType"                 // Jun 19  Payer #1
			,"OtherPayer01IDQualifier"                  //Jun 19
			,"OtherPayer01ID"                           //Jun 19
			,"OtherPayer01Paid01Qualifier"              //Jun 19  Up to 6 paid amounts #1
			,"OtherPayer01Paid01Amount"                 //Jun 19
			,"OtherPayer01Paid02Qualifier"              //Jun 19  Up to 6 paid amounts #2
			,"OtherPayer01Paid02Amount"                 //Jun 19
			,"OtherPayer01Paid03Qualifier"              //Jun 19  Up to 6 paid amounts #3
			,"OtherPayer01Paid03Amount"                 //Jun 19
			,"OtherPayer01Paid04Qualifier"              //Jun 19  Up to 6 paid amounts #4
			,"OtherPayer01Paid04Amount"                 //Jun 19
			,"OtherPayer01Paid05Qualifier"              //Jun 19  Up to 6 paid amounts #5
			,"OtherPayer01Paid05Amount"                 //Jun 19
			,"OtherPayer01Paid06Qualifier"              //Jun 19  Up to 6 paid amounts #6
			,"OtherPayer01Paid06Amount"                 //Jun 19
			,"OtherPayer01RejectCode"                   //Jun 19
			,"OtherPayer02CoverageType"                 //Jun 19  Payer #2
			,"OtherPayer02IDQualifier"                  //Jun 19
			,"OtherPayer02ID"                           //Jun 19  
			,"OtherPayer02Paid01Qualifier"              //Jun 19  Up to 6 paid amounts #1
			,"OtherPayer02Paid01Amount"                 //Jun 19
			,"OtherPayer02Paid02Qualifier"              //Jun 19  Up to 6 paid amounts #2
			,"OtherPayer02Paid02Amount"                 //Jun 19
			,"OtherPayer02Paid03Qualifier"              //Jun 19  Up to 6 paid amounts #3
			,"OtherPayer02Paid03Amount"                 //Jun 19
			,"OtherPayer02Paid04Qualifier"              //Jun 19  Up to 6 paid amounts #4
			,"OtherPayer02Paid04Amount"                 //Jun 19
			,"OtherPayer02Paid05Qualifier"              //Jun 19  Up to 6 paid amounts #5
			,"OtherPayer02Paid05Amount"                 //Jun 19
			,"OtherPayer02Paid06Qualifier"              //Jun 19  Up to 6 paid amounts #6
			,"OtherPayer02Paid06Amount"                 //Jun 19
			,"OtherPayer02RejectCode"                   //Jun 19
//del Jun 19			,"AmountPaid01"
			
			// Claim
			,"Claim"
			,"PrescriptionServiceReferenceNumberQualifier"
			,"PrescriptionServiceReferenceNumber"
			,"ProductServiceIDQualifier"
			,"ProductServiceID"
			,"QuantityDispensed"
			,"FillNumber"    
			,"DaysSupply"    
			,"CompoundCode"
			,"DispenseAsWrittenProductSelectionCode"
			,"DatePrescriptionWritten"
			,"NumberofRefillsAuthorized"
			,"OtherCoverageCode"
			,"QuantityPrescribed"  
			,"OtherCoverageCode"  
			,"UnitDoseIndicator"  
			,"OriginallyPrescribedProductServiceIDQualifier"  
			,"OriginallyPrescribedProductServiceCode"  
			,"AlternateID"  
			,"UnitOfMeasure"
			
			//Pricing
			,"Pricing"
			,"IngredientCostSubmitted"
			,"DispensingFeeSubmitted"
			,"GrossAmountDue"
			
			// Compound
			,"Compound"
			,"CompoundProductIDQualifier01"
			,"CompoundProductID01"
			
			// Clinical
			,"Clinical"
			,"DiagnosisCode01"
			


			
			
			
	};
	String[] fields = new String[fieldNames.length];
/**********************************/	
/* Override acumen.FileSubscriber */
/**********************************/
	@Override
	public void setProperties(String keyPrefix, Properties p) throws Exception {
	}
	@Override
	public boolean read(File file) throws InterruptedException, Exception {
		for(int i = 0; i < fields.length; i++) {
			fields[i] = "";
		}
		Batch batch = new Batch();
		RecordIterator recIter;
		recIter = new DefaultRecordIterator(new RecordFactory()
		, new FileReader(file)
        ,'\u0002'
        ,'\u0003');
		int i;
		for(i = 0; recIter.hasNext(); i++) {
			Record next = recIter.next();
			System.out.println(next.getSegmentIdentifier());
		}
		System.out.println(i + " records");
		
		FileReader fileReader = new FileReader(file);
		try {
		recIter = new DefaultRecordIterator(new RecordFactory()
		, fileReader
        ,'\u0002'
        ,'\u0003');
		RecordReader recordReader = new RecordReader(recIter,new DefaultRecordHandler(new SimpleReportB1()));
        batch.read(recordReader);
		} finally {
			fileReader.close();
		}
        
		return true;
	}	
	
	
	
	String[] getFields() {
		return fields;
	}
	int getIndex(String fieldName) {
		for(int i = 0; i < fieldNames.length; i++) {
			if(fieldName.equals(fieldNames[i])) {
				return i;
			}
		}
		throw new RuntimeException(fieldName + " is not a field");
	}
	public void set(String key,String value) {
		fields[getIndex(key)] = value;
	}
	public void set(String field,int repetition, String value) {
		set(form(field, repetition),value);
	}
	public void set(String role,int roleRepetition, String field, int fieldRepetition, String value) {
		set(form(role,roleRepetition ) +  form(field, fieldRepetition),value);
	}
	public void set(String role,int roleRepetition, String field, int fieldRepetition,String subfield, int subfieldRepetition, String value) {
		set(form(role,roleRepetition ) +  form(field, fieldRepetition) + form(subfield,subfieldRepetition ),value)  ;
	}
	public static String form(String field, int repetition) {
		return repetition == 0
		? field
	    : field + String.format("%1$02d", repetition);
	}

	@Override
	public void handleHeader(Header header) {
		transmissionCount++;
		claimCount = 0;
		try {
			if(transmissionCount == 1){
				System.err.println(joiner.join(fieldNames));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        set("BINNumber",header.getBINNumber());
        set("VersionReleaseNumber",header.getVersionReleaseNumber());
        set("TransactionCode",header.getTransactionCode());
		set("ServiceProviderIDQualifier",header.getServiceProviderIDQualifier());
		set("ServiceProviderID",header.getServiceProviderID());
		set("DateOfService",header.getDateOfService());
	}

	@Override
	public void handlePatient(Patient patient) {
		set("PatientIdQualifier", patient.getPatientIDQualifier()); // Jun 18
		set("PatientId", patient.getPatientID());                   // Jun 18
		set("DateOfBirth",patient.getDateOfBirth());
		set("PatientGenderCode",patient.getPatientGenderCode());	
		set("PlaceOfService", patient.getPlaceofService());         // Jun 18
		set("PatientFirstName",patient.getPatientFirstName());
		set("PatientLastName",patient.getPatientLastName());
		set("PatientStreetAddress",patient.getPatientStreetAddress());
		set("PatientCityAddress",patient.getPatientCityAddress());
		set("PatientStateProvinceAddress",patient.getPatientStateProvinceAddress());
		set("PatientZipPostalZone",patient.getPatientZipPostalZone());
		set("PatientPhoneNumber",patient.getPatientPhoneNumber());
	}

	@Override
	public void handleInsurance(Insurance insurance) {
		set("CardholderID",insurance.getCardholderID());
		set("CardholderFirstName",insurance.getCardholderFirstName());
		set("CardholderLastName",insurance.getCardholderLastName());
		set("GroupID",insurance.getGroupID());
	}

	@Override
	public void handleClaim(Claim claim) {
		/*
		    Quantity Prescribed
			Other Coverage Code
			Unit Dose Indicator
			Originally Prescribed Product/Service ID Qualifier
			Originally Prescribed Product/Service Code
			Originally Prescribed Quantity

		 */
		closePreviousClaim();
		set("PrescriptionServiceReferenceNumberQualifier",claim.getPrescriptionServiceReferenceNumberQualifier());
		set("PrescriptionServiceReferenceNumber",claim.getPrescriptionServiceReferenceNumber());
		set("ProductServiceIDQualifier",claim.getProductServiceIDQualifier());
		set("ProductServiceID",claim.getProductServiceID());
		set("QuantityDispensed",claim.getQuantityDispensed());
		set("FillNumber",claim.getFillNumber());
		set("DaysSupply",claim.getDaysSupply());
		set("CompoundCode",claim.getCompoundCode());
		set("DispenseAsWrittenProductSelectionCode",claim.getDispenseAsWrittenProductSelectionCode());
		set("DatePrescriptionWritten",claim.getDatePrescriptionWritten());
		set("NumberofRefillsAuthorized",claim.getNumberofRefillsAuthorized());
		set("OtherCoverageCode",claim.getOtherCoverageCode());
		set("QuantityPrescribed",claim.getQuantityPrescribed());
		set("OtherCoverageCode",claim.getOtherCoverageCode());
		set("UnitDoseIndicator",claim.getUnitDoseIndicator());
		set("OriginallyPrescribedProductServiceIDQualifier",claim.getOriginallyPrescribedProductServiceIDQualifier());
		set("OriginallyPrescribedProductServiceCode",claim.getOriginallyPrescribedProductServiceCode());
		
		set("AlternateID",claim.getAlternateID());
		
		
		set("UnitOfMeasure",claim.getUnitOfMeasure());
		claimCount++;
	}
	/**
	 * 
	 */
	private void closePreviousClaim() {
		if (claimCount != 0) {
			try {
				System.err.println(joiner.join(fields));
				for(int i = 0; i < fields.length; i++) {
					fields[i] = "";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handlePharmacyProvider(PharmacyProvider pharmacyProvider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePrescriber(Prescriber prescriber) {
		/*
			Prescriber ID Qualifier
			Prescriber ID     
			Prescriber Last Name
			Prescriber Phone Number
			Primary Care Provider ID Qualifier
			Primary Care Provider ID
			Primary Care Provider Last Name
			Prescriber First Name
			Prescriber Address
			Prescriber City
			Prescriber State
			Prescriber ZIP Code
		 */
		
		set("PrescriberIDQualifier",prescriber.getPrescriberIDQualifier());
		set("PrescriberID",prescriber.getPrescriberID());
		set("PrescriberLastName",prescriber.getPrescriberLastName());
		set("PrescriberPhoneNumber",prescriber.getPrescriberPhoneNumber());
		set("PrimaryCareProviderIDQualifier",prescriber.getPrimaryCareProviderIDQualifier());
		set("PrimaryCareProviderID",prescriber.getPrimaryCareProviderID());
		set("PrimaryCareProviderLastName",prescriber.getPrimaryCareProviderLastName());		

		
		
	}

	@Override
	public void handleCOB_OtherPayments(COB_OtherPayments cobOtherPayments) {
		try {
			int otherPayerCount = 0;
			for(Detail detail : cobOtherPayments.getDetails() ) {
			    otherPayerCount++;
				set("OtherPayer", otherPayerCount,"CoverageType",0, detail.getOtherPayerCoverageType()); //Jun 18
				set("OtherPayer", otherPayerCount,"IDQualifier", 0, detail.getOtherPayerIDQualifier());   //Jun 18
				set("OtherPayer", otherPayerCount,"ID",0, detail.getOtherPayerID());                     //Jun 18 
	//		    QualifiedAmount qualifiedAmount = detail.getAmountsPaid().get(0);
	//		    Field reject;
	//		    if(detail.getRejects().size() > 0) {
	//		    	reject = detail.getRejects().get(0);
	//		    }else {
	//		    	reject = new Field("XX");
	//		    }
	//		    set("Reject",reject.getValue());
	
	//		    set("OtherPayerIDQualifier",detail.getOtherPayerIDQualifier());
	//		    set("OtherPayerID",detail.getOtherPayerID());
				int paidCount = 0;
			    for(QualifiedAmount qualifiedAmount : detail.getAmountsPaid()) {
			    	paidCount++;
				    set("OtherPayer", otherPayerCount,"Paid",paidCount,"Qualifier",0,qualifiedAmount.getQualifier());   //Jun 18 
				    set("OtherPayer", otherPayerCount,"Paid",paidCount,"Amount",0,overPunch.toString(qualifiedAmount.getAmount()));         //Jun 18
			    }
			    int rejectCount = 0;
			    for(Field reject : detail.getRejects()) {
			    	rejectCount++;
			    	set("OtherPayer", otherPayerCount,"RejectCode",0, reject.getValue());
			    }
			    //ver 1set("AmountPaid01",qualifiedAmount.getAmount());  
			    
			}
		    
		}catch(Exception ignore) {
			ignore.printStackTrace(System.out);
		}
		
	}

	@Override
	public void handleWorkersCompensation(
			WorkersCompensation workersCompensation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDUR_PPS(DUR_PPS dur_pps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePricing(Pricing pricing) {
		set("IngredientCostSubmitted", pricing.getIngredientCostSubmitted());
		set("DispensingFeeSubmitted", pricing.getDispensingFeeSubmitted());
		set("GrossAmountDue", overPunch.toString(pricing.getGrossAmountDue()));
		
	}

	@Override
	public void handleCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Compound Product ID Qualifier
     *  Compound Product ID   

	 */
	@Override
	public void handleCompound(Compound compound) {
		if(compound.getIngredients().size() > 0) {
		    Ingredient ingredient = compound.getIngredients().get(0);
			set("CompoundProductIDQualifier01",ingredient.getCompoundProductIDQualifier() );
			set("CompoundProductID01",ingredient.getCompoundProductID());
		}
		
	}

	/*Diagnosis Code*/

	@Override
	public void handleClinical(Clinical clinical) {
		if(clinical.getDiagnosisCodes().size() > 0 ) {
		    set("DiagnosisCode01",clinical.getDiagnosisCodes().get(0).getDiagnosisCode());
		}
		
	}

	@Override
	public void startB1() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endB1() {
		closePreviousClaim();
		claimCount = 0;
	}

}
