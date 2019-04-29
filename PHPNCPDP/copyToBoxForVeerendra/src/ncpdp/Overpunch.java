package ncpdp;

import java.util.regex.Pattern;

/*
; SYNOPSIS: Take a number that may or may not contain a symbol in it's last digit
;           to indicate the sign of the number.
;
;           This is used by some system to indicate a numbers sign without prepending
;           a + or - sign.
;           
;           Last Char    Corresponding Digit   Sign of Number
;               {               0               Positive
;               A               1               Positive
;               B               2               Positive
;               C               3               Positive
;               D               4               Positive
;               E               5               Positive
;               F               6               Positive
;               G               7               Positive
;               H               8               Positive
;               I               9               Positive
;               }               0               Negative
;               J               1               Negative
;               K               2               Negative
;               L               3               Negative
;               M               4               Negative
;               N               5               Negative
;               O               6               Negative
;               P               7               Negative
;               Q               8               Negative
;               R               9               Negative
;               
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
*/
public class Overpunch {
	boolean decimal   = false;
	int     precision = 2;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Overpunch op = new Overpunch();
		System.out.println("Value is [" + op.toString("12345}  ") + "]");
		System.out.println("Value is [" + op.toString("12345{") + "]");
		System.out.println("Value is [" + op.toString("123456") + "]");
		System.out.println("Value is [" + op.toString("12345z") + "]");

		op.setDecimal();

		System.out.println("Value is [" + op.toString("12345}") + "]");
		System.out.println("Value is [" + op.toString("12345{") + "]");
		System.out.println("Value is [" + op.toString("123456") + "]");
		System.out.println("Value is [" + op.toString("12345z") + "]");
		System.out.println("Value is [" + op.toString("11") + "]");

		op.setPrecision(3);

		System.out.println("Value is [" + op.toString("12345}") + "]");
		System.out.println("Value is [" + op.toString("12345{") + "]");
		System.out.println("Value is [" + op.toString("123456") + "]");
		System.out.println("Value is [" + op.toString("12345z") + "]");

		op.setInteger();

		System.out.println("Value is [" + op.toString("12345}") + "]");
		System.out.println("Value is [" + op.toString("12345{") + "]");
		System.out.println("Value is [" + op.toString("123456") + "]");
		System.out.println("Value is [" + op.toString("12345z") + "]");
}

	public Overpunch(){
	}
	
	public void setDecimal(){decimal = true;}
	public void setDecimal(int precision){decimal = true; this.precision = precision;}
	public void setInteger(){decimal = false;}
	public void setPrecision(int precision){ this.precision = precision;}
	
	private String toDecimal(String op){
		String overPunch = op.trim();

		// String too short? Prepend 0's
		if(overPunch.length() <= precision){
			StringBuffer result = new StringBuffer();
			result.append(".");

			if(overPunch.length() == precision){
				result.append(overPunch);
			}
			else{
				int zerosRequired = precision - overPunch.length();

				for(int i=0; i < zerosRequired; i++){
	    			result.append("0");
				}
				result.append(overPunch);
			}
			return result.toString();
		}

		// String NOT TOO SHORT
		String last  = overPunch.substring(overPunch.length() - precision);
    	String first = overPunch.substring(0,overPunch.length() - precision);
    	return first + '.' + last;
	}
	public double toDouble(String overPunch){
		String op = toString(overPunch);
	    return Double.parseDouble(op);
	}
	public float toFloat(String overPunch){
		String op = toString(overPunch);
		return Float.parseFloat(op);
	}
	public int toInt(String overPunch){
		String op = toString(overPunch);
		return Integer.parseInt(op);
	}
	public String toString(String op){
		String overPunch = op.trim();

		String num = new String();

		// Get last character
		char cLast   = overPunch.charAt(overPunch.length() - 1);

		//If this is a number, just return the entire string
		if(Character.isDigit(cLast)){
			if(decimal) return toDecimal(overPunch);
			return overPunch;
		}
		
		String sFirst = overPunch.substring(0,(overPunch.length() - 1)); 
		String sLast = overPunch.substring(overPunch.length() - 1); 
        String lastDigit = "";
        String sign = "";
        
        // Determine the last digit
        if(sLast.matches("[{}]")) lastDigit = "0";
        if(sLast.matches("[AJ]")) lastDigit = "1";
        if(sLast.matches("[BK]")) lastDigit = "2";
        if(sLast.matches("[CL]")) lastDigit = "3";
        if(sLast.matches("[DM]")) lastDigit = "4";
        if(sLast.matches("[EN]")) lastDigit = "5";
        if(sLast.matches("[FO]")) lastDigit = "6";
        if(sLast.matches("[GP]")) lastDigit = "7";
        if(sLast.matches("[HQ]")) lastDigit = "8";
        if(sLast.matches("[IR]")) lastDigit = "9";
        
        if(sLast.matches("[}JKLMNOPQR]")) sign = "-";

        if(lastDigit.equals("")){
        	//System.err.println("overpunchToString ERROR VALUE [" + overPunch + "] IS NOT A VALID OVERPUNCHED NUMBER!");
        	return overPunch;
        }

        num = sign  + sFirst + lastDigit;
        //System.out.println(num);
        if(decimal) return toDecimal (num);
        return num;
	}

}
