package hipaa.Histogram837;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import x12.Loop;
import hipaa4010.Handler837I;
//import hipaa4010.Handler837P;
import hipaa4010.X837P_1000a;
import hipaa4010.X837P_1000b;
import hipaa4010.X837P_2000a;
import hipaa4010.X837P_2000b;
import hipaa4010.X837P_2000c;
import hipaa4010.X837P_2010aa;
import hipaa4010.X837P_2010ab;
import hipaa4010.X837P_2010ba;
import hipaa4010.X837P_2010bb;
import hipaa4010.X837P_2010bc;
import hipaa4010.X837P_2010bd;
import hipaa4010.X837P_2010ca;
import hipaa4010.X837P_2300;
import hipaa4010.X837P_2305;
import hipaa4010.X837P_2310a;
import hipaa4010.X837P_2310b;
import hipaa4010.X837P_2310c;
import hipaa4010.X837P_2310d;
import hipaa4010.X837P_2310e;
import hipaa4010.X837P_2320;
import hipaa4010.X837P_2330a;
import hipaa4010.X837P_2330b;
import hipaa4010.X837P_2330c;
import hipaa4010.X837P_2330d;
import hipaa4010.X837P_2330e;
import hipaa4010.X837P_2330f;
import hipaa4010.X837P_2330g;
import hipaa4010.X837P_2330h;
import hipaa4010.X837P_2400;
import hipaa4010.X837P_2410;
import hipaa4010.X837P_2420a;
import hipaa4010.X837P_2420b;
import hipaa4010.X837P_2420c;
import hipaa4010.X837P_2420d;
import hipaa4010.X837P_2420e;
import hipaa4010.X837P_2420f;
import hipaa4010.X837P_2420g;
import hipaa4010.X837P_2430;
import hipaa4010.X837P_2440;
import hipaa4010.X837P_Gs;
import hipaa4010.X837P_Isa;
import hipaa4010.X837P_St;

public class Handler837 //obsolete implements Handler837P 
{
	HistogramX12 histogram ;
    final char segmentDelimiter = '\u00BB';
    final char fieldDelimiter = '\u0092';
    final char subFieldDelimiter = '\u0094';
	public Handler837(HistogramX12 histogramX12) {
		this.histogram = histogramX12;
	}

	private void observe(Loop loop) {
		String packageName = loop.getClass().getPackage().getName();
		String loopName = loop.getClass().getSimpleName();
		String key = packageName.substring(5) + "\t" + loopName.substring(1, 5) + "\t" + loopName.substring(6).toUpperCase();
		StringBuffer buf = new StringBuffer();
		loop.toStringBuffer(
			buf
		  , Character.toString(segmentDelimiter)
		  , Character.toString(fieldDelimiter)
		  , Character.toString(subFieldDelimiter)
		);
		histogram.addLoop(key, buf.toString(), segmentDelimiter, fieldDelimiter, subFieldDelimiter);
		
	}	
	InvocationHandler implementHandler = new InvocationHandler(){
        /**
         * implementHandler.invoke()  (Anonymous class)
         */
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			
			String methodClass = method.getDeclaringClass().getSimpleName();
			String methodName = method.getName();
			if(methodClass.startsWith("Handler837")
			&& args.length == 1
			&& args[0] instanceof Loop) {
				Loop loop = (Loop) args[0];
			    if(methodName.startsWith("start")) {
				    observe(loop);
			    }else if(methodName.startsWith("end")) {
				    loop.clear();
			    }
				
			}
			
			//System.out.printf("%s ( %s )\n",method.getDeclaringClass().getSimpleName()+ "." + method.getName(), args[0]);
			
			return null;
		}
	};
	
	
	
	public Object getHandler837P() {
		Object proxy = Proxy.newProxyInstance(hipaa4010.Handler837P.class.getClassLoader()
				, new Class[] {hipaa4010.Handler837P.class,hipaa5010.Handler837P.class, hipaa4010.Handler837I.class,hipaa5010.Handler837I.class }, implementHandler);
		return  proxy;
	}
	
	/**
	 * @return the histogram
	 */
	public HistogramX12 getHistogram() {
		return histogram;
	}



	
	
}
