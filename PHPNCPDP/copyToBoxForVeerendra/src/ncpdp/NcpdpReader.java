package ncpdp;



public class NcpdpReader {
    public NcpdpReader(SegmentIterator iterator, Object handler) {
    	segmentIterator = iterator;
    	if(handler instanceof B1SegmentHandler) {
    	    b1SegmentHandler = (B1SegmentHandler)handler;
    	}
	}

	private SegmentIterator segmentIterator;

    private B1SegmentHandler b1SegmentHandler;

	/**
	 * @return the segmentIterator
	 */
	public SegmentIterator getSegmentIterator() {
		return segmentIterator;
	}

	/**
	 * @return the b1SegmentHandler
	 */
	public B1SegmentHandler getB1SegmentHandler() {
		return b1SegmentHandler;
	}
    
}
