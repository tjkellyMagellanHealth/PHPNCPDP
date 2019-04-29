package ncpdp;

public class RecordReader {

	private RecordIterator recordIterator;
	private RecordHandler recordHandler;

	public RecordReader(RecordIterator recIter, RecordHandler recordHandler) {
		this.recordIterator = recIter;
		this.recordHandler = recordHandler;
		
	}

	public RecordHandler getRecordHandler() {
		return recordHandler;
	}

	public RecordIterator getRecordIterator() {
		return recordIterator;
	}

}
