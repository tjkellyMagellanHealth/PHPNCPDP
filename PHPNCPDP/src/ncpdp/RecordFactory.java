package ncpdp;

import ncpdp.Batch.Detail;
import ncpdp.Batch.Header;
import ncpdp.Batch.Record;
import ncpdp.Batch.Trailer;

public class RecordFactory {

	public static Header createHeader(String header) {

		return new Header(header);
	}

	public static Detail createDetail(String detail) {

		return new Detail(detail);
	}

	public static Trailer createTrailer(String trailer) {
		return new Trailer(trailer);
	}

	public Record build(String recordData) {
		String segmentId = recordData.substring(1, 3);
		Record record = 
			 ("00".equals(segmentId)) ? new Header(recordData)
			:("G1".equals(segmentId)) ? new Detail(recordData)
			:("99".equals(segmentId)) ? new Trailer(recordData)
			: null;
		return record;
	}
}
