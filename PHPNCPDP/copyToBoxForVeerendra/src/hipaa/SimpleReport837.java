/**
 * 
 */
package hipaa;









import hipaa4010.C023;
import hipaa4010.Svd;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magellan.math.StringMath;
import magellan.util.PipedString;
import magellan.util.StringFactory;
import x12.DefaultSegmentIterator;
import x12.SegmentIterator;
import x12.X12Reader;
import acumen.Acumen;
import acumen.FileSubscriber;
import acumen.InterruptedException;

/**
 * @author tjkelly
 *
 */
public class SimpleReport837 extends DefaultHandler837 implements FileSubscriber {
	/*
	 * 
		,"XFirstName"    // X
		,"XMiddleName"
		,"XLastName"
		,"XId"
		,"XAddr1"
		,"XAddr2"
		,"XCity"
		,"XState"
		,"XZip"
		,"XDateofBirth"
		,"XGender"
	 */
	
	
	static final String END2400 = "End2400";
	static final String TRANSACTION_HEADER = "TransactionHeader";
	static final String F2400 = "2400";
	static final String F2300 = "2300";
	static final String F2000B = "2000B";
	static final String F2000C = "2000C";
	static final String F2000A = "2000A";
	
	
	final static String[] fieldNames =
	{   // Transaction Header
		TRANSACTION_HEADER
		,"SubmitterFirstName"          // 1000A
		,"SubmitterMiddleName"
		,"SubmitterLastName"
//		,"ReceiverFirstName"           // 1000B Receiver
//		,"ReceiverMiddleName"
//		,"ReceiverLastName"
		,"Receiver"
		,F2000A
		,"BillingProviderFirstName"    // 2010aa BillingProvider
		,"BillingProviderMiddleName"
		,"BillingProviderLastName"
		,"BillingProviderId"
		,"BillingProviderAddr1"
		,"BillingProviderAddr2"
		,"BillingProviderCity"
		,"BillingProviderState"
		,"BillingProviderZip"
		,"BillingProviderTaxIdNumber"
		,"BillingProviderTelephoneNumber"
		,"BillingProviderFaxNumber"
		,"PayToProviderFirstName"      // 2010ab PayToProvider
		,"PayToProviderMiddleName"
		,"PayToProviderLastName"
		,"PayToProviderId"
		,"PayToProviderAddr1"
		,"PayToProviderAddr2"
		,"PayToProviderCity"
		,"PayToProviderState"
		,"PayToProviderZip"
		,F2000B
		,"SubscriberFirstName"    // 2010ba Subscriber
		,"SubscriberMiddleName"
		,"SubscriberLastName"
		,"SubscriberId"
		,"SubscriberAddr1"
		,"SubscriberAddr2"
		,"SubscriberCity"
		,"SubscriberState"
		,"SubscriberZip"
		,"SubscriberDateOfBirth"
		,"SubscriberGender"
		,"PlanType"
		,F2000C
		,"PatientFirstName"        // 2010ca Patient
		,"PatientMiddleName"
		,"PatientLastName"
		,"PatientId"
		,"PatientAddr1"
		,"PatientAddr2"
		,"PatientCity"
		,"PatientState"
		,"PatientZip"
		,"PatientDateOfBirth"
		,"PatientGender"
		,"PatientRelationshipToSubscriber"
		,F2300                 
		,"PatientAccount#"                     //2300 Claim
		,"TotalClaimChargeAmount"    
		,"FacilityTypeCode"
		,"ClaimSubmissionReasonCode"                   
		,"StatementFrom"
		,"StatementTo"
		,"InitialTreatmentDate"
		,"RelatedHospitalizationAdmissionDate"
		,"RelatedHospitalizationAdmissionDateAndHour"
		,"RelatedHospitalizationDischargeDate"
		,"RelatedHospitalizationDischargeHour"
		,"TypeOfAdmission"
		,"SourceOfAdmission"
		,"MedicalRecord#"
		,"DiagnosisCode01"
		,"DiagnosisCode02"
		,"DiagnosisCode03"
		,"DiagnosisCode04"
		,"DiagnosisCode05"
		,"DiagnosisCode06"
		,"DiagnosisCode07"
		,"DiagnosisCode08"
		,"DiagnosisCode09"
		,"DiagnosisCode10"
		,"DiagnosisCode11"
		,"DiagnosisCode12"
		,"DiagnosisCode13"
		,"DiagnosisRelatedGroup"
		,"IcdProcedure01"
		,"IcdProcedure02"
		,"IcdProcedure03"
		,"IcdProcedure04"
		,"IcdProcedure05"
		,"IcdProcedure06"
		,"IcdProcedure07"
		,"IcdProcedure08"
		,"IcdProcedure09"
		,"IcdProcedure10"
		,"IcdProcedure11"
		,"IcdProcedure12"
		,"IcdProcedure13"
		,"ConditionCode01"
		,"ConditionCode02"
		,"ConditionCode03"
		,"ConditionCode04"
		,"ConditionCode05"
		,"ConditionCode06"
		,"ConditionCode07"
		,"ConditionCode08"
		,"ConditionCode09"
		,"ConditionCode10"
		,"ConditionCode11"
		,"ConditionCode12"
		,"ReferringProviderFirstName"    // 2310a ReferringProvider
		,"ReferringProviderMiddleName"
		,"ReferringProviderLastName"
		,"ReferringProviderId"
		,"AttendingProviderFirstName"    // 2310a AttendingProvider
		,"AttendingProviderMiddleName"
		,"AttendingProviderLastName"
		,"AttendingProviderId"
		,"AttendingProviderTaxonomy"
		,"RenderingProviderFirstName"    // 2310b/2310d RenderingProvider
		,"RenderingProviderMiddleName"
		,"RenderingProviderLastName"
		,"RenderingProviderId"
		,"RenderingProviderTaxonomy"
		,"OperatingProviderFirstName"    // 2310b OperatingProvider
		,"OperatingProviderMiddleName"
		,"OperatingProviderLastName"
		,"OperatingProviderId"
		,"PurchasedServiceProviderFirstName"    // 2310c PurchasedServiceProvider
		,"PurchasedServiceProviderMiddleName"
		,"PurchasedServiceProviderLastName"
		,"PurchasedServiceProviderId"
		,"ServiceFacilityLocationFirstName"    // 2310c/2310d/2310e ServiceFacilityLocation
		,"ServiceFacilityLocationMiddleName"
		,"ServiceFacilityLocationLastName"
		,"ServiceFacilityLocationId"
		,"ServiceFacilityLocationAddr1"
		,"ServiceFacilityLocationAddr2"
		,"ServiceFacilityLocationCity"
		,"ServiceFacilityLocationState"
		,"ServiceFacilityLocationZip"
		,"OtherProviderFirstName"    // 2310c OtherProvider
		,"OtherProviderMiddleName"
		,"OtherProviderLastName"
		,"OtherProviderId"
		,"OtherOperatingPhysicianFirstName"    // 2310c OtherOperatingPhysician
		,"OtherOperatingPhysicianMiddleName"
		,"OtherOperatingPhysicianLastName"
		,"OtherOperatingPhysicianId"
		,"SupervisingProviderFirstName"    // 2310d/2310e SupervisingProvider
		,"SupervisingProviderMiddleName"
		,"SupervisingProviderLastName"
		,"SupervisingProviderId"
		,"OtherSubscriber01Group#"     //2320 Other Subscriber #1
		,"OtherSubscriber01Group"
		,"OtherSubscriber01Paid"
		,"OtherSubscriber01Allowed"
		,"OtherSubscriber01NonCovered"
		,"OtherSubscriber01ClaimPaidDate"
		,"OtherSubscriber01Deductible"
		,"OtherSubscriber01CoInsurance"
		,"OtherSubscriber01CoPay"
		,"OtherSubscriber01AmountNotCovered"
		,"OtherSubscriber02Group#"     //2320 Other Subscriber #2
		,"OtherSubscriber02Group"
		,"OtherSubscriber02Paid"
		,"OtherSubscriber02Allowed"
		,"OtherSubscriber02NonCovered"
		,"OtherSubscriber02ClaimPaidDate"
		,"OtherSubscriber02Deductible"
		,"OtherSubscriber02CoInsurance"
		,"OtherSubscriber02CoPay"
		,"OtherSubscriber02AmountNotCovered"
		,"OtherSubscriber03Group#"     //2320 Other Subscriber #3
		,"OtherSubscriber03Group"
		,"OtherSubscriber03Paid"
		,"OtherSubscriber03Allowed"
		,"OtherSubscriber03NonCovered"
		,"OtherSubscriber03ClaimPaidDate"
		,"OtherSubscriber03Deductible"
		,"OtherSubscriber03CoInsurance"
		,"OtherSubscriber03CoPay"
		,"OtherSubscriber03AmountNotCovered"
		,"OtherSubscriber04Group#"     //2320 Other Subscriber #4
		,"OtherSubscriber04Group"
		,"OtherSubscriber04Paid"
		,"OtherSubscriber04Allowed"
		,"OtherSubscriber04NonCovered"
		,"OtherSubscriber04ClaimPaidDate"
		,"OtherSubscriber04Deductible"
		,"OtherSubscriber04CoInsurance"
		,"OtherSubscriber04CoPay"
		,"OtherSubscriber04AmountNotCovered"
		,"OtherSubscriber05Group#"     //2320 Other Subscriber #5
		,"OtherSubscriber05Group"
		,"OtherSubscriber05Paid"
		,"OtherSubscriber05Allowed"
		,"OtherSubscriber05NonCovered"
		,"OtherSubscriber05ClaimPaidDate"
		,"OtherSubscriber05Deductible"
		,"OtherSubscriber05CoInsurance"
		,"OtherSubscriber05CoPay"
		,"OtherSubscriber05AmountNotCovered"
		,"OtherSubscriber06Group#"     //2320 Other Subscriber #6
		,"OtherSubscriber06Group"
		,"OtherSubscriber06Paid"
		,"OtherSubscriber06Allowed"
		,"OtherSubscriber06NonCovered"
		,"OtherSubscriber06ClaimPaidDate"
		,"OtherSubscriber06Deductible"
		,"OtherSubscriber06CoInsurance"
		,"OtherSubscriber06CoPay"
		,"OtherSubscriber06AmountNotCovered"
		,"OtherSubscriber07Group#"     //2320 Other Subscriber #7
		,"OtherSubscriber07Group"
		,"OtherSubscriber07Paid"
		,"OtherSubscriber07Allowed"
		,"OtherSubscriber07NonCovered"
		,"OtherSubscriber07ClaimPaidDate"
		,"OtherSubscriber07Deductible"
		,"OtherSubscriber07CoInsurance"
		,"OtherSubscriber07CoPay"
		,"OtherSubscriber07AmountNotCovered"
		,"OtherSubscriber08Group#"     //2320 Other Subscriber #8
		,"OtherSubscriber08Group"
		,"OtherSubscriber08Paid"
		,"OtherSubscriber08Allowed"
		,"OtherSubscriber08NonCovered"
		,"OtherSubscriber08ClaimPaidDate"
		,"OtherSubscriber08Deductible"
		,"OtherSubscriber08CoInsurance"
		,"OtherSubscriber08CoPay"
		,"OtherSubscriber08AmountNotCovered"
		,"OtherSubscriber09Group#"     //2320 Other Subscriber #9
		,"OtherSubscriber09Group"
		,"OtherSubscriber09Paid"
		,"OtherSubscriber09Allowed"
		,"OtherSubscriber09NonCovered"
		,"OtherSubscriber09ClaimPaidDate"
		,"OtherSubscriber09Deductible"
		,"OtherSubscriber09CoInsurance"
		,"OtherSubscriber09CoPay"
		,"OtherSubscriber09AmountNotCovered"
		,"OtherSubscriber10Group#"     //2320 Other Subscriber #10
		,"OtherSubscriber10Group"
		,"OtherSubscriber10Paid"
		,"OtherSubscriber10Allowed"
		,"OtherSubscriber10NonCovered"
		,"OtherSubscriber10ClaimPaidDate"
		,"OtherSubscriber10Deductible"
		,"OtherSubscriber10CoInsurance"
		,"OtherSubscriber10CoPay"
		,F2400
		,"Assigned#"                   // 2400  Service Line
		,"RevenueCode"
		,"Product/SvcId"
		,"ProcedureModifier1"
		,"ProcedureModifier2"
		,"ProcedureModifier3"
		,"ProcedureModifier4"
		,"ServiceUnitCount"
		,"PlaceOfService"
		,"ProcedureCode"
		,"ServiceDateFrom"
		,"ServiceDateTo"
		,"TestingDate"
		,"InitialTreatmentDate2"
		,"PriorAuthorization#1"
		,"PriorAuthorization#2"
		,"SalesTaxAmount"               // AMT
		,"ApprovedAmount"      
		,"PostageClaimedAmount"
		,"PricingProduct/SvcId"
		,"Drug01Code"                   // 2410 Drug Identification #01  
		,"Drug01UnitPrice"
		,"Drug01UnitCount"
		,"Drug01UnitOfMeasure"
		,"Drug02Code"                   // 2410 Drug Identification #02  
		,"Drug02UnitPrice"
		,"Drug02UnitCount"
		,"Drug02UnitOfMeasure"
		,"Drug03Code"                   // 2410 Drug Identification #03  
		,"Drug03UnitPrice"
		,"Drug03UnitCount"
		,"Drug03UnitOfMeasure"
		,"Drug04Code"                   // 2410 Drug Identification #04  
		,"Drug04UnitPrice"
		,"Drug04UnitCount"
		,"Drug04UnitOfMeasure"
		,"Drug05Code"                   // 2410 Drug Identification #05  
		,"Drug05UnitPrice"
		,"Drug05UnitCount"
		,"Drug05UnitOfMeasure"
		,"Drug06Code"                   // 2410 Drug Identification #06  
		,"Drug06UnitPrice"
		,"Drug06UnitCount"
		,"Drug06UnitOfMeasure"
		,"Drug07Code"                   // 2410 Drug Identification #07  
		,"Drug07UnitPrice"
		,"Drug07UnitCount"
		,"Drug07UnitOfMeasure"
		,"Drug08Code"                   // 2410 Drug Identification #08  
		,"Drug08UnitPrice"
		,"Drug08UnitCount"
		,"Drug08UnitOfMeasure"
		,"Drug09Code"                   // 2410 Drug Identification #09  
		,"Drug09UnitPrice"
		,"Drug09UnitCount"
		,"Drug09UnitOfMeasure"
		,"Drug10Code"                   // 2410 Drug Identification #10  
		,"Drug10UnitPrice"
		,"Drug10UnitCount"
		,"Drug10UnitOfMeasure"
		,"Drug11Code"                   // 2410 Drug Identification #11  
		,"Drug11UnitPrice"
		,"Drug11UnitCount"
		,"Drug11UnitOfMeasure"
		,"Drug12Code"                   // 2410 Drug Identification #12  
		,"Drug12UnitPrice"
		,"Drug12UnitCount"
		,"Drug12UnitOfMeasure"
		,"Drug13Code"                   // 2410 Drug Identification #13  
		,"Drug13UnitPrice"
		,"Drug13UnitCount"
		,"Drug13UnitOfMeasure"
		,"Drug14Code"                   // 2410 Drug Identification #14  
		,"Drug14UnitPrice"
		,"Drug14UnitCount"
		,"Drug14UnitOfMeasure"
		,"Drug15Code"                   // 2410 Drug Identification #15  
		,"Drug15UnitPrice"
		,"Drug15UnitCount"
		,"Drug15UnitOfMeasure"
		,"Drug16Code"                   // 2410 Drug Identification #16  
		,"Drug16UnitPrice"
		,"Drug16UnitCount"
		,"Drug16UnitOfMeasure"
		,"Drug17Code"                   // 2410 Drug Identification #17  
		,"Drug17UnitPrice"
		,"Drug17UnitCount"
		,"Drug17UnitOfMeasure"
		,"Drug18Code"                   // 2410 Drug Identification #18  
		,"Drug18UnitPrice"
		,"Drug18UnitCount"
		,"Drug18UnitOfMeasure"
		,"Drug19Code"                   // 2410 Drug Identification #19  
		,"Drug19UnitPrice"
		,"Drug19UnitCount"
		,"Drug19UnitOfMeasure"
		,"Drug20Code"                   // 2410 Drug Identification #20  
		,"Drug20UnitPrice"
		,"Drug20UnitCount"
		,"Drug20UnitOfMeasure"
		,"Drug21Code"                   // 2410 Drug Identification #21  
		,"Drug21UnitPrice"
		,"Drug21UnitCount"
		,"Drug21UnitOfMeasure"
		,"Drug22Code"                   // 2410 Drug Identification #22  
		,"Drug22UnitPrice"
		,"Drug22UnitCount"
		,"Drug22UnitOfMeasure"
		,"Drug23Code"                   // 2410 Drug Identification #23  
		,"Drug23UnitPrice"
		,"Drug23UnitCount"
		,"Drug23UnitOfMeasure"
		,"Drug24Code"                   // 2410 Drug Identification #24  
		,"Drug24UnitPrice"
		,"Drug24UnitCount"
		,"Drug24UnitOfMeasure"
		,"Drug25Code"                   // 2410 Drug Identification #25  
		,"Drug25UnitPrice"
		,"Drug25UnitCount"
		,"Drug25UnitOfMeasure"
		,"RenderingProvider2FirstName"    // 2420a RenderingProvider2
		,"RenderingProvider2MiddleName"
		,"RenderingProvider2LastName"
		,"RenderingProvider2Id"
		,"AttendingPhysician2FirstName"    // 2420a AttendingPhysician2
		,"AttendingPhysician2MiddleName"
		,"AttendingPhysician2LastName"
		,"AttendingPhysician2Id"
		,"OperatingingPhysician2FirstName"    // 2420b OperatingingPhysician2
		,"OperatingingPhysician2MiddleName"
		,"OperatingingPhysician2LastName"
		,"OperatingingPhysician2Id"
		,"ServiceFacilityLocation2FirstName"    // 2420c ServiceFacilityLocation2
		,"ServiceFacilityLocation2MiddleName"
		,"ServiceFacilityLocation2LastName"
		,"ServiceFacilityLocation2Id"
		,"ServiceFacilityLocation2Addr1"
		,"ServiceFacilityLocation2Addr2"
		,"ServiceFacilityLocation2City"
		,"ServiceFacilityLocation2State"
		,"ServiceFacilityLocation2Zip"
		,"OtherProvider2FirstName"    // 2420c OtherProvider2
		,"OtherProvider2MiddleName"
		,"OtherProvider2LastName"
		,"OtherProvider2Id"

		,"OrderingProvider2FirstName"    // 2420e OrderingProvider2
		,"OrderingProvider2MiddleName"
		,"OrderingProvider2LastName"
		,"OrderingProvider2Id"

		,"ReferringProvider2FirstName"    // 2420f ReferringProvider2
		,"ReferringProvider2MiddleName"
		,"ReferringProvider2LastName"
		,"ReferringProvider2Id"

		,"ReferringProvider3FirstName"    // 2420f ReferringProvider3
		,"ReferringProvider3MiddleName"
		,"ReferringProvider3LastName"
		,"ReferringProvider3Id"

		,"OtherPayerPriorAuthorization01"  //2420g Other Payer Prior Authorization
		,"OtherPayerPriorAuthorization02"
		,"OtherPayerPriorAuthorization03"
		,"OtherPayerPriorAuthorization04"
		// 2430 Line Adjudication

		,"Adjudication01PayerId"
		,"Adjudication01PaidAmount"
		,"Adjudication01ProcedureCode"
		,"Adjudication01ProcedureModifier01"
		,"Adjudication01ProcedureModifier02"
		,"Adjudication01ProcedureModifier03"
		,"Adjudication01ProcedureModifier04"
		,"Adjudication01AmountNonCovered"
		,"Adjudication01Deductible"
		,"Adjudication01CoInsurance"
		,"Adjudication01CoPay"
		,"Adjudication02PayerId"
		,"Adjudication02PaidAmount"
		,"Adjudication02ProcedureCode"
		,"Adjudication02ProcedureModifier01"
		,"Adjudication02ProcedureModifier02"
		,"Adjudication02ProcedureModifier03"
		,"Adjudication02ProcedureModifier04"
		,"Adjudication02AmountNonCovered"
		,"Adjudication02Deductible"
		,"Adjudication02CoInsurance"
		,"Adjudication02CoPay"
		,"Adjudication03PayerId"
		,"Adjudication03PaidAmount"
		,"Adjudication03ProcedureCode"
		,"Adjudication03ProcedureModifier01"
		,"Adjudication03ProcedureModifier02"
		,"Adjudication03ProcedureModifier03"
		,"Adjudication03ProcedureModifier04"
		,"Adjudication03AmountNonCovered"
		,"Adjudication03Deductible"
		,"Adjudication03CoInsurance"
		,"Adjudication03CoPay"
		,"Adjudication04PayerId"
		,"Adjudication04PaidAmount"
		,"Adjudication04ProcedureCode"
		,"Adjudication04ProcedureModifier01"
		,"Adjudication04ProcedureModifier02"
		,"Adjudication04ProcedureModifier03"
		,"Adjudication04ProcedureModifier04"
		,"Adjudication04AmountNonCovered"
		,"Adjudication04Deductible"
		,"Adjudication04CoInsurance"
		,"Adjudication04CoPay"
		,"Adjudication05PayerId"
		,"Adjudication05PaidAmount"
		,"Adjudication05ProcedureCode"
		,"Adjudication05ProcedureModifier01"
		,"Adjudication05ProcedureModifier02"
		,"Adjudication05ProcedureModifier03"
		,"Adjudication05ProcedureModifier04"
		,"Adjudication05AmountNonCovered"
		,"Adjudication05Deductible"
		,"Adjudication05CoInsurance"
		,"Adjudication05CoPay"
		,"Adjudication06PayerId"
		,"Adjudication06PaidAmount"
		,"Adjudication06ProcedureCode"
		,"Adjudication06ProcedureModifier01"
		,"Adjudication06ProcedureModifier02"
		,"Adjudication06ProcedureModifier03"
		,"Adjudication06ProcedureModifier04"
		,"Adjudication06AmountNonCovered"
		,"Adjudication06Deductible"
		,"Adjudication06CoInsurance"
		,"Adjudication06CoPay"
		,"Adjudication07PayerId"
		,"Adjudication07PaidAmount"
		,"Adjudication07ProcedureCode"
		,"Adjudication07ProcedureModifier01"
		,"Adjudication07ProcedureModifier02"
		,"Adjudication07ProcedureModifier03"
		,"Adjudication07ProcedureModifier04"
		,"Adjudication07AmountNonCovered"
		,"Adjudication07Deductible"
		,"Adjudication07CoInsurance"
		,"Adjudication07CoPay"
		,"Adjudication08PayerId"
		,"Adjudication08PaidAmount"
		,"Adjudication08ProcedureCode"
		,"Adjudication08ProcedureModifier01"
		,"Adjudication08ProcedureModifier02"
		,"Adjudication08ProcedureModifier03"
		,"Adjudication08ProcedureModifier04"
		,"Adjudication08AmountNonCovered"
		,"Adjudication08Deductible"
		,"Adjudication08CoInsurance"
		,"Adjudication08CoPay"
		,"Adjudication09PayerId"
		,"Adjudication09PaidAmount"
		,"Adjudication09ProcedureCode"
		,"Adjudication09ProcedureModifier01"
		,"Adjudication09ProcedureModifier02"
		,"Adjudication09ProcedureModifier03"
		,"Adjudication09ProcedureModifier04"
		,"Adjudication09AmountNonCovered"
		,"Adjudication09Deductible"
		,"Adjudication09CoInsurance"
		,"Adjudication09CoPay"
		,"Adjudication10PayerId"
		,"Adjudication10PaidAmount"
		,"Adjudication10ProcedureCode"
		,"Adjudication10ProcedureModifier01"
		,"Adjudication10ProcedureModifier02"
		,"Adjudication10ProcedureModifier03"
		,"Adjudication10ProcedureModifier04"
		,"Adjudication10AmountNonCovered"
		,"Adjudication10Deductible"
		,"Adjudication10CoInsurance"
		,"Adjudication10CoPay"
		,"Adjudication11PayerId"
		,"Adjudication11PaidAmount"
		,"Adjudication11ProcedureCode"
		,"Adjudication11ProcedureModifier01"
		,"Adjudication11ProcedureModifier02"
		,"Adjudication11ProcedureModifier03"
		,"Adjudication11ProcedureModifier04"
		,"Adjudication11AmountNonCovered"
		,"Adjudication11Deductible"
		,"Adjudication11CoInsurance"
		,"Adjudication11CoPay"
		,"Adjudication12PayerId"
		,"Adjudication12PaidAmount"
		,"Adjudication12ProcedureCode"
		,"Adjudication12ProcedureModifier01"
		,"Adjudication12ProcedureModifier02"
		,"Adjudication12ProcedureModifier03"
		,"Adjudication12ProcedureModifier04"
		,"Adjudication12AmountNonCovered"
		,"Adjudication12Deductible"
		,"Adjudication12CoInsurance"
		,"Adjudication12CoPay"
		,"Adjudication13PayerId"
		,"Adjudication13PaidAmount"
		,"Adjudication13ProcedureCode"
		,"Adjudication13ProcedureModifier01"
		,"Adjudication13ProcedureModifier02"
		,"Adjudication13ProcedureModifier03"
		,"Adjudication13ProcedureModifier04"
		,"Adjudication13AmountNonCovered"
		,"Adjudication13Deductible"
		,"Adjudication13CoInsurance"
		,"Adjudication13CoPay"
		,"Adjudication14PayerId"
		,"Adjudication14PaidAmount"
		,"Adjudication14ProcedureCode"
		,"Adjudication14ProcedureModifier01"
		,"Adjudication14ProcedureModifier02"
		,"Adjudication14ProcedureModifier03"
		,"Adjudication14ProcedureModifier04"
		,"Adjudication14AmountNonCovered"
		,"Adjudication14Deductible"
		,"Adjudication14CoInsurance"
		,"Adjudication14CoPay"
		,"Adjudication15PayerId"
		,"Adjudication15PaidAmount"
		,"Adjudication15ProcedureCode"
		,"Adjudication15ProcedureModifier01"
		,"Adjudication15ProcedureModifier02"
		,"Adjudication15ProcedureModifier03"
		,"Adjudication15ProcedureModifier04"
		,"Adjudication15AmountNonCovered"
		,"Adjudication15Deductible"
		,"Adjudication15CoInsurance"
		,"Adjudication15CoPay"
		,"Adjudication16PayerId"
		,"Adjudication16PaidAmount"
		,"Adjudication16ProcedureCode"
		,"Adjudication16ProcedureModifier01"
		,"Adjudication16ProcedureModifier02"
		,"Adjudication16ProcedureModifier03"
		,"Adjudication16ProcedureModifier04"
		,"Adjudication16AmountNonCovered"
		,"Adjudication16Deductible"
		,"Adjudication16CoInsurance"
		,"Adjudication16CoPay"
		,"Adjudication17PayerId"
		,"Adjudication17PaidAmount"
		,"Adjudication17ProcedureCode"
		,"Adjudication17ProcedureModifier01"
		,"Adjudication17ProcedureModifier02"
		,"Adjudication17ProcedureModifier03"
		,"Adjudication17ProcedureModifier04"
		,"Adjudication17AmountNonCovered"
		,"Adjudication17Deductible"
		,"Adjudication17CoInsurance"
		,"Adjudication17CoPay"
		,"Adjudication18PayerId"
		,"Adjudication18PaidAmount"
		,"Adjudication18ProcedureCode"
		,"Adjudication18ProcedureModifier01"
		,"Adjudication18ProcedureModifier02"
		,"Adjudication18ProcedureModifier03"
		,"Adjudication18ProcedureModifier04"
		,"Adjudication18AmountNonCovered"
		,"Adjudication18Deductible"
		,"Adjudication18CoInsurance"
		,"Adjudication18CoPay"
		,"Adjudication19PayerId"
		,"Adjudication19PaidAmount"
		,"Adjudication19ProcedureCode"
		,"Adjudication19ProcedureModifier01"
		,"Adjudication19ProcedureModifier02"
		,"Adjudication19ProcedureModifier03"
		,"Adjudication19ProcedureModifier04"
		,"Adjudication19AmountNonCovered"
		,"Adjudication19Deductible"
		,"Adjudication19CoInsurance"
		,"Adjudication19CoPay"
		,"Adjudication20PayerId"
		,"Adjudication20PaidAmount"
		,"Adjudication20ProcedureCode"
		,"Adjudication20ProcedureModifier01"
		,"Adjudication20ProcedureModifier02"
		,"Adjudication20ProcedureModifier03"
		,"Adjudication20ProcedureModifier04"
		,"Adjudication20AmountNonCovered"
		,"Adjudication20Deductible"
		,"Adjudication20CoInsurance"
		,"Adjudication20CoPay"
		,"Adjudication21PayerId"
		,"Adjudication21PaidAmount"
		,"Adjudication21ProcedureCode"
		,"Adjudication21ProcedureModifier01"
		,"Adjudication21ProcedureModifier02"
		,"Adjudication21ProcedureModifier03"
		,"Adjudication21ProcedureModifier04"
		,"Adjudication21AmountNonCovered"
		,"Adjudication21Deductible"
		,"Adjudication21CoInsurance"
		,"Adjudication21CoPay"
		,"Adjudication22PayerId"
		,"Adjudication22PaidAmount"
		,"Adjudication22ProcedureCode"
		,"Adjudication22ProcedureModifier01"
		,"Adjudication22ProcedureModifier02"
		,"Adjudication22ProcedureModifier03"
		,"Adjudication22ProcedureModifier04"
		,"Adjudication22AmountNonCovered"
		,"Adjudication22Deductible"
		,"Adjudication22CoInsurance"
		,"Adjudication22CoPay"
		,"Adjudication23PayerId"
		,"Adjudication23PaidAmount"
		,"Adjudication23ProcedureCode"
		,"Adjudication23ProcedureModifier01"
		,"Adjudication23ProcedureModifier02"
		,"Adjudication23ProcedureModifier03"
		,"Adjudication23ProcedureModifier04"
		,"Adjudication23AmountNonCovered"
		,"Adjudication23Deductible"
		,"Adjudication23CoInsurance"
		,"Adjudication23CoPay"
		,"Adjudication24PayerId"
		,"Adjudication24PaidAmount"
		,"Adjudication24ProcedureCode"
		,"Adjudication24ProcedureModifier01"
		,"Adjudication24ProcedureModifier02"
		,"Adjudication24ProcedureModifier03"
		,"Adjudication24ProcedureModifier04"
		,"Adjudication24AmountNonCovered"
		,"Adjudication24Deductible"
		,"Adjudication24CoInsurance"
		,"Adjudication24CoPay"
		,"Adjudication25PayerId"
		,"Adjudication25PaidAmount"
		,"Adjudication25ProcedureCode"
		,"Adjudication25ProcedureModifier01"
		,"Adjudication25ProcedureModifier02"
		,"Adjudication25ProcedureModifier03"
		,"Adjudication25ProcedureModifier04"
		,"Adjudication25AmountNonCovered"
		,"Adjudication25Deductible"
		,"Adjudication25CoInsurance"
		,"Adjudication25CoPay"
				,END2400
		
	};
	int counter2320;
    String[] fields = new String[fieldNames.length];
    StringFactory joiner = new PipedString();
   
	
	/**
	 * 
	 */
	public SimpleReport837() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			System.out.println("syntax error: ");
			System.out.println("java hipaa.SimpleReport837 <fileName>");
			System.out.println("----- or ----------------------------------------");
			System.out.println("java hipaa.SimpleReport837 <SimpleReport837.properties>");
			System.exit(1);
		}
		if(args.length > 1 || args[0].contains(".properties")) {
			Acumen.main(args);
		}else {
	        SimpleReport837 simpleReport837 = new SimpleReport837();
	        simpleReport837.read(new File(args[0]));
		}

	}
	/* (non-Javadoc)
	 * @see acumen.FileSubscriber#setProperties(java.lang.String, java.util.Properties)
	 */
	@Override
	public void setProperties(String keyPrefix, Properties p) throws Exception {
		

	}
	/* (non-Javadoc)
	 * @see acumen.FileSubscriber#read(java.io.File)
	 */
	@Override
	public boolean read(File file) throws InterruptedException, Exception {
		System.err.println(joiner.join(fieldNames));
	
		FileReader fileReader = new FileReader(file);
		try {
			SegmentIterator iterator = new DefaultSegmentIterator(null, fileReader);
			X12Reader x12Reader = new X12Reader(iterator);
			x12Reader.setHandler(this);
			x12Reader.readIsa();
		
		} finally {
			fileReader.close();
		}
        
		return true;
	}
	/****************************
	 *  1000a Submitter         *
	 ****************************/
	@Override
	public void start1000a(hipaa4010.X837P_1000a x1000a) {
		createSubmitterReceiver();
		setNm1("Submitter", toFields(x1000a.getNm1()) );
	}

	@Override
	public void start1000a(hipaa5010.X837P_1000a x1000a) {
		createSubmitterReceiver();
		setNm1("Submitter", toFields(x1000a.getNm1()) );
	}

	@Override
	public void start1000a(hipaa4010.X837I_1000a x1000a) {
		createSubmitterReceiver();
		setNm1("Submitter", toFields(x1000a.getNm1()) );
	}

	@Override
	public void start1000a(hipaa5010.X837I_1000a x1000a) {
		createSubmitterReceiver();
		setNm1("Submitter", toFields(x1000a.getNm1()) );
	}
	
	/****************************
	 *  1000b Receiver          *
	 ****************************/
	@Override
	public void end1000b(hipaa4010.X837P_1000b x1000b) {
		set("Receiver", x1000b.getNm1().getNameLastorOrgName03());
	}

	@Override
	public void end1000b(hipaa5010.X837P_1000b x1000b) {
		set("Receiver", x1000b.getNm1().getNameLastorOrgName03());
	}

	@Override
	public void end1000b(hipaa4010.X837I_1000b x1000b) {
		set("Receiver", x1000b.getNm1().getNameLastorOrgName03());
	}
	
	@Override
	public void end1000b(hipaa5010.X837I_1000b x1000b) {
        set("Receiver", x1000b.getNm1().getNameLastorOrgName03());
	}
	/***********************************
	 *  2000a Billing/Pay-To Provider  *
	 **********************************/
	
	@Override
	public void start2000a(hipaa4010.X837P_2000a x837p_2000a) {
		createBillPayTo();
	}
	
	@Override
	public void start2000a(hipaa5010.X837P_2000a x837p_2000a) {
		createBillPayTo();
	}


	@Override
	public void start2000a(hipaa4010.X837I_2000a x837i_2000a) {
		createBillPayTo();
	}

	@Override
	public void start2000a(hipaa5010.X837I_2000a x837i_2000a) {
		createBillPayTo();
	}





	/****************************
	 *  2010aa Billing Provider *
	 ****************************/
	@Override
	public void end2010aa(hipaa4010.X837P_2010aa x2010aa) {
		hipaa4010.Nm1 nm1 = x2010aa.getNm1();
		setNm1("BillingProvider", toFields(nm1) );
		setN3( "BillingProvider", toFields(x2010aa.getN3()));
		setN4( "BillingProvider", toFields(x2010aa.getN4()));
		
		if (nm1.getIdCodeQualifier08().matches("24|34")) {
			set("BillingProviderTaxIdNumber", nm1.getIdCode09());
		} else {
			for (hipaa4010.Ref ref : x2010aa.getRef0B()) {
				if (ref.getRefIdQualifier01().matches("EI|SY")) {
					set("BillingProviderTaxIdNumber", ref.getRefId02());
				}
			}
		}
		for(hipaa4010.Per per : x2010aa.getPer()) {
			//"BillingProviderTelephoneNumber"
			String communicationNumberQualifiers[] = 
			{ 
				per.getCommunicationNumQualifier03()
			   ,per.getCommunicationNumQualifier05()
			   ,per.getCommunicationNumQualifier07()
			};
			String communicationNumbers[] = 
			{ 
				per.getCommunicationNum04()
			   ,per.getCommunicationNum06()
			   ,per.getCommunicationNum08()
			};
			for(int i = 0; i < communicationNumberQualifiers.length; i++ ) {
				String commNumQual = communicationNumberQualifiers[i];
				String commNum     = communicationNumbers[i];
				if(commNumQual.equals("FX")) {
					set("BillingProviderFaxNumber",commNum);
				} else if(commNumQual.matches("TE|EM|EX")){
					set("BillingProviderTelephoneNumber",commNum);
				}
			}
		}
	}
	
	@Override
	public void end2010aa(hipaa5010.X837P_2010aa x2010aa) {
		hipaa5010.Nm1 nm1 = x2010aa.getNm1();
		setNm1("BillingProvider", toFields(nm1) );
		setN3( "BillingProvider", toFields(x2010aa.getN3()));
		setN4( "BillingProvider", toFields(x2010aa.getN4()));
		
		if (nm1.getIdCodeQualifier08().matches("24|34")) {
			set("BillingProviderTaxIdNumber", nm1.getIdCode09());
		} else {
			for (hipaa5010.Ref ref : x2010aa.getRef0B()) {
				if (ref.getRefIdQualifier01().matches("EI|SY")) {
					set("BillingProviderTaxIdNumber", ref.getRefId02());
				}
			}
		}
		for(hipaa5010.Per per : x2010aa.getPer()) {
			//"BillingProviderTelephoneNumber"
			String communicationNumberQualifiers[] = 
			{ 
				per.getCommunicationNumQualifier03()
			   ,per.getCommunicationNumQualifier05()
			   ,per.getCommunicationNumQualifier07()
			};
			String communicationNumbers[] = 
			{ 
				per.getCommunicationNum04()
			   ,per.getCommunicationNum06()
			   ,per.getCommunicationNum08()
			};
			for(int i = 0; i < communicationNumberQualifiers.length; i++ ) {
				String commNumQual = communicationNumberQualifiers[i];
				String commNum     = communicationNumbers[i];
				if(commNumQual.equals("FX")) {
					set("BillingProviderFaxNumber",commNum);
				} else if(commNumQual.matches("TE|EM|EX")){
					set("BillingProviderTelephoneNumber",commNum);
				}
			}
		}
	}

	@Override
	public void end2010aa(hipaa4010.X837I_2010aa x2010aa) {
		hipaa4010.Nm1 nm1 = x2010aa.getNm1();
		setNm1("BillingProvider", toFields(nm1) );
		setN3( "BillingProvider", toFields(x2010aa.getN3()));
		setN4( "BillingProvider", toFields(x2010aa.getN4()));
		
		if (nm1.getIdCodeQualifier08().matches("24|34")) {
			set("BillingProviderTaxIdNumber", nm1.getIdCode09());
		} else {
			for (hipaa4010.Ref ref : x2010aa.getRef0B()) {
				if (ref.getRefIdQualifier01().matches("EI|SY")) {
					set("BillingProviderTaxIdNumber", ref.getRefId02());
				}
			}
		}
		for(hipaa4010.Per per : x2010aa.getPer()) {
			//"BillingProviderTelephoneNumber"
			String communicationNumberQualifiers[] = 
			{ 
				per.getCommunicationNumQualifier03()
			   ,per.getCommunicationNumQualifier05()
			   ,per.getCommunicationNumQualifier07()
			};
			String communicationNumbers[] = 
			{ 
				per.getCommunicationNum04()
			   ,per.getCommunicationNum06()
			   ,per.getCommunicationNum08()
			};
			for(int i = 0; i < communicationNumberQualifiers.length; i++ ) {
				String commNumQual = communicationNumberQualifiers[i];
				String commNum     = communicationNumbers[i];
				if(commNumQual.equals("FX")) {
					set("BillingProviderFaxNumber",commNum);
				} else if(commNumQual.matches("TE|EM|EX")){
					set("BillingProviderTelephoneNumber",commNum);
				}
			}
		}
	}

	@Override
	public void end2010aa(hipaa5010.X837I_2010aa x2010aa) {
		setNm1("BillingProvider", toFields(x2010aa.getNm1()) );
		setN3( "BillingProvider", toFields(x2010aa.getN3()));
		setN4( "BillingProvider", toFields(x2010aa.getN4()));
		
	}
	

	/****************************
	 *  2010ab PayTo Provider   *
	 ****************************/	

	@Override
	public void end2010ab(hipaa4010.X837P_2010ab x2010ab) {
		setNm1("PayToProvider", toFields(x2010ab.getNm1()) );
		setN3( "PayToProvider", toFields(x2010ab.getN3()));
		setN4( "PayToProvider", toFields(x2010ab.getN4()));
	}
	
	@Override
	public void end2010ab(hipaa5010.X837P_2010ab x2010ab) {
		setNm1("PayToProvider", toFields(x2010ab.getNm1()) );
		setN3( "PayToProvider", toFields(x2010ab.getN3()));
		setN4( "PayToProvider", toFields(x2010ab.getN4()));
	}


	@Override
	public void end2010ab(hipaa4010.X837I_2010ab x2010ab) {
		setNm1("PayToProvider", toFields(x2010ab.getNm1()) );
		setN3( "PayToProvider", toFields(x2010ab.getN3()));
		setN4( "PayToProvider", toFields(x2010ab.getN4()));
	}

	@Override
	public void end2010ab(hipaa5010.X837I_2010ab x2010ab) {
		setNm1("PayToProvider", toFields(x2010ab.getNm1()) );
		setN3( "PayToProvider", toFields(x2010ab.getN3()));
		setN4( "PayToProvider", toFields(x2010ab.getN4()));
	}
	
	
    /************************************************
     *  2000b Subscriber Heirarchical               *                      
     ************************************************/
	@Override
	public void start2000b(hipaa4010.X837P_2000b x2000b) {
		createSubscriber();
		hipaa4010.Sbr sbr = x2000b.getSbr();
		set("PatientRelationshipToSubscriber",sbr.getIndividualRelationshipCode02());
		set("PlanType", sbr.getClmFilingIndicatorCode09());
	}
	
	@Override
	public void start2000b(hipaa5010.X837P_2000b x2000b) {
		createSubscriber();
		hipaa5010.Sbr sbr = x2000b.getSbr();
		set("PatientRelationshipToSubscriber",sbr.getIndividualRelationshipCode02());
		set("PlanType", sbr.getClmFilingIndicatorCode09());
	}

	@Override
	public void start2000b(hipaa4010.X837I_2000b x2000b) {
		createSubscriber();
		hipaa4010.Sbr sbr = x2000b.getSbr();
		set("PatientRelationshipToSubscriber",sbr.getIndividualRelationshipCode02());
		set("PlanType", sbr.getClmFilingIndicatorCode09());
	}
	
	@Override
	public void start2000b(hipaa5010.X837I_2000b x2000b) {
		createSubscriber();
		hipaa5010.Sbr sbr = x2000b.getSbr();
		set("PatientRelationshipToSubscriber",sbr.getIndividualRelationshipCode02());
		set("PlanType", sbr.getClmFilingIndicatorCode09());
	}

	/****************************
	 *  2010ba Subscriber Name  *
	 ****************************/
	@Override
	public void start2010ba(hipaa4010.X837P_2010ba x2010ba) {
		setNm1("Subscriber", toFields(x2010ba.getNm1()));
		setN3( "Subscriber", toFields(x2010ba.getN3()));
		setN4( "Subscriber", toFields(x2010ba.getN4()));
		set("SubscriberDateOfBirth", x2010ba.getDmg().getDateTimePeriod02());
		set("SubscriberGender", x2010ba.getDmg().getGenderCode03());
	}

	@Override
	public void start2010ba(hipaa5010.X837P_2010ba x2010ba) {
		setNm1("Subscriber", toFields(x2010ba.getNm1()));
		setN3( "Subscriber", toFields(x2010ba.getN3()));
		setN4( "Subscriber", toFields(x2010ba.getN4()));
		set("SubscriberDateOfBirth", x2010ba.getDmg().getDateTimePeriod02());
		set("SubscriberGender", x2010ba.getDmg().getGenderCode03());
	}


	@Override
	public void start2010ba(hipaa4010.X837I_2010ba x2010ba) {
		setNm1("Subscriber", toFields(x2010ba.getNm1()));
		setN3( "Subscriber", toFields(x2010ba.getN3()));
		setN4( "Subscriber", toFields(x2010ba.getN4()));
		set("SubscriberDateOfBirth", x2010ba.getDmg().getDateTimePeriod02());
		set("SubscriberGender", x2010ba.getDmg().getGenderCode03());
	}


	@Override
	public void start2010ba(hipaa5010.X837I_2010ba x2010ba) {
		setNm1("Subscriber", toFields(x2010ba.getNm1()));
		setN3( "Subscriber", toFields(x2010ba.getN3()));
		setN4( "Subscriber", toFields(x2010ba.getN4()));
		set("SubscriberDateOfBirth", x2010ba.getDmg().getDateTimePeriod02());
		set("SubscriberGender", x2010ba.getDmg().getGenderCode03());
	}
    /************************************************
     *  2000C Patient Heirarchical                  *                      
     ************************************************/
	@Override
	public void start2000c(hipaa4010.X837P_2000c x2000c) {
		set("PatientRelationshipToSubscriber",x2000c.getPat().getIndividualRelationshipCode01());
	}
	
	@Override
	public void start2000c(hipaa5010.X837P_2000c x2000c) {
		set("PatientRelationshipToSubscriber",x2000c.getPat().getIndividualRelationshipCode01());
	}

	@Override
	public void start2000c(hipaa4010.X837I_2000c x2000c) {
		set("PatientRelationshipToSubscriber",x2000c.getPat().getIndividualRelationshipCode01());
	}
	
	@Override
	public void start2000c(hipaa5010.X837I_2000c x2000c) {
		set("PatientRelationshipToSubscriber",x2000c.getPat().getIndividualRelationshipCode01());
	}
	/****************************
	 *  2010ca Patient Name     *
	 ****************************/
	@Override
	public void start2010ca(hipaa4010.X837P_2010ca x2010ca) {
		createPatient();
		setNm1("Patient", toFields(x2010ca.getNm1()));
		setN3( "Patient", toFields(x2010ca.getN3()));
		setN4( "Patient", toFields(x2010ca.getN4()));
		set("PatientDateOfBirth", x2010ca.getDmg().getDateTimePeriod02());
		set("PatientGender", x2010ca.getDmg().getGenderCode03());
	}

	@Override
	public void start2010ca(hipaa5010.X837P_2010ca x2010ca) {
		createPatient();
		setNm1("Patient", toFields(x2010ca.getNm1()));
		setN3( "Patient", toFields(x2010ca.getN3()));
		setN4( "Patient", toFields(x2010ca.getN4()));
		set("PatientDateOfBirth", x2010ca.getDmg().getDateTimePeriod02());
		set("PatientGender", x2010ca.getDmg().getGenderCode03());
	}


	@Override
	public void start2010ca(hipaa4010.X837I_2010ca x2010ca) {
		createPatient();
		setNm1("Patient", toFields(x2010ca.getNm1()));
		setN3( "Patient", toFields(x2010ca.getN3()));
		setN4( "Patient", toFields(x2010ca.getN4()));
		set("PatientDateOfBirth", x2010ca.getDmg().getDateTimePeriod02());
		set("PatientGender", x2010ca.getDmg().getGenderCode03());
	}


	@Override
	public void start2010ca(hipaa5010.X837I_2010ca x2010ca) {
		createPatient();
		setNm1("Patient", toFields(x2010ca.getNm1()));
		setN3( "Patient", toFields(x2010ca.getN3()));
		setN4( "Patient", toFields(x2010ca.getN4()));
		set("PatientDateOfBirth", x2010ca.getDmg().getDateTimePeriod02());
		set("PatientGender", x2010ca.getDmg().getGenderCode03());
	}	
	/****************************
	 *  2300 Claim              *
	 ****************************/	

	@Override
	public void start2300(hipaa4010.X837P_2300 x2300) {
		counter2320 = 0;
		createClaim();
	}
	@Override
	public void end2300(hipaa4010.X837P_2300 x2300) {
		hipaa4010.Clm clm = x2300.getClm();
		set("PatientAccount#",clm.getClmSubmittersId01());
		set("TotalClaimChargeAmount",clm.getMonetaryAmt02());
		C023 hlthCareSvcLocInfo05 = clm.getHlthCareSvcLocInfo05();
		set("FacilityTypeCode",hlthCareSvcLocInfo05.getFacilityCodeValue01());
		set("ClaimSubmissionReasonCode",hlthCareSvcLocInfo05.getClmFrequencyTypeCode03());
		set("InitialTreatmentDate",x2300.getDtp454().getDateTimePeriod03());
		set("RelatedHospitalizationAdmissionDate",x2300.getDtp435().getDateTimePeriod03() );
		set("RelatedHospitalizationDischargeDate",x2300.getDtp096().getDateTimePeriod03() );
		set("MedicalRecord#",x2300.getRefEA().getRefId02());
		
		hipaa4010.Hi hi = x2300.getHi();
		hipaa4010.C022 c022s[] 
 	    = {hi.getHlthCareCodeInfo01()
 		  ,hi.getHlthCareCodeInfo02()
 		  ,hi.getHlthCareCodeInfo03()
 		  ,hi.getHlthCareCodeInfo04()
 		  ,hi.getHlthCareCodeInfo05()
 		  ,hi.getHlthCareCodeInfo06()
 		  ,hi.getHlthCareCodeInfo07()
 		  ,hi.getHlthCareCodeInfo08()};
	    
		for(int i = 0; i < c022s.length; i++) {
			hipaa4010.C022 info = c022s[i];
 			if(info.getCodeListQualifierCode01().length() > 0) {
 				set("DiagnosisCode",i+1,"",info.getIndustryCode02());
 			}
 		}
		for(hipaa4010.X837P_2400 x2400 : x2300.get2400()) {
			write2400toCsv(x2400);
		}
		x2300.clear();
	}



	@Override
		public void start2300(hipaa5010.X837P_2300 x2300) {
	/*		
	        default2300(x2300);
			hipaa5010.Clm clm = x2300.getClm();
			set("TotalClaimChargeAmount",clm.getMonetaryAmt02());
			set("FacilityTypeCode",clm.getHlthCareSvcLocInfo05().getFacilityCodeValue01());
			set("InitialTreatmentDate",x2300.getDtp454().getDateTimePeriod03());
			set("RelatedHospitalizationAdmissionDate",x2300.getDtp435().getDateTimePeriod03() );
			set("RelatedHospitalizationDischargeDate",x2300.getDtp096().getDateTimePeriod03() );
			set("MedicalRecord#",x2300.getRefEA().getRefId02());
			hipaa5010.Hi hi = x2300.getHiBK();
			hipaa5010.C022 c022s[] 
	 	    = {hi.getHlthCareCodeInfo01()
	 		  ,hi.getHlthCareCodeInfo02()
	 		  ,hi.getHlthCareCodeInfo03()
	 		  ,hi.getHlthCareCodeInfo04()
	 		  ,hi.getHlthCareCodeInfo05()
	 		  ,hi.getHlthCareCodeInfo06()
	 		  ,hi.getHlthCareCodeInfo07()
	 		  ,hi.getHlthCareCodeInfo08()
	 		  };
			for(int i = 1; i < c022s.length; i++) {
				hipaa5010.C022 info = c022s[i];
	 			if(info.getCodeListQualifierCode01().length() > 0) {
                    set("DiagnosisCode",i+1,"",info.getIndustryCode02());
	 			}
	 		}
	 		
		    counter2320 = 0;
	*/ 		
		}


	@Override
	public void start2300(hipaa4010.X837I_2300 x2300) {
		
		counter2320 = 0;
		createClaim();
	}
	@Override
	public void end2300(hipaa4010.X837I_2300 x2300) {

		// CLM 02 CLM 05-01
		hipaa4010.Clm clm = x2300.getClm();
		set("PatientAccount#",clm.getClmSubmittersId01());
		set("TotalClaimChargeAmount",clm.getMonetaryAmt02());
		C023 hlthCareSvcLocInfo05 = clm.getHlthCareSvcLocInfo05();
		set("FacilityTypeCode",hlthCareSvcLocInfo05.getFacilityCodeValue01());
		set("ClaimSubmissionReasonCode",hlthCareSvcLocInfo05.getClmFrequencyTypeCode03());
		// DTP*434 03
		hipaa4010.Dtp dtp434 = x2300.getDtp434();
		setDtp("Statement", getDates(dtp434.getDateTimePeriod03()));
		// DTP*435 03
		set("RelatedHospitalizationAdmissionDateAndHour",x2300.getDtp435().getDateTimePeriod03() );
		// DTP*096 03
		set("RelatedHospitalizationDischargeHour",x2300.getDtp096().getDateTimePeriod03() );
		set("TypeOfAdmission",x2300.getCl1().getAdmissionTypeCode01());
		set("SourceOfAdmission",x2300.getCl1().getAdmissionSourceCode02());
		// REF*9F  02
		if(x2300.getRef9FSize() > 0 ) {
		    set("PriorAuthorization#1",x2300.getRef9F(0).getRefId02());
		}		
		if(x2300.getRef9FSize() > 1 ) {
		    set("PriorAuthorization#2",x2300.getRef9F(1).getRefId02());
		}
		

		// HI*BK 01    - 02
		// HI*BF 01,12 - 02
		
		{
			hipaa4010.Hi principalDiagnosis = x2300.getHiBK();
			hipaa4010.Hi otherDiagnosis = firstHi(x2300.getHiBF());
			hipaa4010.C022 c022s[] = {
					principalDiagnosis.getHlthCareCodeInfo01(),
					otherDiagnosis.getHlthCareCodeInfo01(),
					otherDiagnosis.getHlthCareCodeInfo02(),
					otherDiagnosis.getHlthCareCodeInfo03(),
					otherDiagnosis.getHlthCareCodeInfo04(),
					otherDiagnosis.getHlthCareCodeInfo05(),
					otherDiagnosis.getHlthCareCodeInfo06(),
					otherDiagnosis.getHlthCareCodeInfo07(),
					otherDiagnosis.getHlthCareCodeInfo08(),
					otherDiagnosis.getHlthCareCodeInfo09(),
					otherDiagnosis.getHlthCareCodeInfo10(),
					otherDiagnosis.getHlthCareCodeInfo11(),
					otherDiagnosis.getHlthCareCodeInfo12() };
			for(int i = 0; i < c022s.length; i++) {
				hipaa4010.C022 info = c022s[i];
				if (info.getCodeListQualifierCode01().length() > 0) {
	 				set("DiagnosisCode",i+1,"",info.getIndustryCode02());
				}
			}
		}
		// HI*DR 01 - 02
		set("DiagnosisRelatedGroup",x2300.getHiDR().getHlthCareCodeInfo01().getIndustryCode02());
		
		// HI*BP 01    - 02
		// HI*BO 01,12 - 02
		{
			hipaa4010.Hi principalProcedure = x2300.getHiBP();
			hipaa4010.Hi otherProcedure = firstHi(x2300.getHiBO());
			hipaa4010.C022 c022s[] = {
				principalProcedure.getHlthCareCodeInfo01()
				,otherProcedure.getHlthCareCodeInfo01()
				,otherProcedure.getHlthCareCodeInfo02()
				,otherProcedure.getHlthCareCodeInfo03()
				,otherProcedure.getHlthCareCodeInfo04()
				,otherProcedure.getHlthCareCodeInfo05()
				,otherProcedure.getHlthCareCodeInfo06()
				,otherProcedure.getHlthCareCodeInfo07()
				,otherProcedure.getHlthCareCodeInfo08()
				,otherProcedure.getHlthCareCodeInfo09()
				,otherProcedure.getHlthCareCodeInfo10()
				,otherProcedure.getHlthCareCodeInfo11()
				,otherProcedure.getHlthCareCodeInfo12()
			};
			for(int i = 0; i < c022s.length; i++) {
				hipaa4010.C022 info = c022s[i];
				if (info.getCodeListQualifierCode01().length() > 0) {
	 				set("IcdProcedure",i+1,"",info.getIndustryCode02());
				}
			}
			
		}
		// HI*BG 01-12 01
		{
			hipaa4010.Hi conditionCode = firstHi(x2300.getHiBG());
			hipaa4010.C022 c022s[] = {
				 conditionCode.getHlthCareCodeInfo01()
				,conditionCode.getHlthCareCodeInfo02()
				,conditionCode.getHlthCareCodeInfo03()
				,conditionCode.getHlthCareCodeInfo04()
				,conditionCode.getHlthCareCodeInfo05()
				,conditionCode.getHlthCareCodeInfo06()
				,conditionCode.getHlthCareCodeInfo07()
				,conditionCode.getHlthCareCodeInfo08()
				,conditionCode.getHlthCareCodeInfo09()
				,conditionCode.getHlthCareCodeInfo10()
				,conditionCode.getHlthCareCodeInfo11()
				,conditionCode.getHlthCareCodeInfo12()
			};
			for(int i = 1; i < c022s.length; i++) {
				hipaa4010.C022 info = c022s[i];
				if (info.getCodeListQualifierCode01().length() > 0) {
	 				set("ConditionCode",i+1,"",info.getIndustryCode02());
				}
			}
			
		}

		for(hipaa4010.X837I_2400 x2400 : x2300.get2400()) {
			write2400toCsv(x2400);
		}
		x2300.clear();

	}

	
    @Override
	public void start2300(hipaa5010.X837I_2300 x2300) {
		/*
		 default2300(x2300);
		 */
	}


	/***********************************
	 *  2310a Referring Provider Name  *
	 ***********************************/
	@Override
	public void end2310a(hipaa4010.X837P_2310a x2310a) {
		setNm1("ReferringProvider",toFields(x2310a.getNm1()));
		
	}
	@Override
	public void end2310a(hipaa5010.X837P_2310a x2310a) {
		setNm1("ReferringProvider",toFields(x2310a.getNm1()));
	}


	/***********************************
	 *  2310a Attending Provider Name  *
	 ***********************************/
	@Override
	public void end2310a(hipaa4010.X837I_2310a x2310a) {
		setNm1("AttendingProvider",toFields(x2310a.getNm1()));
		//363LP0200N
		set("AttendingProviderTaxonomy", x2310a.getPrv().getRefId03());

	}
	@Override
	public void end2310a(hipaa5010.X837I_2310a x2310a) {
		setNm1("AttendingProvider",toFields(x2310a.getNm1()));
	}


	/***********************************
	 *  2310b Rendering Provider Name  *
	 ***********************************/
	@Override
	public void end2310b(hipaa4010.X837P_2310b x2310b) {
		setNm1("RenderingProvider",toFields(x2310b.getNm1()));
		set("RenderingProviderTaxonomy", x2310b.getPrv().getRefId03());

	}
	@Override
	public void end2310b(hipaa5010.X837P_2310b x2310b) {
		setNm1("RenderingProvider",toFields(x2310b.getNm1()));
	}

	/***********************************
	 *  2310b Operating Physician Name *
	 ***********************************/	
	@Override
	public void end2310b(hipaa4010.X837I_2310b x2310b) {
		setNm1("OperatingPhysician",toFields(x2310b.getNm1()));
	}

	@Override
	public void end2310b(hipaa5010.X837I_2310b x2310b) {
		setNm1("OperatingPhysician",toFields(x2310b.getNm1()));
	}


	/*******************************************
	 *  2310c Purchased Service Provider Name  *
	 *******************************************/
	@Override
	public void end2310c(hipaa4010.X837P_2310c x2310c) {
		setNm1("PurchasedService",toFields(x2310c.getNm1()));
	}
	
	/*******************************************
	 *  2310c Service Facility Location Name  *
	 *******************************************/
	@Override
	public void end2310c(hipaa5010.X837P_2310c x2310c) {
		setNm1("ServiceFacilityLocation",toFields(x2310c.getNm1()));
		setN3("ServiceFacilityLocation", toFields(x2310c.getN3()));
		setN4("ServiceFacilityLocation", toFields(x2310c.getN4()));
	}


	/***********************************
	 *  2310c Other Provider Name *
	 ***********************************/
	@Override
	public void end2310c(hipaa4010.X837I_2310c x2310c) {
		setNm1("OtherProviderName",toFields(x2310c.getNm1()));
	}

	/*****************************************
	 *  2310c Other Operating Physician Name *
	 ****************************************/
	@Override
	public void end2310c(hipaa5010.X837I_2310c x2310c) {
	}



	/*************************************
	 *  2310d Service Facility Location  *
	 *************************************/
	@Override
	public void end2310d(hipaa4010.X837P_2310d x2310d) {
		setNm1("ServiceFacilityLocation",toFields(x2310d.getNm1()));
		setN3("ServiceFacilityLocation", toFields(x2310d.getN3()));
		setN4("ServiceFacilityLocation", toFields(x2310d.getN4()));

	}
	/*************************************
	 *  2310d Supervising Provider Name  *
	 *************************************/
	@Override
	public void end2310d(hipaa5010.X837P_2310d x2310d) {
		setNm1("SupervisingProvider",toFields(x2310d.getNm1()));
	}

	/*************************************
	 *  2310d Rendering Provider Name    *
	 *************************************/
//	@Override
//	public void end2310d(hipaa4010.X837I_2310d x2310d) {
//	}
	
	@Override
	public void end2310d(hipaa5010.X837I_2310d x2310d) {
		setNm1("RenderingProvider",toFields(x2310d.getNm1()));
	}

	/*************************************
	 *  2310e Supervising Provider Name  *
	 *************************************/
	@Override
	public void end2310e(hipaa4010.X837P_2310e x2310e) {
		setNm1("SupervisingProvider",toFields(x2310e.getNm1()));
	}	
	/*************************************
	 *  2310e Ambulance Pickup Location  *
	 *************************************/
	@Override
	public void end2310e(hipaa5010.X837P_2310e x2310e) {
	}	
	/*************************************
	 *  2310e Service Facility Name      *
	 *************************************/
	@Override
	public void end2310e(hipaa4010.X837I_2310e x2310e) {
		setNm1("ServiceFacilityLocation",toFields(x2310e.getNm1()));
		setN3("ServiceFacilityLocation", toFields(x2310e.getN3()));
		setN4("ServiceFacilityLocation", toFields(x2310e.getN4()));
	}
	/**********************************************
	 *  2310e Service Facility Location Name      *
	 *********************************************/
	@Override
	public void end2310e(hipaa5010.X837I_2310e x2310e) {
		setNm1("ServiceFacilityLocation",toFields(x2310e.getNm1()));
		setN3("ServiceFacilityLocation", toFields(x2310e.getN3()));
		setN4("ServiceFacilityLocation", toFields(x2310e.getN4()));
	}


	/******************************************************************************
	 * 2320 Other Subscriber Info                                                 *
	 * Group#, Group, Paid, Allowed, NonCovered, Deductible, Copay, ConInsurance
	 * Amount Not Covered  * 
	 *****************************************************************************/
	@Override
	public void end2320(hipaa4010.X837P_2320 x2320) {
		counter2320++;
		set("OtherSubscriber",counter2320, "Group#",   x2320.getSbr().getRefId03());
		set("OtherSubscriber",counter2320, "Group",    x2320.getSbr().getName04());
		set("OtherSubscriber",counter2320, "Paid",     x2320.getAmtD().getMonetaryAmt02());
		set("OtherSubscriber",counter2320, "Allowed",  x2320.getAmtB6().getMonetaryAmt02());
		BigDecimal deductible = BigDecimal.ZERO;
		BigDecimal coInsurance = BigDecimal.ZERO;
		BigDecimal coPay = BigDecimal.ZERO;
		BigDecimal notCoveredAmount = BigDecimal.ZERO;
		for (hipaa4010.Cas cas: x2320.getCas()) {
			String reasons[] = {
				cas.getClmAdjustmentReasonCode02()
			   ,cas.getClmAdjustmentReasonCode05()
			   ,cas.getClmAdjustmentReasonCode08()
			   ,cas.getClmAdjustmentReasonCode11()
			   ,cas.getClmAdjustmentReasonCode14()
			   ,cas.getClmAdjustmentReasonCode17()
			};
			String amounts[] = {
				cas.getMonetaryAmt03()
			   ,cas.getMonetaryAmt06()
			   ,cas.getMonetaryAmt09()
			   ,cas.getMonetaryAmt12()
			   ,cas.getMonetaryAmt15()
			   ,cas.getMonetaryAmt18()
			};
			
			for(int i = 0; i < reasons.length && i < amounts.length; i++) {
				int reason = toInteger(reasons[i]);
				String amt = amounts[i];
				switch (reason) {
				case 1:
					deductible = calculator.add(deductible, amt);
					break;
				case 2:
					coInsurance = calculator.add(coInsurance, amt);
					break;
				case 3:
					coPay = calculator.add(coPay, amt);
					break;
				default:
					notCoveredAmount = calculator.add(notCoveredAmount, amt);
					break;	
				}
			}
		}
//		if(notCoveredAmount.compareTo(BigDecimal.ZERO) == 0) {
//			notCoveredAmount = calculator.add(notCoveredAmount, x2320.getAmtA8().getMonetaryAmt02());
//		}
		set("OtherSubscriber",counter2320, "Deductible",  deductible.toPlainString());
		set("OtherSubscriber",counter2320, "CoInsurance",  coInsurance.toPlainString());
		set("OtherSubscriber",counter2320, "CoPay",  coPay.toPlainString());
		set("OtherSubscriber",counter2320, "AmountNotCovered", notCoveredAmount.toPlainString());
		//OtherSubscriber01Deductible OtherSubscriber01AmountNotCovered
		if(x2320.get2330bSize() == 1) {
			String claimPaidDate = x2320.get2330b(0).getDtp().getDateTimePeriod03();
			set("OtherSubscriber",counter2320, "ClaimPaidDate",  claimPaidDate);
		}


		
	}
	@Override
	public void end2320(hipaa5010.X837P_2320 x2320) {
		counter2320++;
		set("OtherSubscriber",counter2320, "Group#",     x2320.getSbr().getRefId03());
		set("OtherSubscriber",counter2320, "Group",      x2320.getSbr().getName04());
		set("OtherSubscriber",counter2320, "Paid",       x2320.getAmtD().getMonetaryAmt02());
		set("OtherSubscriber",counter2320, "NonCovered", x2320.getAmtA8().getMonetaryAmt02());
	}
	protected static StringMath calculator = new StringMath(true);
	@Override
	public void end2320(hipaa4010.X837I_2320 x2320) {
		
		counter2320++;
		set("OtherSubscriber",counter2320, "Group#",   x2320.getSbr().getRefId03());
		set("OtherSubscriber",counter2320, "Group",    x2320.getSbr().getName04());
		set("OtherSubscriber",counter2320, "Paid",     x2320.getAmtC4().getMonetaryAmt02());
		set("OtherSubscriber",counter2320, "Allowed",  x2320.getAmtB6().getMonetaryAmt02());
		BigDecimal deductible = BigDecimal.ZERO;
		BigDecimal coInsurance = BigDecimal.ZERO;
		BigDecimal coPay = BigDecimal.ZERO;
		BigDecimal notCoveredAmount = BigDecimal.ZERO;
		for (hipaa4010.Cas cas: x2320.getCas()) {
			String reasons[] = {
				cas.getClmAdjustmentReasonCode02()
			   ,cas.getClmAdjustmentReasonCode05()
			   ,cas.getClmAdjustmentReasonCode08()
			   ,cas.getClmAdjustmentReasonCode11()
			   ,cas.getClmAdjustmentReasonCode14()
			   ,cas.getClmAdjustmentReasonCode17()
			};
			String amounts[] = {
				cas.getMonetaryAmt03()
			   ,cas.getMonetaryAmt06()
			   ,cas.getMonetaryAmt09()
			   ,cas.getMonetaryAmt12()
			   ,cas.getMonetaryAmt15()
			   ,cas.getMonetaryAmt18()
			};
			
			for(int i = 0; i < reasons.length && i < amounts.length; i++) {
				int reason = toInteger(reasons[i]);
				String amt = amounts[i];
				switch (reason) {
				case 1:
					deductible = calculator.add(deductible, amt);
					break;
				case 2:
					coInsurance = calculator.add(coInsurance, amt);
					break;
				case 3:
					coPay = calculator.add(coPay, amt);
					break;
				default:
					notCoveredAmount = calculator.add(notCoveredAmount, amt);
					break;	
				}
			}
		}
		if(notCoveredAmount.compareTo(BigDecimal.ZERO) == 0) {
			notCoveredAmount = calculator.add(notCoveredAmount, x2320.getAmtA8().getMonetaryAmt02());
		}
		set("OtherSubscriber",counter2320, "Deductible",  deductible.toPlainString());
		set("OtherSubscriber",counter2320, "CoInsurance",  coInsurance.toPlainString());
		set("OtherSubscriber",counter2320, "CoPay",  coPay.toPlainString());
		set("OtherSubscriber",counter2320, "AmountNotCovered", notCoveredAmount.toPlainString());
		//OtherSubscriber01Deductible OtherSubscriber01AmountNotCovered
		if(x2320.get2330bSize() == 1) {
			String claimPaidDate = x2320.get2330b(0).getDtp().getDateTimePeriod03();
			set("OtherSubscriber",counter2320, "ClaimPaidDate",  claimPaidDate);
		}
	}


	/**
	 * @param numberString
	 * @return
	 */
	private int toInteger(String numberString) {
		return calculator.toBigDecimal(numberString).intValue();
	}
	@Override
	public void end2320(hipaa5010.X837I_2320 x2320) {
		counter2320++;
		set("OtherSubscriber",counter2320, "Group#",     x2320.getSbr().getRefId03());
		set("OtherSubscriber",counter2320, "Group",      x2320.getSbr().getName04());
		set("OtherSubscriber",counter2320, "Paid",       x2320.getAmtD().getMonetaryAmt02());
		set("OtherSubscriber",counter2320, "NonCovered", x2320.getAmtA8().getMonetaryAmt02());
	}
	
	
	public void write2400toCsv(hipaa4010.X837P_2400 x2400) {
		createDetail();
		set("Assigned#", x2400.getLx().getAssignedNum01());
		hipaa4010.Sv1 sv1 = x2400.getSv1();
		hipaa4010.C003 composite = sv1.getCompositeMedProcId01();
		set("Product/SvcId",composite.getProductSlashSvcID02());
		String mods[] = 
		{
			composite.getProcModifier03()
		   ,composite.getProcModifier04()
		   ,composite.getProcModifier05()
		   ,composite.getProcModifier06()
		};
		for(int i = 0; i < mods.length; i++) {
			set("ProcedureModifier"+ (i+1), mods[i]);	
		}
		set("ServiceUnitCount", sv1.getQuantity04());
		set("PlaceOfService", sv1.getFacilityCodeValue05());
		hipaa4010.Sv5 sv5 = x2400.getSv5();
		set("ProcedureCode", sv5.getCompositeMedProcId01().getProductSlashSvcID02());
		
		String range[] = getDates(x2400.getDtp472().getDateTimePeriod03());
		setDtp("ServiceDate",range);

		
		set("TestingDate", firstDtp(x2400.getDtp738()).getDateTimePeriod03());
		

		set("InitialTreatmentDate", x2400.getDtp454().getDateTimePeriod03());
		for (int i = 0; i < x2400.getRef9FSize(); i++) {
			set("PriorAuthorization#", i + 1, "", x2400.getRef9F(i).getRefId02());
		}
		set("SalesTaxAmount"      ,x2400.getAmtT().getMonetaryAmt02());
		set("ApprovedAmount"      ,x2400.getAmtAAE().getMonetaryAmt02());
		set("PostageClaimedAmount",x2400.getAmtF4().getMonetaryAmt02());
		set("PricingProduct/SvcId", x2400.getHcp().getProductSlashSvcID10());
		for(int i = 0; i < x2400.get2410Size(); i ++) {
			hipaa4010.X837P_2410 x2410 = x2400.get2410(i);
			hipaa4010.Ctp ctp = x2410.getCtp();
			int repetition = i+1;
			set("Drug", repetition,"Code", x2410.getLin().getProductSlashSvcID03());
			set("Drug", repetition,"UnitPrice", ctp.getUnitPrice03()); 
			set("Drug", repetition,"UnitCount", ctp.getQuantity04());
			set("Drug", repetition,"UnitOfMeasure", ctp.getCompositeUnitofMeas05().getUnitorBasisforMeasCode01());
		}
		if(x2400.get2420aSize() > 0) {
		    setNm1("RenderingProvider2",toFields( x2400.get2420a(0).getNm1()));
		}
		if(x2400.get2420cSize() > 0) {
		    setNm1("ServiceFacilityLocation2",toFields( x2400.get2420c(0).getNm1()));
		}
		if(x2400.get2420eSize() > 0) {
		    setNm1("OrderingProvider2",toFields( x2400.get2420e(0).getNm1()));
		}
		if(x2400.get2420fSize() > 0) {
		    setNm1("ReferringProvider2",toFields( x2400.get2420f(0).getNm1()));
		}
		if(x2400.get2420fSize() > 1) {
		    setNm1("ReferringProvider3",toFields( x2400.get2420f(1).getNm1()));
		}
		
		for(int i = 0; i < x2400.get2420gSize(); i++) {
			hipaa4010.X837P_2420g x2420g = x2400.get2420g(i);
			for(int j = 0; j< x2420g.getRefSize(); j++) {
				int repetition = i *5 +j + 1;
				set("OtherPayerPriorAuthorization",repetition, "", x2420g.getRef(j).getRefId02());
			}
		}
		for(int i = 0; i < x2400.get2430Size(); i++) {
			hipaa4010.X837P_2430 x2430 = x2400.get2430(i);
			int adjudicationRepetition = i+1;
			Svd svd = x2430.getSvd();
			hipaa4010.C003 adjComposite = svd.getCompositeMedProcId03();
			String adjudicationMods[] = 
			{
			    adjComposite.getProcModifier03()
			   ,adjComposite.getProcModifier04()
			   ,adjComposite.getProcModifier05()
			   ,adjComposite.getProcModifier06()
			};
			
			set("Adjudication", adjudicationRepetition, "PayerId", svd.getIdCode01());
			set("Adjudication", adjudicationRepetition, "PaidAmount", svd.getMonetaryAmt02());
			
			set("Adjudication", adjudicationRepetition, "ProcedureCode", adjComposite.getProductSlashSvcID02());
			for (int j = 0; j < adjudicationMods.length; j++ ) {
				set("Adjudication", adjudicationRepetition, "ProcedureModifier0"+(j+1), adjudicationMods[j]);	
			}
			BigDecimal deductible = BigDecimal.ZERO;
			BigDecimal coInsurance = BigDecimal.ZERO;
			BigDecimal coPay = BigDecimal.ZERO;
			BigDecimal notCoveredAmount = BigDecimal.ZERO;
			int casCount = 0;
			for(hipaa4010.Cas cas : x2430.getCas() ) {
				String reasons[] = {
					cas.getClmAdjustmentReasonCode02()
				   ,cas.getClmAdjustmentReasonCode05()
				   ,cas.getClmAdjustmentReasonCode08()
				   ,cas.getClmAdjustmentReasonCode11()
				   ,cas.getClmAdjustmentReasonCode14()
				   ,cas.getClmAdjustmentReasonCode17()
				};
				String amounts[] = {
					cas.getMonetaryAmt03()
				   ,cas.getMonetaryAmt06()
				   ,cas.getMonetaryAmt09()
				   ,cas.getMonetaryAmt12()
				   ,cas.getMonetaryAmt15()
				   ,cas.getMonetaryAmt18()
				};
				for(int k = 0; k < reasons.length && k < amounts.length; k++) {
					int reason = toInteger(reasons[k]);
					String amt = amounts[k];
					switch (reason) {
					case 1:
						deductible = calculator.add(deductible, amt);
						break;
					case 2:
						coInsurance = calculator.add(coInsurance, amt);
						break;
					case 3:
						coPay = calculator.add(coPay, amt);
						break;
					default:
						notCoveredAmount = calculator.add(notCoveredAmount, amt);
						break;	
					}
				}
				set("Adjudication",adjudicationRepetition, "Deductible",  deductible.toPlainString());
				set("Adjudication",adjudicationRepetition, "CoInsurance",  coInsurance.toPlainString());
				set("Adjudication",adjudicationRepetition, "CoPay",  coPay.toPlainString());
				set("Adjudication",adjudicationRepetition, "AmountNonCovered", notCoveredAmount.toPlainString());

				
			}

			
		}
		
		try {
			System.err.println(joiner.join(fields));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void write2400toCsv(hipaa4010.X837I_2400 x2400) {
		createDetail();
		set("Assigned#", x2400.getLx().getAssignedNum01());
		hipaa4010.Sv2 sv2 = x2400.getSv2();
		set("RevenueCode",sv2.getProductSlashSvcID01());
		hipaa4010.C003 composite = sv2.getCompositeMedProcId02();
		set("Product/SvcId",composite.getProductSlashSvcID02());

		String mods[] = 
		{
			composite.getProcModifier03()
		   ,composite.getProcModifier04()
		   ,composite.getProcModifier05()
		   ,composite.getProcModifier06()
		};
		for(int i = 0; i < mods.length; i++) {
			set("ProcedureModifier"+ (i+1), mods[i]);	
		}

		set("ServiceUnitCount", sv2.getQuantity05());
		
		String range[] = getDates(x2400.getDtp472().getDateTimePeriod03());
		setDtp("ServiceDate",range);
		
		for(int i = 0; i < x2400.get2410Size(); i ++) {
			hipaa4010.X837I_2410 x2410 = x2400.get2410(i);
			hipaa4010.Ctp ctp = x2410.getCtp();
			int repetition = i+1;
			set("Drug", repetition,"Code", x2410.getLin().getProductSlashSvcID03());
			set("Drug", repetition,"UnitPrice", ctp.getUnitPrice03()); 
			set("Drug", repetition,"UnitCount", ctp.getQuantity04());
			set("Drug", repetition,"UnitOfMeasure", ctp.getCompositeUnitofMeas05().getUnitorBasisforMeasCode01());
		}
		if(x2400.get2420aSize() > 0) {
		    setNm1("AttendingPhysician2",toFields( x2400.get2420a(0).getNm1()));
		}
		if(x2400.get2420bSize() > 0) {
		    setNm1("OperatingingPhysician2",toFields( x2400.get2420b(0).getNm1()));
		}
		if(x2400.get2420cSize() > 0) {
		    setNm1("OtherProvider2",toFields( x2400.get2420c(0).getNm1()));
		}

		for(int counter2430 = 0; counter2430 < x2400.get2430Size(); counter2430++) {
			hipaa4010.X837I_2430 x2430 = x2400.get2430(counter2430);
			int adjudicationRepetition = counter2430+1;
			Svd svd = x2430.getSvd();
			hipaa4010.C003 adjComposite = svd.getCompositeMedProcId03();
			String adjudicationMods[] = 
			{
			    adjComposite.getProcModifier03()
			};
			set("Adjudication", adjudicationRepetition, "ProcedureCode", adjComposite.getProductSlashSvcID02());
			for (int j = 0; j < adjudicationMods.length; j++ ) {
				set("Adjudication", adjudicationRepetition, "ProcedureModifier0"+(j+1), adjudicationMods[j]);	
			}
			
			set("Adjudication", adjudicationRepetition, "PayerId", svd.getIdCode01());
			set("Adjudication", adjudicationRepetition, "PaidAmount", svd.getMonetaryAmt02());
			BigDecimal deductible = BigDecimal.ZERO;
			BigDecimal coInsurance = BigDecimal.ZERO;
			BigDecimal coPay = BigDecimal.ZERO;
			BigDecimal notCoveredAmount = BigDecimal.ZERO;
			int casCount = 0;
			for(hipaa4010.Cas cas : x2430.getCas() ) {
				String reasons[] = {
					cas.getClmAdjustmentReasonCode02()
				   ,cas.getClmAdjustmentReasonCode05()
				   ,cas.getClmAdjustmentReasonCode08()
				   ,cas.getClmAdjustmentReasonCode11()
				   ,cas.getClmAdjustmentReasonCode14()
				   ,cas.getClmAdjustmentReasonCode17()
				};
				String amounts[] = {
					cas.getMonetaryAmt03()
				   ,cas.getMonetaryAmt06()
				   ,cas.getMonetaryAmt09()
				   ,cas.getMonetaryAmt12()
				   ,cas.getMonetaryAmt15()
				   ,cas.getMonetaryAmt18()
				};
				for(int k = 0; k < reasons.length && k < amounts.length; k++) {
					int reason = toInteger(reasons[k]);
					String amt = amounts[k];
					switch (reason) {
					case 1:
						deductible = calculator.add(deductible, amt);
						break;
					case 2:
						coInsurance = calculator.add(coInsurance, amt);
						break;
					case 3:
						coPay = calculator.add(coPay, amt);
						break;
					default:
						notCoveredAmount = calculator.add(notCoveredAmount, amt);
						break;	
					}
				}
				set("Adjudication",adjudicationRepetition, "Deductible",  deductible.toPlainString());
				set("Adjudication",adjudicationRepetition, "CoInsurance",  coInsurance.toPlainString());
				set("Adjudication",adjudicationRepetition, "CoPay",  coPay.toPlainString());
				set("Adjudication",adjudicationRepetition, "AmountNonCovered", notCoveredAmount.toPlainString());

				
			}
		}
		try {
			System.err.println(joiner.join(fields));
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		
		
	}


	/**
	 * @param hi
	 * @return
	 */
	private hipaa4010.Hi firstHi(LinkedList<hipaa4010.Hi> hi) {
		return hi.size() > 0 
		? hi.get(0)
		: new hipaa4010.Hi();
	}	
	/**
	 * @param hi
	 * @return
	 */
	private hipaa4010.Dtp firstDtp(LinkedList<hipaa4010.Dtp> dtps) {
		return dtps.size() > 0 
		? dtps.get(0)
		: new hipaa4010.Dtp();
	}
	
	private String [] toFields(hipaa4010.Nm1 nm1) {
		String [] fields = {nm1.getNameFirst04(), nm1.getNameMiddle05(), nm1.getNameLastorOrgName03(),nm1.getIdCode09() };
		return fields;
	}	

	private String [] toFields(hipaa5010.Nm1 nm1) {
		String [] fields = {nm1.getNameFirst04(), nm1.getNameMiddle05(), nm1.getNameLastorOrgName03(),nm1.getIdCode09() };
		return fields;
	}

	private String[] toFields(hipaa4010.N3 n3) {
		String[] fields = {n3.getAddrInfo01(), n3.getAddrInfo02()};
		return fields;
	}
	private String[] toFields(hipaa5010.N3 n3) {
		String[] fields = {n3.getAddrInfo01(), n3.getAddrInfo02()};
		return fields;
	}


	private String[] toFields(hipaa4010.N4 n4) {
		String[] fields = {n4.getCityName01(), n4.getStateorProvinceCode02(), n4.getPostalCode03()};
		return fields;
	}
	private String[] toFields(hipaa5010.N4 n4) {
		String[] fields = {n4.getCityName01(), n4.getStateorProvinceCode02(), n4.getPostalCode03()};
		return fields;
	}
	

	
	
	public void setNm1(String role, String[] nm1Fields) {
		set(role,"FirstName",nm1Fields[0] );
		set(role,"MiddleName",nm1Fields[1] );
		set(role,"LastName",nm1Fields[2] );

		if(find(role +"Id") >=0) {
		    set(role, "Id",nm1Fields[3] );
		}

	}

	public void setN3(String role, String[] n3Fields) {
		set(role,"Addr1",n3Fields[0]);
		set(role,"Addr2",n3Fields[1]);
	}
	public void setN4(String role, String[] n4Fields) {
		set(role,"City",n4Fields[0]);
		set(role,"State",n4Fields[1]);
		set(role,"Zip",n4Fields[2]);
	}
	public void setDtp(String role, String[]dateRange) {
		set(role + "From", dateRange[0]);
		set(role + "To", dateRange[1]);
	}



	
	int getIndex(String fieldName) {
		for(int i = 0; i < fieldNames.length; i++) {
			if(fieldName.equals(fieldNames[i])) {
				return i;
			}
		}
		throw new RuntimeException(fieldName + " is not a field");
	}

	

	/**
	 * @param dates
	 * @return
	 */
	public static String[] getDates(String dates) {
		final Pattern datesRegex = Pattern.compile("^\\s*(\\d{8})\\s?-?\\s?(\\d*)");
		Matcher m = datesRegex.matcher(dates);
		if(m.find()) {
			String twoDates[] = {m.group(1), m.group(2)};
			return twoDates;  			
		}else {
			String twoDates[] = {"", ""};
			return twoDates;	
		}
	}


	int find(String fieldName) {
		for(int i = 0; i < fieldNames.length; i++) {
			if(fieldName.equals(fieldNames[i])) {
				return i;
			}
		}
		return -1;
	}
	public void set(String key,String value) {
		fields[getIndex(key)] = value;
		
	}
	public void set(String role, String field, String value) {
		set(role + field, value);
	}
	public void set(String role,int repetition, String field, String value) {
		
		String fieldName 
		= repetition == 0 
		? role + field 
		: role + String.format("%1$02d", repetition)+ field;
		set(fieldName,value);
	}


	public String get(String role,int repetition, String field) {
		String fieldName 
		= repetition == 0 
		? role + field 
		: role + String.format("%1$02d", repetition)+ field;
		return get(fieldName);
	}
	public String get(String role, String field) {
		return get(role + field);
	}
	public String get(String key) {
		return fields[getIndex(key)];
	}


	public void createSubmitterReceiver() {
		clearFields(TRANSACTION_HEADER, END2400);
	}


	public void createBillPayTo() {
		clearFields(F2000A, END2400);
	}
	public void createSubscriber() {
		clearFields(F2000B, END2400);
	}
	public void createPatient() {
		clearFields(F2000C, END2400);
	}
	public void createClaim() {
		clearFields(F2300, END2400);
	}
	public void createDetail() {
		clearFields(F2400, END2400);
	}


	private void clearFields(String begin, String end) {
		int beginIndex = getIndex(begin);
		int endIndex = getIndex(end);
		for(int i = beginIndex; i <= endIndex; i++) {
			fields[i] = "";
		}
		
	}


	



}
