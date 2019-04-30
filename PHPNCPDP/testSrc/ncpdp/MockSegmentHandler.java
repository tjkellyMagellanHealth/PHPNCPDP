package ncpdp;

import ncpdp.B1.COB_OtherPayments;
import ncpdp.B1.Claim;
import ncpdp.B1.Clinical;
import ncpdp.B1.Compound;
import ncpdp.B1.Coupon;
import ncpdp.B1.DUR_PPS;
import ncpdp.B1.Header;
import ncpdp.B1.Insurance;
import ncpdp.B1.Patient;
import ncpdp.B1.PharmacyProvider;
import ncpdp.B1.Prescriber;
import ncpdp.B1.Pricing;
import ncpdp.B1.WorkersCompensation;

public class MockSegmentHandler implements B1SegmentHandler{

Header header;
Patient patient;
Insurance insurance;
Claim claim;
PharmacyProvider pharmacyProvider;
Prescriber prescriber;
COB_OtherPayments cobOtherPayments;
WorkersCompensation workersCompensation;
DUR_PPS dur_pps;
Pricing pricing;
Coupon coupon;
Compound compound;
Clinical clinical;

@Override
public void handleHeader(Header header) {
	this.header = header;
	
}

@Override
public void handlePatient(Patient patient) {
	this.patient = patient;
	
}

@Override
public void handleInsurance(Insurance insurance) {
	this.insurance = insurance;
}

@Override
public void handleClaim(Claim claim) {
	this.claim = claim;
	
}

@Override
public void handlePharmacyProvider(PharmacyProvider pharmacyProvider) {
	this.pharmacyProvider = pharmacyProvider;
	
}

@Override
public void handlePrescriber(Prescriber prescriber) {
	this.prescriber = prescriber;
	
}

@Override
public void handleCOB_OtherPayments(COB_OtherPayments cobOtherPayments) {
	this.cobOtherPayments = cobOtherPayments;
	
}

@Override
public void handleWorkersCompensation(
		WorkersCompensation workersCompensation) {
	this.workersCompensation = workersCompensation;
	
}

@Override
public void handleDUR_PPS(DUR_PPS dur_pps) {
	this.dur_pps = dur_pps;
	
}

@Override
public void handlePricing(Pricing pricing) {
	this.pricing = pricing;
	
}

@Override
public void handleCoupon(Coupon coupon) {
	this.coupon = coupon;
	
}

@Override
public void handleCompound(Compound compound) {
	this.compound = compound;
	
}

@Override
public void handleClinical(Clinical clinical) {
	this.clinical = clinical;
	
}

@Override
public void startB1() {
	System.out.println("start B1");
	
}

@Override
public void endB1() {
	System.out.println("end B1");
	
}}