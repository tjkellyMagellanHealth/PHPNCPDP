package ncpdp;

import ncpdp.Batch.*;

public interface RecordHandler {

	void startBatch();

	void endBatch();

	void handleHeader(Header header);

	void handleDetail(Detail detail);

	void handleTrailer(Trailer trailer);

}
