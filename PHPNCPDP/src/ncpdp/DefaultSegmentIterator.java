/**
 * 
 */
package ncpdp;

import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.Vector;





/**
 * @author tjkelly
 *
 */
public class DefaultSegmentIterator implements SegmentIterator {

	private Reader reader;
	private char segmentSeparator;
	private char fieldSeparator;
	private char groupSeparator;
	private ncpdp.SegmentFactory segmentFactory;
	private boolean eof = false;
	private Segment cacheSegment = null;

	/**
	 * 
	 */
	public DefaultSegmentIterator(SegmentFactory segmentFactory,Reader reader,  char segmentSeparator, char fieldSeparator, char groupSeparator) { 
        this.reader = reader;
        this.segmentSeparator = segmentSeparator;
        this.fieldSeparator = fieldSeparator;
        this.groupSeparator = groupSeparator;
        this.segmentFactory = segmentFactory;
        peek();
    }

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return (peek() != null);
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Segment next() {
		Segment segment = peek();
        if(segment ==null){
        	throw new NoSuchElementException();
        }
        cacheSegment = null;
        return  segment;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("DefaultSegmentIterator does not support remove()");

	}

	/* (non-Javadoc)
	 * @see ncpdp.SegmentIterator#peek()
	 */
	@Override
	public Segment peek() {
        if(eof) return null;
        if (cacheSegment == null) {
            // attempt to load putbackSegments
        	StringBuffer cbuf = new StringBuffer(150);
        	try {
        	    int readByte = reader.read();
        	    while(readByte != -1 && readByte != segmentSeparator) {
        	    	cbuf.append((char) readByte);
        	    	readByte = reader.read();
        	    }
        	    if(readByte == -1) {
        	        reader.close();	
        	        eof = true;
        	    }else if (readByte == segmentSeparator) {
        	    	String segData = cbuf.toString();
        	    	if(segData.length() > 0) {
        	    		cacheSegment = segmentFactory.build(segData, fieldSeparator, groupSeparator);
        	    	}
        	    }
        	}catch(Exception e) {
        		try {
        			eof = true;
        			reader.close();
        		}catch(Exception closeError) {
        			closeError.printStackTrace();
        		}
        	}
        }
        return cacheSegment;
	}



	/* (non-Javadoc)
	 * @see ncpdp.SegmentIterator#getSegmentFactory()
	 */
	@Override
	public SegmentFactory getSegmentFactory()
			throws UnsupportedOperationException {
		return segmentFactory;
	}

	/* (non-Javadoc)
	 * @see ncpdp.SegmentIterator#getSegmentSeparator()
	 */
	@Override
	public char getSegmentSeparator() {
		return segmentSeparator;
	}

	/* (non-Javadoc)
	 * @see ncpdp.SegmentIterator#getFieldSeparator()
	 */
	@Override
	public char getFieldSeparator() {
		return fieldSeparator;
	}

	/* (non-Javadoc)
	 * @see ncpdp.SegmentIterator#getGroupSeparator()
	 */
	@Override
	public char getGroupSeparator() {
		return groupSeparator;
	}

}
