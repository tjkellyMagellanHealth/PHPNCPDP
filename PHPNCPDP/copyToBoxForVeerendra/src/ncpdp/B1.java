/**
 * 
 */
package ncpdp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * @author tjkelly
 *
 */
public class B1 {
/* Editing tips for collections
 * 1.   Declare class           public static class RejectCodes extends Fields{}                                                
 *      
 * 2.   Declare member            RejectCodes rejectCodes = new RejectCodes();
 *      
 * 3.   override add(Field)#1  public boolean add(Field f) {                       		// (non-Javadoc)                                    
 *                             	boolean changed = true;                           		// @see ncpdp.Fields#add(ncpdp.Field)               
 *                             	if("5E".equals(f.getCode())){                     		// SE ER                                            
 *                              	int capacity = 10;                                		//                                                 
 *                              	try {                                             		@Override                                           
 *                              		capacity = Integer.parseInt(f.getValue());      		public boolean add(Field f) {                       
 *                              	} catch(Exception ignore) {}                      			boolean changed = true;                           
 *                              	rejectCodes.ensureCapacity(capacity);             			if("SE".equals(f.getCode())) {                    
 *                              	changed = super.add(f);                           				int capacity = 10;                              
 *                              }                                                  				try {                                           
 * 4.   override add(Field)#2   }  else if("6E".equals(f.getCode())){              					capacity = Integer.parseInt(f.getValue());    
 *      				                   rejectCodes.add(f);                              				}catch(Exception ignore) {}                     
 *      				                }                                                   				procedureModifierCodes.ensureCapacity(capacity);
 *      				                                                                    				changed = super.add(f);                         
 *      				                                                                    			} else if("ER".equals(f.getCode())) {             
 *      				                                                                    				procedureModifierCodes.add(f);                  
 *      				                                                                    			} else {                                          
 *      				                                                                    				changed = super.add(f);                         
 *      				                                                                    			}                                                 
 *      				                                                                    			                                                  
 *      				                                                                    			return changed;                                   
 *      				                                                                    		}                                                   
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 *      				                    
 * 5.   getter                  public Fields getRejects() {				                
 *                                  return rejectCodes;      
 *                              }    
 *      
 *    
 * 
 */
	
	public final static char SegmentSeparator = '\u001E';
	public final static char GroupSeparator = '\u001D';
	public final static char FieldSeparator = '\u001C'; 
	public static class Header  extends Segment {
    	protected String data;
		public Header(String header) {
			this.data = header;
		}

		
		  /* (non-Javadoc)
		 * @see ncpdp.Segment#getSegmentId()
		 */
		@Override
		public String getSegmentId() {
			return "TH";
		}


		/* (non-Javadoc)
		 * @see ncpdp.Fields#addData(java.lang.String)
		 */
		@Override
		public void addData(String data) {
			this.data = data;
		}


		/* (non-Javadoc)
		 * @see ncpdp.Fields#addData(java.lang.String, char)
		 */
		@Override
		public void addData(String data, char fieldSeparator) {
			this.data = data;
		}


		/* (non-Javadoc)
		 * @see ncpdp.Fields#add(ncpdp.Field)
		 */
		@Override
		public boolean add(Field f) {
			throw new UnsupportedOperationException("TH");
		}


		/* (non-Javadoc)
		 * @see ncpdp.Fields#addElement(ncpdp.Field)
		 */
		@Override
		public synchronized void addElement(Field field) {
			throw new UnsupportedOperationException("TH");
		}


		/* (non-Javadoc)
		 * @see ncpdp.Fields#addAll(java.util.Collection)
		 */
		@Override
		public synchronized boolean addAll(Collection<? extends Field> c) {
			throw new UnsupportedOperationException("TH");
		}


		/* (non-Javadoc)
		 * @see ncpdp.Fields#getValue(java.lang.String)
		 */
		@Override
		String getValue(String key) {
			return data;
		}


		/* (non-Javadoc)
		 * @see ncpdp.Fields#getField(java.lang.String)
		 */
		@Override
		Field getField(String key) {
			throw new UnsupportedOperationException("TH");
		}


		/* (non-Javadoc)
		 * @see ncpdp.Fields#getIndex(java.lang.String, int)
		 */
		@Override
		int getIndex(String key, int beginIndex) {
			throw new UnsupportedOperationException("TH");
		}


		/* (non-Javadoc)
		 * @see java.util.Vector#add(int, java.lang.Object)
		 */
		@Override
		public void add(int index, Field element) {
			throw new UnsupportedOperationException("TH");
		}


		/* (non-Javadoc)
		 * @see java.util.Vector#addAll(int, java.util.Collection)
		 */
		@Override
		public synchronized boolean addAll(int index,
				Collection<? extends Field> c) {
			throw new UnsupportedOperationException("TH");
		}


		/** get BIN Number.
		   * Card Issuer ID or Bank ID Number used for network routing.
		   * 
		   * @return BIN Number
		   */
		  public String getBINNumber() {
		   return data.substring(0,6);
		  }
		
		  /** get Version/Release Number.
		   * Code uniquely identifying the transmission syntax and corresponding Data Dictionary.
		   * 
		   * @return Version/Release Number
		   */
		  public String getVersionReleaseNumber() {
		   return data.substring(6,8);
		  }
		
		  /** get Transaction Code.
		   * Code identifying the type of transaction.
		   * 
		   * @return Transaction Code
		   */
		  public String getTransactionCode() {
		   return data.substring(8,10);
		  }
		
		  /** get Processor Control Number.
		   * Number assigned by the processor.
		   * 
		   * @return Processor Control Number
		   */
		  public String getProcessorControlNumber() {
		   return data.substring(10,20);
		  }
		
		  /** get Transaction Count.
		   * Count of transactions in the transmission.
		   * 
		   * @return Transaction Count
		   */
		  public String getTransactionCount() {
		   return data.substring(20,21);
		  }
		
		  /** get Service Provider ID Qualifier.
		   * Code qualifying the ‘Service Provider ID’ (201-B1).
		   * 
		   * @return  Service Provider ID Qualifier
		   */
		  public String getServiceProviderIDQualifier() {
		   return data.substring(21,23);
		  }
		
		  /** get Service Provider ID.
		   * ID assigned to a pharmacy or provider.
		   * 
		   * @return Service Provider ID
		   */
		  public String getServiceProviderID() {
		   return data.substring(23,38);
		  }
		
		  /** get Date Of Service.
		   * Identifies date the prescription was filled or professional service rendered.
		   * 
		   * @return Date Of Service
		   */
		  public String getDateOfService() {
		   return data.substring(38,46);
		  }
		
		  /** get Software Vendor/ Certification ID.
		   * ID assigned by the switch or processor to identify the software source.
		   * 
		   * @return Software Vendor/ Certification ID
		   */
		  public String getSoftwareVendorCertificationID() {
		   return data.substring(46,56);
		  }
		
		
	}
    public static class Patient extends Segment{
    	public Patient() {}
		public Patient(String patient) {
			super(patient);
		}
		
        /** (QUALIFIER) Patient ID Qualifier at 331-CX. Code qualifying the ‘Patient ID’ (332-CY).
         * 
         * @return Patient ID Qualifier
         */
        public String getPatientIDQualifier() {
            return getValue("CX");
        }

		
		/** Patient ID at 332-CY. ID assigned to the patient.
		 * 
		 * @return Patient ID
		 */
		public String getPatientID() {
			return getValue("CY");
		}

		/** Date Of Birth at 304-C4. Date of birth of patient.
		 * 
		 * @return Date Of Birth
		 */
		public String getDateOfBirth() {
			return getValue("C4");
		}		
		
        /** Patient Gender Code at 305-C5. Code indicating the gender of the individual..
         * 
         * @return Patient Gender Code
         */
        public String getPatientGenderCode() {
            return getValue("C5");
        }

        /** Patient First Name at 310-CA. Individual first name..
         * 
         * @return Patient First Name
         */
        public String getPatientFirstName() {
            return getValue("CA");
        }
        /** Patient Last Name at 311-CB. Individual last name..
         * 
         * @return Patient Last Name
         */
        public String getPatientLastName() {
            return getValue("CB");
        }
        /** Patient Street Address at 322-CM. Free-form text for address information..
         * 
         * @return Patient Street Address
         */
        public String getPatientStreetAddress() {
            return getValue("CM");
        }
        /** Patient City Address at 323-CN. Free-form text for city name..
         * 
         * @return Patient City Address
         */
        public String getPatientCityAddress() {
            return getValue("CN");
        }
        /** Patient State/Province Address at 324-CO. Standard State/Province Code as defined by appropriate government agency..
         * 
         * @return Patient State/Province Address
         */
        public String getPatientStateProvinceAddress() {
            return getValue("CO");
        }
        /** Patient Zip/Postal Zone at 325-CP. Code defining international postal zone excluding punctuation and blanks (zip code for US)..
         * 
         * @return Patient Zip/Postal Zone
         */
        public String getPatientZipPostalZone() {
            return getValue("CP");
        }
        /** Patient Phone Number at 326-CQ. Ten-digit phone number of patient..
         * 
         * @return Patient Phone Number
         */
        public String getPatientPhoneNumber() {
            return getValue("CQ");
        }
        /** Place of Service at 307-C7. Code identifying the place where a drug or service is dispensed or administered..
         * 
         * @return Place of Service
         */
        public String getPlaceofService() {
            return getValue("C7");
        }
        /** Employer ID at 333-CZ. ID assigned to employer..
         * 
         * @return Employer ID
         */
        public String getEmployerID() {
            return getValue("CZ");
        }
        /** Smoker/Non-Smoker Code at 334-1C. Code indicating the patient as a smoker or non-smoker..
         * 
         * @return Smoker/Non-Smoker Code
         */
        public String getSmokerNonSmokerCode() {
            return getValue("1C");
        }
        /** Pregnancy Indicator at 335-2C. Code indicating the patient as pregnant or non-pregnant..
         * 
         * @return Pregnancy Indicator
         */
        public String getPregnancyIndicator() {
            return getValue("2C");
        }
        /** Patient E-Mail Address at 350-HN. The E-Mail address of the patient (member)..
         * 
         * @return Patient E-Mail Address
         */
        public String getPatientEMailAddress() {
            return getValue("HN");
        }
        /** Patient Residence at 384-4X. Code identifying the patient’s place of residence..
         * 
         * @return Patient Residence
         */
        public String getPatientResidence() {
            return getValue("4X");
        }

		
	}
    public static class Insurance extends Segment{
		public Insurance() {}
		public Insurance(String insurance) {
			super(insurance);
		}
		
        /** Cardholder ID at 302-C2. Insurance ID assigned to the cardholder..
         * 
         * @return Cardholder ID
         */
        public String getCardholderID() {
            return getValue("C2");
        }
        /** Cardholder First Name at 312-CC. Individual first name..
         * 
         * @return Cardholder First Name
         */
        public String getCardholderFirstName() {
            return getValue("CC");
        }
        /** Cardholder Last Name at 313-CD. Individual last name..
         * 
         * @return Cardholder Last Name
         */
        public String getCardholderLastName() {
            return getValue("CD");
        }
        /** Home Plan at 314-CE. Code identifying the Blue Cross or Blue Shield plan ID which indicates where the member’s coverage has been designated. Usually where the member lives or purchased their coverage..
         * 
         * @return Home Plan
         */
        public String getHomePlan() {
            return getValue("CE");
        }
        /** Plan ID at 524-FO. Assigned by the processor to identify a set of parameters, benefit, or coverage criteria used to adjudicate a claim..
         * 
         * @return Plan ID
         */
        public String getPlanID() {
            return getValue("FO");
        }
        /** Eligibility Clarification Code at 309-C9. Code indicating that the pharmacy is clarifying eligibility based on receiving a denial..
         * 
         * @return Eligibility Clarification Code
         */
        public String getEligibilityClarificationCode() {
            return getValue("C9");
        }
        /** Facility ID at 336-8C. ID assigned to the patient’s clinic/host party..
         * 
         * @return Facility ID
         */
        public String getFacilityID() {
            return getValue("8C");
        }
        /** Group ID at 301-C1. ID assigned to the cardholder group or employer group..
         * 
         * @return Group ID
         */
        public String getGroupID() {
            return getValue("C1");
        }
        /** Person Code at 303-C3. Code assigned to a specific person within a family..
         * 
         * @return Person Code
         */
        public String getPersonCode() {
            return getValue("C3");
        }
        /** Patient Relationship Code at 306-C6. Code indicating relationship of patient to cardholder..
         * 
         * @return Patient Relationship Code
         */
        public String getPatientRelationshipCode() {
            return getValue("C6");
        }

	}
    public static class ProcedureModifierCodes extends Fields{}
    public static class SubmissionClarificationCodes extends Fields{}
    
    public static class Claim extends Segment{
    	ProcedureModifierCodes procedureModifierCodes = new ProcedureModifierCodes();
    	SubmissionClarificationCodes submissionClarificationCodes = new SubmissionClarificationCodes();
    	public Claim() {}
    	
		public Claim(String claim) {
			super();
			addData(claim);
		}
		
		
		/* (non-Javadoc)
		 * @see ncpdp.Fields#add(ncpdp.Field)
		 * SE ER
		 * NX DK
		 */
		@Override
		public boolean add(Field f) {
			boolean changed = true;
			if("SE".equals(f.getCode())) {
				int capacity = 10;
				try {
					capacity = Integer.parseInt(f.getValue());
				}catch(Exception ignore) {}
				procedureModifierCodes.ensureCapacity(capacity);
				changed = super.add(f);
			} else if("ER".equals(f.getCode())) {
				procedureModifierCodes.add(f);
			} else if("NX".equals(f.getCode())) {
				int capacity = 10;
				try {
					capacity = Integer.parseInt(f.getValue());
				}catch(Exception ignore) {}
				submissionClarificationCodes.ensureCapacity(capacity);
				changed = super.add(f);
			} else if("DK".equals(f.getCode())) {
				submissionClarificationCodes.add(f);
			} else {
				changed = super.add(f);
			}
			
			return changed;
		}


				/** (QUALIFIER) Prescription/ Service Reference Number Qualifier at 455-EM. Indicates the type of billing submitted.
		         * 
		         * @return Prescription/ Service Reference Number Qualifier
		         */
		        public String getPrescriptionServiceReferenceNumberQualifier() {
		            return getValue("EM");
		        }



		        /** Prescription/ Service Reference Number at 402-D2. Reference number assigned by the provider for the dispensed drug/product and/or service provided..
		         * 
		         * @return Prescription/ Service Reference Number
		         */
		        public String getPrescriptionServiceReferenceNumber() {
		            return getValue("D2");
		        }

		        /** (QUALIFIER) Product/Service ID Qualifier at 436-E1. Code qualifying the value in 'Product/Service ID' (407-D7).
		         * 
		         * @return Product/Service ID Qualifier
		         */
		        public String getProductServiceIDQualifier() {
		            return getValue("E1");
		        }

		        /** Product/Service ID at 407-D7. ID of the product dispensed or service provided..
		         * 
		         * @return Product/Service ID
		         */
		        public String getProductServiceID() {
		            return getValue("D7");
		        }


		        /** Associated Prescription/ Service Reference Number at 456-EN. Related ‘Prescription/Service Reference Number’ (402-D2) to which the service is associated..
		         * 
		         * @return Associated Prescription/ Service Reference Number
		         */
		        public String getAssociatedPrescriptionServiceReferenceNumber() {
		            return getValue("EN");
		        }


		        /** Associated Prescription/ Service Date at 457-EP. Date of the ‘Associated Prescription/Service Reference Number’ (456-EN)..
		         * 
		         * @return Associated Prescription/ Service Date
		         */
		        public String getAssociatedPrescriptionServiceDate() {
		            return getValue("EP");
		        }





		        /** Procedure Modifier Code at 459-ER. Identifies special circumstances related to the performance of the service..
		         * 
		         * @return Procedure Modifier Code
		         */
//		        public String getProcedureModifierCodes() {
//		            return getValue("ER");
//		        }
		        public Fields getProcedureModifierCodes() {
		            return procedureModifierCodes;     
		        }                           


		        /** Quantity Dispensed at 442-E7. Quantity dispensed expressed in metric decimal units..
		         * 
		         * @return Quantity Dispensed
		         */
		        public String getQuantityDispensed() {
		            return getValue("E7");
		        }


		        /** Fill Number at 403-D3. The code indicating whether the prescription is an original or a refill..
		         * 
		         * @return Fill Number
		         */
		        public String getFillNumber() {
		            return getValue("D3");
		        }


		        /** Days Supply at 405-D5. Estimated number of days the prescription will last..
		         * 
		         * @return Days Supply
		         */
		        public String getDaysSupply() {
		            return getValue("D5");
		        }


		        /** Compound Code at 406-D6. Code indicating whether or not the prescription is a compound..
		         * 
		         * @return Compound Code
		         */
		        public String getCompoundCode() {
		            return getValue("D6");
		        }


		        /** Dispense As Written (DAW)/ Product Selection Code at 408-D8. Code indicating whether or not the prescriber’s instructions regarding generic substitution were followed..
		         * 
		         * @return Dispense As Written (DAW)/ Product Selection Code
		         */
		        public String getDispenseAsWrittenProductSelectionCode() {
		            return getValue("D8");
		        }


		        /** Date Prescription Written at 414-DE. Date prescription was written..
		         * 
		         * @return Date Prescription Written
		         */
		        public String getDatePrescriptionWritten() {
		            return getValue("DE");
		        }


		        /** Number of Refills Authorized at 415-DF. Number of refills authorized by the prescriber..
		         * 
		         * @return Number of Refills Authorized
		         */
		        public String getNumberofRefillsAuthorized() {
		            return getValue("DF");
		        }


		        /** Prescription Origin Code at 419-DJ. Code indicating the origin of the prescription..
		         * 
		         * @return Prescription Origin Code
		         */
		        public String getPrescriptionOriginCode() {
		            return getValue("DJ");
		        }





		        /** Submission Clarification Code at 420-DK. Code indicating that the pharmacist is clarifying the submission..
		         * 
		         * @return Submission Clarification Code
		         */
//		        public String getSubmissionClarificationCode() {
//		            return getValue("DK");
//		        }
		        public Fields getSubmissionClarificationCode() {
		        	return submissionClarificationCodes;
		        }


		        /** Quantity Prescribed at 460-ET. Amount expressed in metric decimal units..
		         * 
		         * @return Quantity Prescribed
		         */
		        public String getQuantityPrescribed() {
		            return getValue("ET");
		        }


		        /** Other Coverage Code at 308-C8. Code indicating whether or not the patient has other insurance coverage..
		         * 
		         * @return Other Coverage Code
		         */
		        public String getOtherCoverageCode() {
		            return getValue("C8");
		        }


		        /** Unit Dose Indicator at 429-DT. Code indicating the type of unit dose dispensing..
		         * 
		         * @return Unit Dose Indicator
		         */
		        public String getUnitDoseIndicator() {
		            return getValue("DT");
		        }
		        

		        /** (QUALIFIER) Originally Prescribed Product/Service ID Qualifier at 453-EJ. Code qualifying the value in 'Originally Prescribed Product/Service Code’ (Field 445-EA).
		         * 
		         * @return Originally Prescribed Product/Service ID Qualifier
		         */
		        public String getOriginallyPrescribedProductServiceIDQualifier() {
		            return getValue("EJ");
		        }

		        /** Originally Prescribed Product/Service Code at 445-EA. Code of the initially prescribed product or service..
		         * 
		         * @return Originally Prescribed Product/Service Code
		         */
		        public String getOriginallyPrescribedProductServiceCode() {
		            return getValue("EA");
		        }


		        /** Originally Prescribed Quantity at 446-EB. Product initially prescribed amount expressed in metric decimal units..
		         * 
		         * @return Originally Prescribed Quantity
		         */
		        public String getOriginallyPrescribedQuantity() {
		            return getValue("EB");
		        }


		        /** Alternate ID at 330-CW. Person identifier to be used for controlled product reporting. Identifier may be that of the patient or the person picking up the prescription as required by the governing body..
		         * 
		         * @return Alternate ID
		         */
		        public String getAlternateID() {
		            return getValue("CW");
		        }


		        /** Scheduled Prescription ID Number at 454-EK. The serial number of the prescription blank/form..
		         * 
		         * @return Scheduled Prescription ID Number
		         */
		        public String getScheduledPrescriptionIDNumber() {
		            return getValue("EK");
		        }


		        /** Unit Of Measure at 600-28. NCPDP standard product billing codes..
		         * 
		         * @return Unit Of Measure
		         */
		        public String getUnitOfMeasure() {
		            return getValue("28");
		        }


		        /** Level Of Service at 418-DI. Coding indicating the type of service the provider rendered..
		         * 
		         * @return Level Of Service
		         */
		        public String getLevelOfService() {
		            return getValue("DI");
		        }


		        /** Prior Authorization Type Code at 461-EU. Code clarifying the ‘Prior Authorization Number Submitted’ (462-EV) or benefit/plan exemption..
		         * 
		         * @return Prior Authorization Type Code
		         */
		        public String getPriorAuthorizationTypeCode() {
		            return getValue("EU");
		        }


		        /** Prior Authorization Number Submitted at 462-EV. Number submitted by the provider to identify the prior authorization..
		         * 
		         * @return Prior Authorization Number Submitted
		         */
		        public String getPriorAuthorizationNumberSubmitted() {
		            return getValue("EV");
		        }


		        /** Intermediary Authorization Type ID at 463-EW. Value indicating that authorization occurred for intermediary processing..
		         * 
		         * @return Intermediary Authorization Type ID
		         */
		        public String getIntermediaryAuthorizationTypeID() {
		            return getValue("EW");
		        }


		        /** Intermediary Authorization ID at 464-EX. Value indicating intermediary authorization occurred..
		         * 
		         * @return Intermediary Authorization ID
		         */
		        public String getIntermediaryAuthorizationID() {
		            return getValue("EX");
		        }


		        /** Dispensing Status at 343-HD. Code indicating the quantity dispensed is a partial fill or the completion of a partial fill. Used only in situations where inventory shortages do not allow the full quantity to be dispensed..
		         * 
		         * @return Dispensing Status
		         */
		        public String getDispensingStatus() {
		            return getValue("HD");
		        }


		        /** Quantity Intended To Be Dispensed at 344-HF. Metric decimal quantity of medication that would be dispensed on original filling if inventory were available. Used in association with a ‘P’ or ‘C’ in ‘Dispensing Status’ (343­HD)..
		         * 
		         * @return Quantity Intended To Be Dispensed
		         */
		        public String getQuantityIntendedToBeDispensed() {
		            return getValue("HF");
		        }


		        /** Days Supply Intended To Be Dispensed at 345-HG. Days supply for metric decimal quantity of medication that would be dispensed on original dispensing if inventory were available. Used in association with a ‘P’ or ‘C’ in ‘Dispensing Status’ (343-HD)..
		         * 
		         * @return Days Supply Intended To Be Dispensed
		         */
		        public String getDaysSupplyIntendedToBeDispensed() {
		            return getValue("HG");
		        }


		        /** Delay Reason Code at 357-NV. Code to specify the reason that submission of the transactions has been delayed..
		         * 
		         * @return Delay Reason Code
		         */
		        public String getDelayReasonCode() {
		            return getValue("NV");
		        }

		
	}
    public static class PharmacyProvider extends Segment{
    	public PharmacyProvider() {}
		public PharmacyProvider(String pharmacyProvider) {
			super(pharmacyProvider);
		}
        /** (QUALIFIER) Provider ID Qualifier at 465-EY. Code qualifying the ‘Provider ID’ (444-E9).
         * 
         * @return Provider ID Qualifier
         */
        public String getProviderIDQualifier() {
            return getValue("EY");
        }

		/** Provider ID at 444-E9. Unique ID assigned to the person responsible for the dispensing of the prescription or provision of the service..
         * 
         * @return Provider ID
         */
        public String getProviderID() {
            return getValue("E9");
        }
	}
    public static class Prescriber extends Segment{
    	public Prescriber() {}
		public Prescriber(String prescriber) {
			super(prescriber);
		}
		
        /** (QUALIFIER) Prescriber ID Qualifier at 466-EZ. Code qualifying the ‘Prescriber ID’ (411-DB).
         * 
         * @return Prescriber ID Qualifier
         */
        public String getPrescriberIDQualifier() {
            return getValue("EZ");
        }

        /** Prescriber ID at 411-DB. ID assigned to the prescriber..
         * 
         * @return Prescriber ID
         */
        public String getPrescriberID() {
            return getValue("DB");
        }
        /** Prescriber Location Code at 467-1E. Location address code assigned to the prescriber as identified in the National Provider System (NPS)..
         * 
         * @return Prescriber Location Code
         */
        public String getPrescriberLocationCode() {
            return getValue("1E");
        }
        /** Prescriber Last Name at 427-DR. Individual last name..
         * 
         * @return Prescriber Last Name
         */
        public String getPrescriberLastName() {
            return getValue("DR");
        }
        /** Prescriber Phone Number at 498-PM. Ten-digit phone number of the prescriber..
         * 
         * @return Prescriber Phone Number
         */
        public String getPrescriberPhoneNumber() {
            return getValue("PM");
        }
        /** (QUALIFIER) Primary Care Provider ID Qualifier at 468-2E. Code qualifying the ‘Primary Care Provider ID’ (421-DL).
         * 
         * @return Primary Care Provider ID Qualifier
         */
        public String getPrimaryCareProviderIDQualifier() {
            return getValue("2E");
        }
        /** Primary Care Provider ID at 421-DL. ID assigned to the primary care provider. Used when the patient is referred to a secondary care provider..
         * 
         * @return Primary Care Provider ID
         */
        public String getPrimaryCareProviderID() {
            return getValue("DL");
        }
        /** Primary Care Provider Location Code at 469-H5. Location address code assigned to the primary care provider as identified in the National Provider System (NPS)..
         * 
         * @return Primary Care Provider Location Code
         */
        public String getPrimaryCareProviderLocationCode() {
            return getValue("H5");
        }
        /** Primary Care Provider Last Name at 470-4E. Individual last name..
         * 
         * @return Primary Care Provider Last Name
         */
        public String getPrimaryCareProviderLastName() {
            return getValue("4E");
        }

	}
    /** Other Payer Coverage Type at 338-5C.
     *  Each 5C describes an other payment
     * @author tjkelly
     *    
     */
    public static class OtherPayments {
    	
    }
    static void copyField(String code, Fields source, Fields dest) {
    	Field f = source.getField(code);
    	if(f!= null) {
    		dest.add(f);
    	}
    }
    public static class COB_OtherPayments extends Segment{
    	private Vector<Detail> details = new Vector<Detail>(5); 
    	public COB_OtherPayments() {}
		public COB_OtherPayments(String segment) {
//			Detail fields = new Detail(segment);
//			copyField("AM", fields , this);
//			copyField("4C", fields, this);
//			{// 
//				int capacity = details.capacity();
//				try {
//					capacity = Integer.getInteger(getCOB_OtherPaymentsCount());
//				} catch(Exception ignore) {}
//				details = new Vector<Detail>(capacity);
//			}
//			Detail detail = null;
//			for(Field f : fields) {
//				if("5C".equals(f.getCode())){
//					detail= new Detail();
//					details.add(detail);
//				}
//				if(detail != null) {
//					detail.add(f);
//				}
//			}
			addData(segment);
		}
		public boolean add(Field f) {
			boolean changed = true;
			if ("4C".equals(f.getCode())) {
				int capacity = 10;
				try {
					capacity = Integer.parseInt(f.getValue());
				} catch(Exception ignore) {
					ignore.printStackTrace();
				}
				details.ensureCapacity(capacity);
				changed = super.add(f);
			}else if("5C".equals(f.getCode())) {
				Detail detail = new Detail();
				details.add(detail);
				changed = detail.add(f);
			}else if(details.isEmpty() == false) {
				Detail last = details.lastElement();
				changed = last.add(f);
			}else {
				changed = super.add(f);
			}
			return changed;
		}		

		/**
		 * @return the details
		 */
		public List<Detail> getDetails() {
			return details;
		}

		public String getCOB_OtherPaymentsCount() {
			return getValue("4C");
		}


		public static class Detail extends Fields{
            Vector<QualifiedAmount> amountsPaid = new Vector<QualifiedAmount>(5);
            RejectCodes rejectCodes = new RejectCodes();
            Vector<PatientResponsibilityAmount> patientResponsibilityAmounts = new Vector<PatientResponsibilityAmount>();
            
     
            
			public Detail() {
				super();
			}

			public Detail(String segment) {
				addData(segment);
			}

			/* (non-Javadoc)
			 * @see java.util.LinkedList#add(java.lang.Object)
			 */
			@Override
			public boolean add(Field f) {
				boolean changed = true;
				if ("HB".equals(f.getCode())) {
					int capacity = 10;
					try {
						capacity = Integer.parseInt(f.getValue());
					} catch(Exception ignore) {}
					amountsPaid.ensureCapacity(capacity);
					changed = super.add(f);
				}else if("HC".equals(f.getCode())) {
					QualifiedAmount amount = new QualifiedAmount();
					amount.add(f);
					amountsPaid.add(amount);
				}else if("DV".equals(f.getCode())) {
					if(amountsPaid.isEmpty() == false) {
					    amountsPaid.lastElement().add( f);
					}
				}else if("5E".equals(f.getCode())){
					int capacity = 10;
					try {
						capacity = Integer.parseInt(f.getValue());
					} catch(Exception ignore) {}
					rejectCodes.ensureCapacity(capacity);
					changed = super.add(f);
				}  else if("6E".equals(f.getCode())){
					rejectCodes.add(f);
				} else if("NR".equals(f.getCode())){
					int capacity = 10;
					try {
						capacity = Integer.parseInt(f.getValue());
					} catch(Exception ignore) {}
					patientResponsibilityAmounts.ensureCapacity(capacity);
					changed = super.add(f);
				}  else if("NP".equals(f.getCode())){
					PatientResponsibilityAmount amount = new PatientResponsibilityAmount();
					amount.add(f);
					patientResponsibilityAmounts.add(amount);
				}  else if("NQ".equals(f.getCode())){
					if(patientResponsibilityAmounts.isEmpty()==false) {
						patientResponsibilityAmounts.lastElement().add(f);
					}
				} 
				else {
					changed = super.add(f);
				}
				return changed;
			}



			/** Other Payer Coverage Type at 338-5C. Code identifying the type of ‘Other Payer ID’ (340-7C)..
			 * 
			 * @return Other Payer Coverage Type
			 */
			public String getOtherPayerCoverageType() {
			    return getValue("5C");
			}

			/** (QUALIFIER) Other Payer ID Qualifier at 339-6C. Code qualifying the ‘Other Payer ID’ (340-7C).
			 * 
			 * @return Other Payer ID Qualifier
			 */
			public String getOtherPayerIDQualifier() {
			    return getValue("6C");
			}

			/** Other Payer ID at 340-7C. ID assigned to the payer..
			 * 
			 * @return Other Payer ID
			 */
			public String getOtherPayerID() {
			    return getValue("7C");
			}

			/** Other Payer Date at 443-E8. Payment or denial date of the claim submitted to the other payer. Used for coordination of benefits..
			 * 
			 * @return Other Payer Date
			 */
			public String getOtherPayerDate() {
			    return getValue("E8");
			}
			
			/**
			 * @return the amountsPaid
			 */
			public Vector<QualifiedAmount> getAmountsPaid() {
				return amountsPaid;
			}
			/** (QUALIFIER) Other Payer Amount Paid Qualifier at 342-HC. Code qualifying the ‘Other Payer Amount Paid’ (431-DV).
			 * 
			 * @return Other Payer Amount Paid Qualifier
			 */
//			public String getOtherPayerAmountPaidQualifier() {
//			    return getValue("HC");
//			}

			/** Other Payer Amount Paid at 431-DV. Amount of any payment known by the pharmacy from other sources (including coupons)..
			 * 
			 * @return Other Payer Amount Paid
			 */
//			public String getOtherPayerAmountPaid() {
//			    return getValue("DV");
//			}
		   public Fields getRejects() {
			    return rejectCodes;
			}
			/** Other Payer Reject Code at 472-6E. The error encountered by the previous “Other Payer” in ‘Reject Code’ (511-FB)..
			 * 
			 * @return Other Payer Reject Code
			 */
//			public String getOtherPayerRejectCode() {
//			    return getValue("6E");
//			}
           public Vector<PatientResponsibilityAmount> getPatientResponsibilityAmounts() {
        	   return patientResponsibilityAmounts;
           }
			/** (QUALIFIER) Other Payer-Patient Responsibility Amount Qualifier at 351-NP. Code qualifying the “Other Payer-Patient Responsibility Amount (352-NQ)”.
			 * 
			 * @return Other Payer-Patient Responsibility Amount Qualifier
			 */
			public String getOtherPayerPatientResponsibilityAmountQualifier() {
			    return getValue("NP");
			}

			/** Other Payer-Patient Responsibility Amount at 352-NQ. The patient’s cost share from a previous payer..
			 * 
			 * @return Other Payer-Patient Responsibility Amount
			 */
			public String getOtherPayerPatientResponsibilityAmount() {
			    return getValue("NQ");
			}
			
		}
		






		
	}
    public static class QualifiedAmount extends Fields{
    	/**
		 * @return the qualifier
		 */
		public String getQualifier() {
			return getValue("HC");
		}
		/**
		 * @return the amount
		 */
		public String getAmount() {
			return getValue("DV");
		}
    }
    public static class PatientResponsibilityAmount extends Fields{
    	/**
		 * @return the qualifier
		 */
		public String getQualifier() {
			return getValue("NP");
		}
		/**
		 * @return the amount
		 */
		public String getAmount() {
			return getValue("NQ");
		}	
    }
    public static class RejectCodes extends Fields{}
    
    
    public static class WorkersCompensation extends Segment{
    	public WorkersCompensation() {}
		public WorkersCompensation(String workersCompensation) {
			super(workersCompensation);
		}



		        /** Date Of Injury at 434-DY. Date on which the injury occurred..
		         * 
		         * @return Date Of Injury
		         */
		        public String getDateOfInjury() {
		            return getValue("DY");
		        }


		        /** Employer Name at 315-CF. Complete name of employer..
		         * 
		         * @return Employer Name
		         */
		        public String getEmployerName() {
		            return getValue("CF");
		        }


		        /** Employer Street Address at 316-CG. Free-form text for address information..
		         * 
		         * @return Employer Street Address
		         */
		        public String getEmployerStreetAddress() {
		            return getValue("CG");
		        }


		        /** Employer City Address at 317-CH. Free-form text for city name..
		         * 
		         * @return Employer City Address
		         */
		        public String getEmployerCityAddress() {
		            return getValue("CH");
		        }


		        /** Employer State/ Province Address at 318-CI. Standard State/Province Code as defined by appropriate government agency..
		         * 
		         * @return Employer State/ Province Address
		         */
		        public String getEmployerStateProvinceAddress() {
		            return getValue("CI");
		        }


		        /** Employer Zip/ Postal Zone at 319-CJ. Code defining international postal zone excluding punctuation and blanks (zip code for US)..
		         * 
		         * @return Employer Zip/ Postal Zone
		         */
		        public String getEmployerZipPostalZone() {
		            return getValue("CJ");
		        }


		        /** Employer Phone Number at 320-CK. Ten-digit phone number of employer..
		         * 
		         * @return Employer Phone Number
		         */
		        public String getEmployerPhoneNumber() {
		            return getValue("CK");
		        }


		        /** Employer Contact Name at 321-CL. Employer primary contact..
		         * 
		         * @return Employer Contact Name
		         */
		        public String getEmployerContactName() {
		            return getValue("CL");
		        }


		        /** Carrier ID at 327-CR. Carrier code assigned in Worker’s Compensation Program..
		         * 
		         * @return Carrier ID
		         */
		        public String getCarrierID() {
		            return getValue("CR");
		        }


		        /** Claim/Reference ID at 435-DZ. Identifies the claim number assigned by Worker’s Compensation Program..
		         * 
		         * @return Claim/Reference ID
		         */
		        public String getClaimReferenceID() {
		            return getValue("DZ");
		        }


	}
    public static class DUR_PPS extends Segment{
    	public static class Service extends Fields{
    		/** DUR/PPS Code Counter. Counter number for each DUR/PPS set/logical grouping.
    		 * 
    		 * @return DUR/PPS Code Counter.
    		 */
    		public String getCounter() {
    			return getValue("7E");
    		}

			/** Reason For Service Code at 439-E4. Code identifying the type of utilization conflict detected or the reason for the pharmacist’s professional service..
			 * 
			 * @return Reason For Service Code
			 */
			public String getReasonForServiceCode() {
			    return getValue("E4");
			}

			/** Professional Service Code at 440-E5. Code identifying pharmacist intervention when a conflict code has been identified or service has been rendered..
			 * 
			 * @return Professional Service Code
			 */
			public String getProfessionalServiceCode() {
			    return getValue("E5");
			}

			/** Result of Service Code at 441-E6. Action taken by a pharmacist in response to a conflict or the result of a pharmacist’s professional service..
			 * 
			 * @return Result of Service Code
			 */
			public String getResultofServiceCode() {
			    return getValue("E6");
			}

			/** DUR/PPS Level Of Effort at 474-8E. Code indicating the level of effort as determined by the complexity of decision making or resources utilized by a pharmacist to perform a professional service..
			 * 
			 * @return DUR/PPS Level Of Effort
			 */
			public String getDURPPSLevelOfEffort() {
			    return getValue("8E");
			}

			/** (QUALIFIER) DUR Co-Agent ID Qualifier at 475-J9. Code qualifying the value in ‘DUR Co-Agent ID’ (476-H6).
			 * 
			 * @return DUR Co-Agent ID Qualifier
			 */
			public String getDURCoAgentIDQualifier() {
			    return getValue("J9");
			}

			/** DUR Co-Agent ID at 476-H6. Identifies the co-existing agent contributing to the DUR event (drug or disease conflicting with the prescribed drug or prompting pharmacist professional service)..
			 * 
			 * @return DUR Co-Agent ID
			 */
			public String getDURCoAgentID() {
			    return getValue("H6");
			}
    		
    	}

		Vector<Service> services = new Vector<Service>();
		public DUR_PPS() {}
		public DUR_PPS(String dur_pps) {
			addData(dur_pps);
		}
		/* (non-Javadoc)
		 * @see ncpdp.Fields#add(ncpdp.Field)
		 * 7E E4 E5 E6 8E J9 H6
		 */
		@Override
		public boolean add(Field f) {
			boolean changed;
			if("7E".equals(f.getCode())) {
				Service service = new Service();
				services.add(service);
				changed = service.add(f);
			}else {
				Service last = services.lastElement();
				changed = last.add(f);
			}
			
			return changed;
		}
		/**
		 * @return the services
		 */
		public Vector<Service> getServices() {
			return services;
		}
		
		
	}
    
    
    public static class OtherAmountsClaimedSubmitted extends Fields {
        /** (QUALIFIER) Other Amount Claimed Submitted Qualifier at 479-H8. Code identifying the additional incurred cost claimed in ‘Other Amount Claimed Submitted’ (480-H9).
         * 
         * @return Other Amount Claimed Submitted Qualifier
         */
        public String getQualifier() {
            return getValue("H8");
        }

        /** Other Amount Claimed Submitted at 480-H9. Amount representing the additional incurred costs for a dispensed prescription or service..
         * 
         * @return Other Amount Claimed Submitted
         */
        public String getAmount() {
            return getValue("H9");
        }
    }
    public static class Pricing extends Segment{
    	OtherAmountsClaimedSubmitted otherAmountsClaimedSubmitted = new OtherAmountsClaimedSubmitted();
    	public Pricing() {}
		public Pricing(String pricing) {
			addData(pricing);
		}
		/* (non-Javadoc)                                    
		 * @see ncpdp.Fields#add(ncpdp.Field)               
		 * H7 H8 H9                                            
		 */                                                 
		@Override                                           
		public boolean add(Field f) {                       
			boolean changed = true;                           
			if("H7".equals(f.getCode())) {                    
				int capacity = 10;                              
				try {                                           
					capacity = Integer.parseInt(f.getValue());    
				}catch(Exception ignore) {}                     
				otherAmountsClaimedSubmitted.ensureCapacity(capacity);
				changed = super.add(f);                         
			} else if("H8".equals(f.getCode()) 
			       || "H9".equals(f.getCode())) {             
				otherAmountsClaimedSubmitted.add(f);                  
			} else {                                          
				changed = super.add(f);                         
			}                                                 
			                                                  
			return changed;                                   
		}                                                   


        /** Ingredient Cost Submitted at 409-D9. Submitted product component cost of the dispensed prescription. This amount is included in the 'Gross Amount Due' (430-DU)..
         * 
         * @return Ingredient Cost Submitted
         */
        public String getIngredientCostSubmitted() {
            return getValue("D9");
        }
        /** Dispensing Fee Submitted at 412-DC. Dispensing fee submitted by the pharmacy. This amount is included in the 'Gross Amount Due' (430-DU)..
         * 
         * @return Dispensing Fee Submitted
         */
        public String getDispensingFeeSubmitted() {
            return getValue("DC");
        }
        /** Professional Service Fee Submitted at 477-BE. Amount submitted by the provider for professional services rendered..
         * 
         * @return Professional Service Fee Submitted
         */
        public String getProfessionalServiceFeeSubmitted() {
            return getValue("BE");
        }
        /** Patient Paid Amount Submitted at 433-DX. Amount the pharmacy received from the patient for the prescription dispensed..
         * 
         * @return Patient Paid Amount Submitted
         */
        public String getPatientPaidAmountSubmitted() {
            return getValue("DX");
        }
        /** Incentive Amount Submitted at 438-E3. Amount represents a fee that is submitted by the pharmacy for contractually agreed upon services. This amount is included in the 'Gross Amount Due' (430-DU)..
         * 
         * @return Incentive Amount Submitted
         */
        public String getIncentiveAmountSubmitted() {
            return getValue("E3");
        }
        public OtherAmountsClaimedSubmitted getOtherAmountsClaimedSubmitted() {
        	return otherAmountsClaimedSubmitted;
        }


        /** Flat Sales Tax Amount Submitted at 481-HA. Flat sales tax submitted for prescription. This amount is included in the ‘Gross Amount Due’ (430-DU)..
         * 
         * @return Flat Sales Tax Amount Submitted
         */
        public String getFlatSalesTaxAmountSubmitted() {
            return getValue("HA");
        }
        /** Percentage Sales Tax Amount Submitted at 482-GE. Percentage sales tax submitted..
         * 
         * @return Percentage Sales Tax Amount Submitted
         */
        public String getPercentageSalesTaxAmountSubmitted() {
            return getValue("GE");
        }
        /** Percentage Sales Tax Rate Submitted at 483-HE. Percentage sales tax rate used to calculate ‘Percentage Sales Tax Amount Submitted’ (482­GE)..
         * 
         * @return Percentage Sales Tax Rate Submitted
         */
        public String getPercentageSalesTaxRateSubmitted() {
            return getValue("HE");
        }
        /** Percentage Sales Tax Basis Submitted at 484-JE. Code indicating the basis for percentage sales tax..
         * 
         * @return Percentage Sales Tax Basis Submitted
         */
        public String getPercentageSalesTaxBasisSubmitted() {
            return getValue("JE");
        }
        /** Usual and Customary Charge at 426-DQ. Amount charged cash customers for the prescription exclusive of sales tax or other amounts claimed..
         * 
         * @return Usual and Customary Charge
         */
        public String getUsualandCustomaryCharge() {
            return getValue("DQ");
        }
        /** Gross Amount Due at 430-DU. Total price claimed from all sources. For prescription claim request, field represents a sum of ‘Ingredient Cost Submitted’ (409-D9), ‘Dispensing Fee Submitted’ (412-DC), ‘Flat Sales Tax Amount Submitted’ (481-HA), ‘Percentage Sales Tax Amount Submitted’ (482-GE), ‘Incentive Amount Submitted’ (438-E3), ‘Other Amount Claimed’ (480-H9). For service claim request, field represents a sum of ‘Professional Services Fee Submitted’ (477­BE), ‘Flat Sales Tax Amount Submitted’ (481-HA), ‘Percentage Sales Tax Amount Submitted’ (482-GE), ‘Other Amount Claimed’ (480-H9)..
         * 
         * @return Gross Amount Due
         */
        public String getGrossAmountDue() {
            return getValue("DU");
        }
        /** Basis Of Cost Determination at 423-DN. Code indicating the method by which 'Ingredient Cost Submitted' (Field 409-D9) was calculated..
         * 
         * @return Basis Of Cost Determination
         */
        public String getBasisOfCostDetermination() {
            return getValue("DN");
        }
		
	}

    public static class Coupon extends Segment{
    	public Coupon() {}
		public Coupon(String coupon) {
			super(coupon);
		}

        /** Coupon Type at 485-KE. Code indicating the type of coupon being used..
         * 
         * @return Coupon Type
         */
        public String getCouponType() {
            return getValue("KE");
        }
        /** Coupon Number at 486-ME. Unique serial number assigned to the prescription coupons..
         * 
         * @return Coupon Number
         */
        public String getCouponNumber() {
            return getValue("ME");
        }
        /** Coupon Value Amount at 487-NE. Value of the coupon..
         * 
         * @return Coupon Value Amount
         */
        public String getCouponValueAmount() {
            return getValue("NE");
        }
		
	}
    public static class Compound extends Segment{
    	Vector<Ingredient> ingredients = new Vector<Ingredient>();
    	public Compound() {}
		public Compound(String compound) {
			addData(compound);
		}

        /* (non-Javadoc)
		 * @see ncpdp.Fields#add(ncpdp.Field)
		 * EC RE
		 */
		@Override
		public boolean add(Field f) {
			boolean changed;
			if("EC".equals(f.getCode())) {
				int capacity = 10;
				try {
					capacity = Integer.parseInt(f.getValue());
				} catch(Exception ignore) {}
				ingredients.ensureCapacity(capacity);
				changed = super.add(f);
			}else if("RE".equals(f.getCode())) {
				Ingredient ingredient = new Ingredient();
				ingredients.add(ingredient);
				changed = ingredient.add(f);
			}else if(ingredients.isEmpty() == false) {
				Ingredient last = ingredients.lastElement();
				changed = last.add(f);
			}else {
				changed = super.add(f);
			}
			return changed;
		}

		/**
		 * @return the ingredients
		 */
		public Vector<Ingredient> getIngredients() {
			return ingredients;
		}

		/** Compound Dosage Form Description Code at 450-EF. Dosage form of the complete compound mixture..
         * 
         * @return Compound Dosage Form Description Code
         */
        public String getCompoundDosageFormDescriptionCode() {
            return getValue("EF");
        }
        /** Compound Dispensing Unit Form Indicator at 451-EG. NCPDP standard product billing codes..
         * 
         * @return Compound Dispensing Unit Form Indicator
         */
        public String getCompoundDispensingUnitFormIndicator() {
            return getValue("EG");
        }
        /** Compound Route of Administration at 452-EH. Code for the route of administration of the complete compound mixture..
         * 
         * @return Compound Route of Administration
         */
        public String getCompoundRouteofAdministration() {
            return getValue("EH");
        }


        public static class Ingredient extends Fields{

			/** (QUALIFIER) Compound Product ID Qualifier at 488-RE. Code qualifying the type of product dispensed.
			 * 
			 * @return Compound Product ID Qualifier
			 */
			public String getCompoundProductIDQualifier() {
			    return getValue("RE");
			}

			/** Compound Product ID at 489-TE. Product identification of an ingredient used in a compound..
			 * 
			 * @return Compound Product ID
			 */
			public String getCompoundProductID() {
			    return getValue("TE");
			}

			/** Compound Ingredient Quantity at 448-ED. Amount expressed in metric decimal units of the product included in the compound mixture..
			 * 
			 * @return Compound Ingredient Quantity
			 */
			public String getCompoundIngredientQuantity() {
			    return getValue("ED");
			}

			/** Compound Ingredient Drug Cost at 449-EE. Ingredient cost for the metric decimal quantity of the product included in the compound mixture indicated in ‘Compound Ingredient Quantity’ (Field 448-ED)..
			 * 
			 * @return Compound Ingredient Drug Cost
			 */
			public String getCompoundIngredientDrugCost() {
			    return getValue("EE");
			}

			/** Compound Ingredient Basis of Cost Determination at 490-UE. Code indicating the method by which the drug cost of an ingredient used in a compound was calculated..
			 * 
			 * @return Compound Ingredient Basis of Cost Determination
			 */
			public String getCompoundIngredientBasisofCostDetermination() {
			    return getValue("UE");
			}
			
		}
		
	}
    public static class DiagnosisCode extends Fields{

		/** (QUALIFIER) Diagnosis Code Qualifier at 492-WE. Code qualifying the ‘Diagnosis Code’ (424-DO).
		 * 
		 * @return Diagnosis Code Qualifier
		 */
		public String getDiagnosisCodeQualifier() {
		    return getValue("WE");
		}

		/** Diagnosis Code at 424-DO. Code identifying the diagnosis of the patient..
		 * 
		 * @return Diagnosis Code
		 */
		public String getDiagnosisCode() {
		    return getValue("DO");
		}
    	
    }
    public static class Measurement extends Fields{
	
		/** Measurement Date at 494-ZE. Date clinical information was collected or measured..
		 * 
		 * @return Measurement Date
		 */
		public String getDate() {
		    return getValue("ZE");
		}
	
		/** Measurement Time at 495-H1. Time clinical information was collected or measured..
		 * 
		 * @return Measurement Time
		 */
		public String getTime() {
		    return getValue("H1");
		}
	
		/** Measurement Dimension at 496-H2. Code indicating the clinical domain of the observed value in ‘Measurement Value’ (499­H4)..
		 * 
		 * @return Measurement Dimension
		 */
		public String getDimension() {
		    return getValue("H2");
		}
	
		/** Measurement Unit at 497-H3. Code indicating the metric or English units used with the clinical information..
		 * 
		 * @return Measurement Unit
		 */
		public String getUnit() {
		    return getValue("H3");
		}
	
		/** Measurement Value at 499-H4. Actual value of clinical information..
		 * 
		 * @return Measurement Value
		 */
		public String getValue() {
		    return getValue("H4");
		}
		
	}


	public static class Clinical extends Segment{
    	Vector<DiagnosisCode> diagnosisCodes = new Vector<DiagnosisCode>();
    	Vector<Measurement> measurements = new Vector<Measurement>();
    	public Clinical() {}
		public Clinical(String clinical) {
			addData(clinical);
		}
		/* (non-Javadoc)                                      
		 * @see ncpdp.Fields#add(ncpdp.Field)                 
		 * VE WE DO   Diagnosis Codes
		 * XE ZE H1 H2 H3 H4                                           
		 */                                                   
		@Override                                             
		public boolean add(Field f) {                   
			boolean changed = true;                       
			if ("VE".equals(f.getCode())) {               
				int capacity = 10;                          
				try {                                       
					capacity = Integer.parseInt(f.getValue());
				} catch(Exception ignore) {                 
					ignore.printStackTrace();                 
				}                                           
				diagnosisCodes.ensureCapacity(capacity);           
				changed = super.add(f);                     
			}else if("WE".equals(f.getCode())) {          
				DiagnosisCode diagnosisCode = new DiagnosisCode();               
				diagnosisCodes.add(diagnosisCode);                        
				changed = diagnosisCode.add(f);                    
			}else if("DO".equals(f.getCode())) {        
				DiagnosisCode last = diagnosisCodes.lastElement();        
				changed = last.add(f);                      
			}else if("XE".equals(f.getCode())) {          
				Measurement measurement = new Measurement();               
				measurements.add(measurement);                        
				changed = measurement.add(f);                    
			}else if(",ZE,H1,H2,H3,H4,".contains("," + f.getCode() +",")) {        
				Measurement last = measurements.lastElement();        
				changed = last.add(f);                      
			}else{                                       
				changed = super.add(f);                     
			}                                             
			return changed;                               
		}                                               
                                                     

        /**
		 * @return the diagnosisCodes
		 */
		public Vector<DiagnosisCode> getDiagnosisCodes() {
			return diagnosisCodes;
		}
		/**
		 * @return the measurements
		 */
		public Vector<Measurement> getMeasurements() {
			return measurements;
		}
		
	}
    
    public static Header createHeader(String header) {
    	return new Header(header);
    }

	public void read(NcpdpReader reader) {
		B1SegmentHandler handler = reader.getB1SegmentHandler();
		SegmentIterator segmentIterator = reader.getSegmentIterator();
		
		if (segmentIterator.hasNext()) {
			handler.startB1();
			while (segmentIterator.hasNext()) {
				Segment segment = segmentIterator.next();
				if (handler != null) {
					if("TH".equals(segment.getSegmentId())){
						handler.handleHeader((Header)segment);
					}else {
						switch (Integer.parseInt(segment.getSegmentId())) {
						case 1:
							handler.handlePatient((Patient) segment);
							break;
						case 2:
							handler.handlePharmacyProvider((PharmacyProvider) segment);
							break;
						case 3:
							handler.handlePrescriber((Prescriber) segment);
							break;
						case 4:
							handler.handleInsurance((Insurance) segment);
							break;
						case 5:
							handler.handleCOB_OtherPayments((COB_OtherPayments) segment);
							break;
						case 6:
							handler.handleWorkersCompensation((WorkersCompensation) segment);
							break;
						case 7:
							handler.handleClaim((Claim) segment);
							break;
						case 8:
							handler.handleDUR_PPS((DUR_PPS) segment);
							break;
						case 9:
							handler.handleCoupon((Coupon) segment);
							break;
						case 10:
							handler.handleCompound((Compound) segment);
							break;
						case 11:
							handler.handlePricing((Pricing) segment);
							break;
						case 13:
							handler.handleClinical((Clinical) segment);
							break;
						}
					}
				}
			}
			handler.endB1();
		}
		
	}
}
