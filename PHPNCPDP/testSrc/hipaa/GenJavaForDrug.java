package hipaa;

public class GenJavaForDrug {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//drug();
		adjudication();

	}

	/**
	 * 
	 */
	public static void adjudication() {
		String templatea = "\r\n" + 
				"		,\"Adjudication%1$02dPayerId\"" + "\r\n" + 
				"		,\"Adjudication%1$02dPaidAmount\""+ "\r\n" + 
				"		,\"Adjudication%1$02dProcedureCode\"";
		
		String templateb = "\r\n" + 
				"		,\"Adjudication%1$02dProcedureModifier%2$02d\"";
		
		

		String templatec = "\r\n" + 
		"		,\"Adjudication%1$02dAmountNonCovered\"" + "\r\n" + 
		"		,\"Adjudication%1$02dDeductible\""+ "\r\n" + 
		"		,\"Adjudication%1$02dCoInsurance\""+ "\r\n" + 
		"		,\"Adjudication%1$02dCoPay\"";
		for(int i = 1; i <= 25;i++) {
			String instance = String.format(templatea,i);
			System.out.print(instance);
			for(int j = 1; j <= 4 ;j++) {
				String instance2 = String.format(templateb,i,j);
				System.out.print(instance2);
			}
			instance = String.format(templatec,i);
			System.out.print(instance);
		}
		
	}

	/**
	 * 
	 */
	public static void drug() {
		String template = 
		"		,\"Drug%1$02dCode\"                   // 2410 Drug Identification #%1$02d  \r\n" + 
		"		,\"Drug%1$02dUnitPrice\"\r\n" + 
		"		,\"Drug%1$02dUnitCount\"\r\n" + 
		"		,\"Drug%1$02dUnitOfMeasure\"";
		for(int i = 1; i < 26;i++) {
			String instance = String.format(template,i);
			System.out.println(instance);
		}
	}

}
