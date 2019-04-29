/**
 * 
 */
package ncpdp.HistogramNcpdp;



import static ncpdp.B1.FieldSeparator;
import static ncpdp.B1.GroupSeparator;
import static ncpdp.B1.SegmentSeparator;
import hipaa.Histogram837.HistogramX12;

import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.Map.Entry;

import ncpdp.B1SegmentHandler;
import ncpdp.Batch;
import ncpdp.Batch.Detail;
import ncpdp.Batch.Header;
import ncpdp.Batch.Trailer;
import ncpdp.DefaultRecordHandler;
import ncpdp.DefaultRecordIterator;
import ncpdp.DefaultSegmentIterator;
import ncpdp.Field;
import ncpdp.NcpdpReader;
import ncpdp.RecordFactory;
import ncpdp.RecordHandler;
import ncpdp.RecordIterator;
import ncpdp.RecordReader;
import ncpdp.Segment;
import ncpdp.SegmentFactory;
import ncpdp.SegmentIterator;
import ncpdp.SimpleReportB1;
import ncpdp.Batch.Record;
import ncpdp.HistogramNcpdp.HistogramNcpdp.SegmentStatistics;

import magellan.util.ListMap2;

import acumen.InterruptedException;
import acumen.StandardCommandLine;
import acumen.QueueEvents;

/**
 * @author tjkelly
 *
 */
public class HistogramNcpdp extends StandardCommandLine implements QueueEvents, RecordHandler {

	SegmentFactory simpleSegmentFactory = new SegmentFactory() {
		@Override
		public Segment build(String seg, char fieldSeparator,
				char groupSeparator) {
			if(seg.indexOf(fieldSeparator) == -1 && seg.length() >= 56 ) {
				return new ncpdp.B1.Header(seg);
			}else {
				return new Segment(seg,fieldSeparator);
			}
	
		}
	};
	
	/* (non-Javadoc)
	 * @see acumen.QueueEvents#startOfQueueEvent()
	 */
	@Override
	public void startOfQueueEvent() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see acumen.QueueEvents#endOfQueueEvent()
	 */
	@Override
	public void endOfQueueEvent() throws Exception {
		report(System.err);

	}

	private void report(PrintStream stream) {
		for(Entry<String,SegmentStatistics> segEntry : this.statsForSegments.entrySet()) {
			String segmentKey = segEntry.getKey();
			SegmentStatistics segmentStats = segEntry.getValue();
			int i = 0;
			for(Entry<String, FieldStatisitics> fieldEntry : segmentStats.entrySet()) {
				i++;
				String fieldKey = fieldEntry.getKey();
				FieldStatisitics fieldStats = fieldEntry.getValue();
				String msg = MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t",i, segmentKey, fieldKey, fieldStats.occurenceCounts, fieldStats.biggest.length(),fieldStats.biggest);
				stream.println(msg);
			}
		}
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new HistogramNcpdp().launch(args);

	}

	@Override
	public void setProperties(String keyPrefix, Properties p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean read(File file) throws InterruptedException, Exception {
		
		Batch batch = new Batch();
		RecordIterator recIter;
		int i;
		FileReader fileReader = new FileReader(file);
		try {
			recIter = new DefaultRecordIterator(new RecordFactory()
			, new FileReader(file)
			,'\u0002'
			,'\u0003');
			// These batch records hold the 'envelope' info.
			for(i = 0; recIter.hasNext(); i++) {
				Record next = recIter.next();
				System.out.println(next.getSegmentIdentifier());
				//SegmentStatistics segStats = statsForSegments.get(next.getSegmentIdentifier());

				
			}
		} finally {
			fileReader.close();
		}
		System.out.println(i + " records");
		
		fileReader = new FileReader(file);
		try {
			recIter = new DefaultRecordIterator(new RecordFactory()
			, fileReader
	        ,'\u0002'
	        ,'\u0003');
			RecordReader recordReader = new RecordReader(recIter,this);
	        batch.read(recordReader);
		} finally {
			fileReader.close();
		}
		return true;
	}
	ListMap2<String, SegmentStatistics> statsForSegments;
	{
		statsForSegments = new ListMap2<String, SegmentStatistics> (){
			@Override
			public SegmentStatistics get(Object segmentKey) {
				if (this.containsKey(segmentKey)) {
					return super.get(segmentKey);
				}
				SegmentStatistics segmentStats = new SegmentStatistics();
				this.put(segmentKey.toString(), segmentStats);
				return segmentStats;
			}
		};
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public  void handleDetail(Detail detail) {
		

		String ncpdpDataRecord = detail.getNCPDPDataRecord();
//		String b1Header = ncpdpDataRecord.substring(0, 56);
//		
//		String b1Transaction = ncpdpDataRecord.substring(56);
		
		SegmentIterator iterator = new DefaultSegmentIterator(simpleSegmentFactory
		,new StringReader(ncpdpDataRecord)
		,SegmentSeparator
		,FieldSeparator
		,GroupSeparator);
		if(iterator.hasNext()) {
			iterator.next(); // toss header
		}
		
		while(iterator.hasNext()) {
			Segment seg = iterator.next();
			System.out.println("segment Id: " + seg.getSegmentId());
			SegmentStatistics segStats = statsForSegments.get(seg.getSegmentId());	
			for(Field field : seg) {
				segStats.addFieldStats(field.getCode(), field.getValue());
			}
		}
	}

	@Override
	public void handleTrailer(Trailer trailer) {
		// TODO Auto-generated method stub
		
	}

	public static class SegmentStatistics extends ListMap2<String, FieldStatisitics> {
			public void addFieldStats(String key, String field) {
				if(field == null) {
					throw new NullPointerException();
				}
				if(this.containsKey(key)) {
					FieldStatisitics fieldStats = this.get(key);
					fieldStats.occurenceCounts++;
					String big = fieldStats.biggest;
					if (big.length() < field.length() 
					|| (big.length() == field.length() &&big.compareTo(field) < 0)) {
						fieldStats.biggest = field;
					}
				} else {
					this.put (key, new FieldStatisitics(field));
				}
			}

		}

	public static class FieldStatisitics {
		public String biggest = "";
		public Integer occurenceCounts = 0;
		public FieldStatisitics() {
		}
		
		public FieldStatisitics(String fieldValue) {
			biggest = fieldValue;
			occurenceCounts = 1;
		}
	
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("FieldStatisitics [biggest=");
			builder.append(biggest);
			builder.append(", occurenceCounts=");
			builder.append(occurenceCounts);
			builder.append("]");
			return builder.toString();
		}
		
		
		
	}
}	