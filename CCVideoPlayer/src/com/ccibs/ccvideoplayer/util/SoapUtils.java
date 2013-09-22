package com.ccibs.ccvideoplayer.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class SoapUtils {

	private String urlRoot;
	private static SoapUtils soapUtils;
	private static final String NAMESPACE = "http://log.wealth.vocy.com/";

	private SoapUtils() {
	}

	public static SoapUtils getInstance() {
		if (soapUtils == null) {
			soapUtils = new SoapUtils();
			soapUtils.urlRoot=HttpRequest.getInstance().webServiceURL;
		}
		return soapUtils;
	}
	public  void  log(final String type, final HashMap<String,Object> args) {
		new Thread(new Runnable() {			
			@Override
			public void run() {
		String methodName="Log";
		args.put("time", currentTime());
		String infor="[type="+type;
 			Iterator<Entry<String, Object>> iter = args.entrySet().iterator();
 			while (iter.hasNext()) {
 				Entry<String, Object> entry = iter.next();
 				String key = entry.getKey().toString();
 				String val = entry.getValue().toString();
 				infor+=","+key+"="+val;
 			}
		infor+="]";		
		
		try {
		
			SoapObject request = new SoapObject(NAMESPACE,methodName);
			request.addProperty("in0", type);
			request.addProperty("in1", infor);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = request;
			HttpTransportSE transport = new HttpTransportSE(urlRoot
					+ methodName);
			transport.call(NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			 System.out.println("result---------->"+type+"=========>"+result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
			}).start();
	}
	public  static String currentTime(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sf .format(new Date());
	}
	public void getServiceInfo(final String methodName, final String[] params) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				SoapObject result=null;
				try {
					SoapObject request = new SoapObject(NAMESPACE, methodName);
					for (int i = 0; i < params.length; i++) {
						request.addProperty("in" + i, params[i]);
					}
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.bodyOut = request;
					HttpTransportSE transport = new HttpTransportSE(urlRoot
							+ methodName);
					transport.call(NAMESPACE + methodName, envelope);
					 result = (SoapObject) envelope.bodyIn;				
	            System.out.println("result------------------------------->"+result);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	
	}
}
