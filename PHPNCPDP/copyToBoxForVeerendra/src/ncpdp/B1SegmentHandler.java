package ncpdp;

import ncpdp.B1.*;

public interface B1SegmentHandler {
	public void handleHeader( Header header);
	public void handlePatient(Patient patient);
	public void handleInsurance(Insurance insurance);
	public void handleClaim(Claim claim);
	public void handlePharmacyProvider(PharmacyProvider pharmacyProvider);
	public void handlePrescriber(Prescriber prescriber);
	public void handleCOB_OtherPayments(COB_OtherPayments cobOtherPayments);
	public void handleWorkersCompensation(WorkersCompensation workersCompensation);
	public void handleDUR_PPS(DUR_PPS dur_pps);
	public void handlePricing(Pricing pricing);
	public void handleCoupon(Coupon coupon);
	public void handleCompound(Compound compound);
	public void handleClinical(Clinical clinical);
	public void startB1();
	public void endB1();
}
