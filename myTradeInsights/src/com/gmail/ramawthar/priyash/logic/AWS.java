package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;

import com.gmail.ramawthar.priyash.responses.ReturnClass; 


public class AWS {
//	static byte[] HmacSHA256(String data, byte[] key) throws Exception {
//	    String algorithm="HmacSHA256";
//	    Mac mac = Mac.getInstance(algorithm);
//	    mac.init(new SecretKeySpec(key, algorithm));
//	    return mac.doFinal(data.getBytes("UTF8"));
//	}
//
//	static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
//	    byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
//	    byte[] kDate = HmacSHA256(dateStamp, kSecret);
//	    byte[] kRegion = HmacSHA256(regionName, kDate);
//	    byte[] kService = HmacSHA256(serviceName, kRegion);
//	    byte[] kSigning = HmacSHA256("aws4_request", kService);
//	    return kSigning;
//	}
	private ReturnClass rc = new ReturnClass("Init");
	public ReturnClass pushInsightToDB(Insight insight){
		
		  try {

				rc.setStatus("Success");
				//URL url = new URL("http://localhost:8080/RESTfulExample/json/product/post");
				
				URL url = new URL("https://4s0q0hhyt2.execute-api.eu-west-1.amazonaws.com/production/insightInsert");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("x-api-key", "7gCbESbtUZ2NuVymjAFR373VpT47q9Ud8xNhkHPj");

				//String input = "{\"qty\":100,\"name\":\"iPad 4\"}";
				//String input = "{\"instrument\":\"ADI\",\"date\":\"12-Feb-18\",\"code\":\"I005\",\"desc\":\"Moving average SMA20 turning point - postman\",\"type\":\"I\",\"value\":\"100.0\",\"note\":\"Nothing indicated\",\"rec\":\"HOLD\",\"owned\":\"owned\"}";
				String input = insight.getParamString();
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();
				
/*
				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
				}
*/
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				/*System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) { 
					System.out.println(output);
				}
*/
				conn.disconnect();

			  } catch (MalformedURLException e) {
				rc.setStatus("ERROR");
				e.printStackTrace();

			  } catch (IOException e) {
				rc.setStatus("ERROR");
				e.printStackTrace();

			 }  catch (Exception er) {
					rc.setStatus("ERROR");
					er.printStackTrace();

				 }
		   
		return rc;
	}

	public static void main(String [] args){
		
		AWS aws = new AWS();
		//aws.pushInsightToDB();
	}
}
