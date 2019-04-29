package ncpdp;

public class Segment extends Fields {		

	public Segment()  {}
	public Segment(String data)  {
		addData(data);
	}

	public Segment(String data, char fieldSeparator)  {
		addData(data, fieldSeparator);
	}
	/** Segment Identification at 111-AM. Identifies the segment in the request and/or response.
	 * 
	 * @return Segment Identification
	 */
	public String getSegmentId() {
		return getValue("AM");
	}
	
}