package ncpdp;

import java.util.Iterator;

import ncpdp.Batch.Record;

public interface RecordIterator extends Iterator<Record> {
	/**
	 * returns the next Record without moving forward. 
	 */
    public Record peek();
    /**
     * returns a RecordFactory that can create a {@link Record} from a {@link String} (optional operation)
     * @return SegmentFactory
     * @throws UnsupportedOperationException
     */
    public RecordFactory getRecordFactory() throws UnsupportedOperationException;
    
    public char getSTX();
    
    public char getETX();

}
