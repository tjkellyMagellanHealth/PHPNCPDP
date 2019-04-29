package ncpdp;

import java.util.Map;
import java.util.TreeMap;

import ncpdp.B1.*;

public class SegmentFactory {
	/*
	 * 	public final static char SegmentSeparator = '\u001E';
	 * public final static char GroupSeparator = '\u001D';
	 * public final static char FieldSeparator = '\u001C'; 
	 */

	public SegmentFactory() {
	
	}
//	static Map<String, Class<? extends Segment> > segmentClasses = new TreeMap<String,Class<? extends Segment> > ();
//	static {
//
//		segmentClasses.put("07", Claim.class);
//		segmentClasses.put("13", Clinical.class);
//		segmentClasses.put("05", COB_OtherPayments.class);
//		segmentClasses.put("10", Compound.class);
//		segmentClasses.put("09", Coupon.class);
//		segmentClasses.put("08", DUR_PPS.class);
//		segmentClasses.put("04", Insurance.class);
//		segmentClasses.put("01", Patient.class);
//		segmentClasses.put("02", PharmacyProvider.class);
//		segmentClasses.put("03", Prescriber.class);
//		segmentClasses.put("11", Pricing.class);
//		segmentClasses.put("06", WorkersCompensation.class);
//	}
	
//	public static Segment oldbuild(String seg,char fieldSeparator, char groupSeparator) {
//		Segment seg2 = new Segment(seg, fieldSeparator); 
//		try {
//			Segment seg3 = segmentClasses.get(seg2.getSegmentId()).newInstance();
//			
//			seg3.addAll(seg2);
//			return seg3;
//		} catch (Exception err) {
//			throw new SegmentNotDefinedException("["+seg+"] segment could not be defined",err);
//		}
//	}
	public Segment build(String seg,char fieldSeparator, char groupSeparator) {
		if(seg.indexOf(fieldSeparator) == -1 && seg.length() >= 56 ) {
			return new Header(seg);
		}else {
			Segment seg2 = new Segment(seg,fieldSeparator);
			
			int segId = 0;
			try {
			    segId = Integer.parseInt(seg2.getSegmentId());
			} catch (Exception ignore ) {} 
			Segment seg3 = 
				(segId == 1) ? new Patient()
			   :(segId == 2) ? new PharmacyProvider()
			   :(segId == 3) ? new Prescriber()
			   :(segId == 4) ? new Insurance()
			   :(segId == 5) ? new COB_OtherPayments()
			   :(segId == 6) ? new WorkersCompensation()
			   :(segId == 7) ? new Claim()
			   :(segId == 8) ? new DUR_PPS()
			   :(segId == 9) ? new Coupon()
			   :(segId ==10) ? new Compound()
			   :(segId ==11) ? new Pricing()
			   :(segId ==13) ? new Clinical()
			   : null;
			 if(seg3 == null) {
				 seg3 = seg2;
			 }else {
			     seg3.addAll(seg2);
			 }
			 return seg3;
		}

		
	}
}
