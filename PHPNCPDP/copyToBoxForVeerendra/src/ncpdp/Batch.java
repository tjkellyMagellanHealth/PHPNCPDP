/**
 * 
 */
package ncpdp;

/**
 * @author tjkelly
 * 
 */
public class Batch {
	public static class Record {

		protected String data;

		public Record(String recordData) {
			assert recordData.charAt(0) == '\u0002';
			assert recordData.charAt(recordData.length() - 1) == '\u0003';
			data = recordData;
		}

		/**
		 * get Segment Identifier, see 701
		 * 
		 * @return Segment Identifier
		 */
		public String getSegmentIdentifier() {
			return data.substring(1, 3);
		}

		public String getLastField(int beginIndex) {
			return data.substring(beginIndex, data.length() - 1);
		}
	}

	public static class Header extends Record {

		public Header(String header) {
			super(header);
		}

		/**
		 * get Transmission Type, see 880-K6
		 * 
		 * @return Transmission Type
		 */
		public String getTransmissionType() {
			return data.substring(3, 4);
		}

		/**
		 * get Sender ID, see 880-K1
		 * 
		 * @return Sender ID
		 */
		public String getSenderID() {
			return data.substring(4, 28);
		}

		/**
		 * get Batch Number, see 806-5C
		 * 
		 * @return Batch Number
		 */
		public String getBatchNumber() {
			return data.substring(28, 35);
		}

		/**
		 * get Creation Date, see 880-K2
		 * 
		 * @return Creation Date
		 */
		public String getCreationDate() {
			return data.substring(35, 43);
		}

		/**
		 * get Creation Time, see 880-K3
		 * 
		 * @return Creation Time
		 */
		public String getCreationTime() {
			return data.substring(43, 47);
		}

		/**
		 * get File Type, see 702
		 * 
		 * @return File Type
		 */
		public String getFileType() {
			return data.substring(47, 48);
		}

		/**
		 * get Version/Release Number, see 102-A2
		 * 
		 * @return Version/Release Number
		 */
		public String getVersionReleaseNumber() {
			return data.substring(48, 50);
		}

		/**
		 * get Reciever ID, see 880-K7
		 * 
		 * @return Reciever ID
		 */
		public String getRecieverID() {
			return getLastField(50);
		}

	}

	public static class Detail extends Record {

		public Detail(String detail) {
			super(detail);
		}

		/**
		 * get Transaction Reference Number, see 880-K5
		 * 
		 * @return Transaction Reference Number
		 */
		public String getTransactionReferenceNumber() {
			return data.substring(3, 13);
		}

		/**
		 * get NCPDP Data Record, see NCPDPDR
		 * 
		 * @return NCPDP Data Record
		 */
		public String getNCPDPDataRecord() {
			return getLastField(13);
		}

	}

	public static class Trailer extends Record {

		public Trailer(String recordData) {
			super(recordData);
		}

		/**
		 * get Batch Number, see 806-5C
		 * 
		 * @return Batch Number
		 */
		public String getBatchNumber() {
			return data.substring(3, 10);
		}

		/**
		 * get Record Count, see 751
		 * 
		 * @return Record Count
		 */
		public String getRecordCount() {
			return data.substring(10, 20);
		}

		/**
		 * get Message, see 504-F4
		 * 
		 * @return Message
		 */
		public String getMessage() {
			return getLastField(20);
		}

	}

	public static Header createHeader(String header) {

		return new Header(header);
	}

	public static Detail createDetail(String detail) {

		return new Detail(detail);
	}

	public static Trailer createTrailer(String trailer) {
		return new Trailer(trailer);
	}

	public void read(RecordReader reader) {
		RecordHandler handler = reader.getRecordHandler();
		RecordIterator recordIterator   = reader.getRecordIterator();
		if(recordIterator.hasNext()) {
			handler.startBatch();
			while(recordIterator.hasNext()) {
				Record record = recordIterator.next();
				if("00".equals(record.getSegmentIdentifier())) {
					handler.handleHeader((Header)record);
				} else if("G1".equals(record.getSegmentIdentifier())) {
					handler.handleDetail((Detail)record);
				} else if("99".equals(record.getSegmentIdentifier())) {
					handler.handleTrailer((Trailer)record);
				}
			}
			handler.endBatch();
		}
		
	}

}
