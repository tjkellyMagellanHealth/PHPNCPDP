package ncpdp;

import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;

import ncpdp.Batch.Record;

public class DefaultRecordIterator implements RecordIterator {

	private RecordFactory recordFactory;
	private Reader reader;
	private char recordSTX;
	private char recordETX;
	Record cacheRecord=null;
	boolean eof=false;

	// RecordFactory recordFactory,Reader reader,  char segmentSeparator, char fieldSeparator, char groupSeparator) {
	public DefaultRecordIterator(RecordFactory recordFactory, Reader reader,  char recordSTX, char recordETX) {
		this.recordFactory = recordFactory;
		this.reader = reader;
		this.recordSTX = recordSTX;
		this.recordETX = recordETX;
		peek();
		
	}

	@Override
	public boolean hasNext() {
		return (peek() != null);
	}

	@Override
	public Record next() {
		Record record = peek();
        if(record ==null){
        	throw new NoSuchElementException();
        }
        cacheRecord = null;
        return  record;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("DefaultRecordIterator does not support remove()");

	}

	@Override
	public Record peek() {
		if(eof) return null;
		if(cacheRecord == null) {
			
			try {
				int readByte = reader.read();
				while (readByte != -1 && readByte != recordSTX) {
					readByte = reader.read();
				}
				StringBuffer cbuf = new StringBuffer(150);
				while (readByte != -1 && readByte != recordETX) {
					cbuf.append((char) readByte);
					readByte = reader.read();
				}
				cbuf.append(recordETX);
				if(readByte == -1) {
					reader.close();
					eof = true;
				}else {
					String recordData = cbuf.toString();
					if(recordData.length() > 2) {
						cacheRecord = recordFactory.build(recordData );
					}
				}
			} catch (Exception e) {
				eof = true;
				try {
					reader.close();
				} catch (Exception closeError) {
					// TODO Auto-generated catch block
					closeError.printStackTrace();
				}
			}
			
		}
		return cacheRecord;
	}

	@Override
	public RecordFactory getRecordFactory()
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getSTX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getETX() {
		// TODO Auto-generated method stub
		return 0;
	}

}
