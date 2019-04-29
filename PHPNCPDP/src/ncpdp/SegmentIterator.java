package ncpdp;

import java.util.Iterator;

/*
 * 	public final static char SegmentSeparator = '\u001E';
	public final static char GroupSeparator = '\u001D';
	public final static char FieldSeparator = '\u001C'; 
 */


	public interface SegmentIterator extends Iterator<Segment> {
		/**
		 * returns the next segment without moving forward. 
		 */
	    public Segment peek();
	    /**
	     * returns a SegmentFactory that can create a {@link Segment} from a {@link String} (optional operation)
	     * @return SegmentFactory
	     * @throws UnsupportedOperationException
	     */
	    public SegmentFactory getSegmentFactory() throws UnsupportedOperationException;
	    
	    public char getSegmentSeparator();
	    
	    public char getFieldSeparator();
	    
	    public char getGroupSeparator();
	}

