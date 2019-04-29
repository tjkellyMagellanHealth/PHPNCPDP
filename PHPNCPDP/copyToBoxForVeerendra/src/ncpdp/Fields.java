package ncpdp;

import static ncpdp.B1.FieldSeparator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

public class Fields extends Vector<Field> {
    public Fields() {}
//    public Fields(String data) {
//		add(data);    	
//    }
	/**
	 * @param data
	 */
	public void addData(String data) {
		for(String fieldData : data.split(Character.toString(FieldSeparator))) {
			if(fieldData.length() >= 2) {
			    add(new Field(fieldData));
			}
		}
	}
	public void addData(String data,char fieldSeparator) {
		for(String fieldData : split(data, fieldSeparator)) {
			if(fieldData.length() >= 2) {
			    add(new Field(fieldData));
			}
		}
	}

	public static String[] split(String fields, char separator){
		int beginIndex=0;
		int endIndex=fields.indexOf(separator);
		Collection<String> results = new LinkedList<String>();
		while(endIndex != -1){
			results.add(fields.substring(beginIndex,endIndex));
			beginIndex = endIndex+1;
			endIndex=fields.indexOf(separator,beginIndex);
		}
		if(beginIndex < fields.length()){
			results.add(fields.substring(beginIndex));
		}
		if(results.size() == 0){
			results.add("");
		}
		return results.toArray(new String[results.size()]);
	}
	
	
    /* (non-Javadoc)
	 * @see java.util.LinkedList#add(java.lang.Object)
	 */
	@Override
	public boolean add(Field f) {
		
//		if(size() == 0) {
//			System.out.println(f + " started. "+ getClass().getSimpleName() );
//		}else {
//			System.out.println(get(0) + " adds " + f + " " + getClass().getSimpleName() );
//		}
		return super.add(f);
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.util.Vector#addElement(java.lang.Object)
	 */
	@Override
	public synchronized void addElement(Field field) {
		add(field);
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#addAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean addAll(Collection<? extends Field> c) {
		boolean success = false;
		for(Field f : c) {
			boolean success2 = add(f);
			success = success || success2;
		}
		return success;
	}

	String getValue(String key) {
    	Field f = getField(key);
    	return (f != null && f.getValue() != null)
    	? f.getValue()
    	: "";
    }
    Field getField(String key) {
    	if(key != null) {
	    	for(Field f : this) {
	    		if(key.equals(f.getCode())) {
	    			return f;
	    		}
	    	}
    	}
    	return null;
    }
    
    int getIndex(String key, int beginIndex) {
    	ListIterator<Field> iter =listIterator(beginIndex);
    	int index = beginIndex;
    	while(iter.hasNext()) {
    		Field field = iter.next();
			if(field.getCode().equals(key)) {
    			return index;
    		}
			index++;
    	}
    	return -1;
    }
    
}
