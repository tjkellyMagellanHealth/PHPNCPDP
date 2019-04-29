package ncpdp;

import java.io.StringReader;

import ncpdp.Batch.Detail;
import ncpdp.Batch.Header;
import ncpdp.Batch.Trailer;
import static ncpdp.B1.*;

public class DefaultRecordHandler implements RecordHandler {
	B1SegmentHandler b1SegmentHandler;
	private Header header;
	private Trailer trailer;
	B1 b1 = new B1();
	public DefaultRecordHandler(B1SegmentHandler b1SegmentHandler) {
		this.b1SegmentHandler = b1SegmentHandler;
	}

	@Override
	public void startBatch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endBatch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHeader(Header header) {
		this.header = header;

	}

	@Override
	public void handleDetail(Detail detail) {
		String ncpdpDataRecord = detail.getNCPDPDataRecord();
//		String b1Header = ncpdpDataRecord.substring(0, 56);
//		
//		String b1Transaction = ncpdpDataRecord.substring(56);
		
		SegmentIterator iterator = new DefaultSegmentIterator(new SegmentFactory()
		,new StringReader(ncpdpDataRecord)
		,SegmentSeparator
		,FieldSeparator
		,GroupSeparator);
		NcpdpReader reader = new NcpdpReader(iterator, b1SegmentHandler);
		b1.read(reader);
		

	}

	@Override
	public void handleTrailer(Trailer trailer) {
		this.trailer = trailer;

	}

}
