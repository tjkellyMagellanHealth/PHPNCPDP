/**
 * 
 */
package hipaa.Histogram837;

import hipaa.SimpleReport837;

import java.io.File;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import acumen.Acumen;
import acumen.FileSubscriber;
import acumen.InterruptedException;
import acumen.QueueEvents;
import acumen.StandardCommandLine;

import magellan.util.ListMap2;
import x12.DefaultSegmentIterator;
import x12.Segment;
import x12.SegmentFactory;
import x12.X12Reader;

/**
 * @author tjkelly
 *
 */
public class HistogramX12 extends StandardCommandLine implements QueueEvents  {
    
	
	SegmentFactory segmentFactory = new SegmentFactory("hipaa4010");
	
	ListMap2<String,LoopStatistics> statsForLoops; 
	{// statsForLoops anonymous class 
		statsForLoops = new ListMap2<String, LoopStatistics>() {
			@Override
			public LoopStatistics get(Object loopKey) {
				if (this.containsKey(loopKey)) {
					return super.get(loopKey);
				}
				LoopStatistics loopStats = new LoopStatistics();
				this.put(loopKey.toString(), loopStats);
				return loopStats;
			}

		};
	}
    /**
	 * @param args
     * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new HistogramX12().launch(args);

	}
    

	
	/**
	 * @return the statsForLoops
	 */
	public ListMap2<String, LoopStatistics> getStatsForLoops() {
		return statsForLoops;
	}


	public LoopStatistics addLoop (String loopName, String segments, char segmentDelimiter, char fieldDelimiter, char subFieldDelimiter) {
		/* 1. 
		 * 
		 */
		LoopStatistics loop = statsForLoops.get(loopName);
		
		for(String segData : segments.split(Pattern.quote(Character.toString(segmentDelimiter)))) {
			String fields[] = segData.split(Pattern.quote(Character.toString(fieldDelimiter)));
			Segment segment = segmentFactory.build(segData, Character.toString(fieldDelimiter), Character.toString(subFieldDelimiter));
			
			String segkey = fields[0] + '\t' + segment.getQualifier();
			SegmentStatistics statsForSegment = loop.get(segkey);
			int index = 0;
			for(String field : fields) {
				statsForSegment.addFieldStats(index,field);
				index++;
			}
		}	
		return loop;
	}
	
	
	public void report(PrintStream stream) {
		for(Entry<String, LoopStatistics> loopEntry : statsForLoops.entrySet()) {
	        String loopKey = loopEntry.getKey();
			for(Entry<String, SegmentStatistics>  segEntry : loopEntry.getValue().entrySet()) {
				String segKey = segEntry.getKey();
				int fieldIndex = 0;
				for (String fieldEntry : segEntry.getValue().getBiggests() ) {
					Integer fieldOccurs = segEntry.getValue().getOccurenceCounts().get(fieldIndex);
					fieldOccurs = fieldOccurs != null ? fieldOccurs : 0;
					String maxLengthAndOccurences = fieldOccurs == 0 ? "\t" : fieldEntry.length()+ "\t" + fieldOccurs; 
					String msg = MessageFormat.format("{0}\t{1}\t{2,number,00}\t{4}\t{3}", loopKey, segKey, fieldIndex, fieldEntry, maxLengthAndOccurences);
					stream.println(msg);	
					fieldIndex++;
				}
			}
			
		}
		
		
	}


	@Override
	public void setProperties(String keyPrefix, Properties p) throws Exception {
		// TODO Auto-generated method stub
		
	}

/*	X12Reader reader = new X12Reader(new DefaultSegmentIterator(isa));
	Handler837 handler = new Handler837();
	reader.setHandler(handler.getHandler837P());
	reader.readIsa();
	handler.getHistogram().report(System.out);
	*/
	Handler837 handler = new Handler837(this);
	
	@Override
	public boolean read(File f) throws InterruptedException, Exception {
		X12Reader reader = new X12Reader(new DefaultSegmentIterator(f));
		reader.setHandler(handler.getHandler837P());
		
		try {
			reader.readIsa();
		} catch (Exception e) {
			
		}
		
		
		return true;
	}


	@Override
	public void startOfQueueEvent() throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void endOfQueueEvent() throws Exception {
		handler.getHistogram().report(System.err);
		
	}

	static int nextId =1;
	static public class LoopStatistics extends ListMap2<String, SegmentStatistics> {
		    int id = nextId++;
		    
			LoopStatistics() {}
			
			@Override
			public SegmentStatistics get(Object segKey) {
				if(this.containsKey(segKey)) {
					return super.get(segKey);
				}
				SegmentStatistics segStats = new SegmentStatistics();
				this.put(segKey.toString(), segStats);
				return segStats;
			}
	
		}


	static public class SegmentStatistics {
		int id = nextId++;
	    Vector<String> biggests = new Vector<String>();
	    Vector<Integer> occurenceCounts = new Vector<Integer>(5,2);
		public void addFieldStats(int index, String field) {
			if(field == null) {
				throw new NullPointerException();
			}
			if(biggests.size() < index + 1) {
				biggests.add(index, field);
			}else {
				String big = biggests.get(index);
				if(big == null || big.length() < field.length() || (big.length() == field.length() &&big.compareTo(field) < 0)){
					biggests.set(index, field);
				}
			}
			if(field.length() > 0) {
				if(occurenceCounts.size() < index +1) {
					occurenceCounts.setSize(index+1);
					occurenceCounts.add(index,1);
				}else {
					Integer previousOccurence = occurenceCounts.get(index);
					if(previousOccurence == null) {
						previousOccurence = 0;
					}
					occurenceCounts.set(index, previousOccurence +1);
				}
			}
		}
	
		public String getBiggest(int index) {
			return biggests.elementAt(index);
		}
		public Vector<String> getBiggests() {
			return biggests;
		}
		public Vector<Integer> getOccurenceCounts(){
			return occurenceCounts;
		}
	
	}



}
