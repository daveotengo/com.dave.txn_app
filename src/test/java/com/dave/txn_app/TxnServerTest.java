package com.dave.txn_app;





import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class TxnServerTest {
	
	private String BASE_STRING = "http://localhost:2021/TransWS/api";

	private int TEST_Transaction_ID = 1;
	private String TEST_TransactionOlderthan30Secs = "{\"amount\":\"91.91\", \"timeStamp\":\"2014-07-17T09:59:20.312Z\", \"id\": " + TEST_Transaction_ID +"}";
	private String TEST_TransactionWithin30Secs = "{\"amount\":\"91.91\", \"timeStamp\":\"2014-07-17T09:59:51.312Z\", \"id\": " + TEST_Transaction_ID +"}";

	private String WRONG_MSG_BODY = "Wrong message body. Check if the JSON content of the message fulfill all the requirements.";

	private String SUCCESS_ON_CREATION = "The resource was successfully added to the system.";
	private String FAILURE_ON_CREATION = null;

	private String SUCCESS_ON_DELETE = null;

	
	private String SUCCESS_ON_NO_TRANSACTIONS="No Transactions Yet For Stats";
	
	/**
	 * This test checks the getStatictics from transaction route
	 * It should return "No Transactions Yet For Stats"
	 */
	@Test
	public void A_TransactionGetStatsWithoutTransactions() {
		Boolean EXPECTED = true;
		
		Boolean connectionResult = false;	
		
		try {
			//Delete any possible transaction
			requestWithoutBody(BASE_STRING + "/" + "transactions", 0, "DELETE");
			
			//Request and check if is empty
			String str = requestWithoutBody(BASE_STRING+"/" + "transactions", 200, "GET");			
			assertEquals(SUCCESS_ON_NO_TRANSACTIONS, str);
	        
	        connectionResult = true;
		} catch(IOException e) {
			connectionResult = false;
		}
		
		assertEquals(EXPECTED, connectionResult);
	}

//	
//	/**
//	 * This test checks an nonexistent route by passing a string as Id
//	 * It should return 404 Not Found
//	 */
	@Test
	public void C_InexistantRoute() {
		Boolean EXPECTED = true;
		
		Boolean connectionResult = false;	
		
		try {
			
			URL url = new URL(BASE_STRING + "/id");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");	        
	        conn.getResponseMessage();
	        
	        assertEquals(404, conn.getResponseCode());
	        
	        connectionResult = true;
		} catch(IOException e) {
			connectionResult = false;
		}
		
		assertEquals(EXPECTED, connectionResult);
	}
//	
//	/**
//	 * This test the create method
//	 * It should return 201 Created since transaction request has timestamp in the last 30 seconds with date 2014-07-17T09:59:51.312Z being the assumed Now
//	 */
	@Test
	public void D_PostCreate_Success() {
		Boolean EXPECTED = true;
		
		Boolean connectionResult = false;
		
		try {
			//Delete any possible user
			requestWithoutBody(BASE_STRING + "/" + "transactions", 0, "DELETE");
			
			String result = requestWithBody(BASE_STRING+"/"+"transactions",201,  TEST_TransactionWithin30Secs, "POST");
	        assertEquals(SUCCESS_ON_CREATION, result);
	        
	        connectionResult = true;
		} catch(IOException e) {
			connectionResult = false;
		}
		
		assertEquals(EXPECTED, connectionResult);
	}
	
//	/**
//	 * This test the create method
//	 * It should return 204 Created since transaction request has timestamp in the older than 30 seconds with date 2014-07-17T09:59:51.312Z being the assumed Now
//	 */
	@Test
	public void DPostCreateRequestWithTimestampOlderthan30secs() {
		Boolean EXPECTED = true;
		
		Boolean connectionResult = false;
		
		try {
			//Delete any possible user
			requestWithoutBody(BASE_STRING + "/" + "transactions", 0, "DELETE");
			
			//returns null because of 204
			String result = requestWithBody(BASE_STRING+"/"+"transactions",204, TEST_TransactionOlderthan30Secs, "POST");
	        assertEquals(FAILURE_ON_CREATION, result);
	        
	        connectionResult = true;
		} catch(IOException e) {
			connectionResult = false;
		}
		
		assertEquals(EXPECTED, connectionResult);
	}
//	
//	/**
//	 * This test the create method with a Repeated Id
//	 * It should return 409 Conflict
//	 */
//	@Test
//	public void E_PostCreate_Conflict() {
//		Boolean EXPECTED = true;
//		
//		Boolean connectionResult = false;	
//				
//		try {
//			//Insert a Transaction to ensure there will be a conflict
//			requestWithBody(0, TEST_Transaction, "POST");
//			
//			String result = requestWithBody(409, TEST_Transaction, "POST");
//			
//	        assertEquals(CONFLICT_ON_CREATION, result);
//	        
//	        connectionResult = true;
//		} catch(IOException e) {
//			connectionResult = false;
//		}
//		
//		assertEquals(EXPECTED, connectionResult);
//	}
//	
//	/**
//	 * This test the create method if the message body is json request is invalid
//	 * It should return 400 Bad Request
//	 */
	@Test
	public void F_PostCreateBadRequest_NullBody() {
		Boolean EXPECTED = true;
		
		Boolean connectionResult = false;	
				
		try {
			//Delete any possible user
			requestWithoutBody(BASE_STRING + "/" + "transactions", 0, "DELETE");
			
			String result = requestWithBody(BASE_STRING + "/"+"transactions",400,"{}", "POST");
			
	        assertEquals(WRONG_MSG_BODY, result);
	        
	        connectionResult = true;
		} catch(IOException e) {
			connectionResult = false;
		}
		
		assertEquals(EXPECTED, connectionResult);
	}


	

	




	
	/**
	 * This test tries to delete all Transaction
	 * It should return 204 NO CONTENT
	 */
	@Test
	public void P_Delete_Success() {
		Boolean EXPECTED = true;
		
		Boolean connectionResult = false;
		
		try {
			
			//will return null because of 204
			String str = requestWithoutBody(BASE_STRING + "/" + "transactions", 204, "DELETE");
	        assertEquals(SUCCESS_ON_DELETE, str);
	        
	        connectionResult = true;
		} catch(IOException e) {
			connectionResult = false;
		}
		
		assertEquals(EXPECTED, connectionResult);
	}
		
	/**
	 * Method used as helper to send request with body
	 * 
	 * @param errorCode the expected error code of the request (0 to ignore assertion)
	 * @param content the body content of the request
	 * @param method the method used for the request
	 * @return the server string response
	 * @throws IOException
	 */
	private String requestWithBody(String baseString,int errorCode, String content, String method) throws IOException {
		BufferedReader br;
		
		URL url = new URL(baseString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");	     
        conn.setDoOutput(true);
                
        String str = content;
        
        OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream());
        outWriter.write(str);
        
        outWriter.flush();
        outWriter.close();
        
        conn.getResponseMessage();
        
        int responseCode = conn.getResponseCode();
        
        if(responseCode >= 200 && responseCode <= 399)
        	br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        else
        	br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        
        String s = br.readLine();
        
        if(errorCode != 0)
        	assertEquals(errorCode, responseCode);
        
        return s;
	}
	
	/**
	 * Method used as helper to send request with no body
	 * 
	 * @param baseUrl the url in which the server that will receive the request is localized
	 * @param errorCode the expected error code of the request (0 to ignore assertion)
	 * @param method the method used for the request
	 * @return the server string response
	 * @throws IOException
	 */
	private String requestWithoutBody(String baseUrl, int errorCode, String method) throws IOException{
		BufferedReader br;
			
		URL url = new URL(baseUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Accept", "application/json");
        conn.getResponseMessage();
        
        int responseCode = conn.getResponseCode();
        
        if(responseCode >= 200 && responseCode <= 399 && conn.getInputStream() != null)
        	br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        else
        	br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        
        if(errorCode != 0)
        	assertEquals(errorCode,responseCode);
                
        return br.readLine(); 
	}

}
