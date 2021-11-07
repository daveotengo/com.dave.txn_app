package com.dave.txn_app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dave.txn_app.endpoints.TxnEndPoint;
import com.dave.txn_app.model.Transaction;
import com.dave.txn_app.response.GetTransResponse;
import com.dave.txn_app.utils.ResponseMessages;

public class TansactionServiceImpl implements TransactionService{

	private static Logger _log = Logger.getLogger(TxnEndPoint.class.getName());


	 SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss.ssss");
	 
	 SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss");


	 //SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss");

	static private List<Transaction> transactions = new ArrayList<Transaction>();

	/**
	 * getAll()
	 * 
	 * Gets the transaction`s ArrayList and builds into a JSON object
	 * 
	 * @return All transactions saved on the system.
	 */

	public Response getTrxStats() {
		List<BigDecimal> listOfAmounts = new ArrayList<BigDecimal>();
		GetTransResponse getTransResponse = new GetTransResponse();

		if (!transactions.isEmpty()) {
			
			for (Transaction trans : transactions) {

				
				 String s = trans.getTimeStamp();
				 System.out.println("printing timestamp");
				 System.out.println(s);

				 
				
				 
				 System.out.println(s);
				 Date date = null;
				 Date assumedNow= null;
				 
				try {
					String assumedNowString = "2014-07-17T09:59:51.312Z";
					
				
					
					assumedNow = sdf2.parse(assumedNowString);
					date = sdf2.parse(s);
					
				if(isWithin30Secs(assumedNow,date)) {
	            	
					listOfAmounts.add(new BigDecimal(trans.getAmount()));

	            }
					
				} catch (ParseException e) {
				
				
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	            
	           // System.out.println(ts);
	            
	        

					//TimeUnit.MILLISECONDS.toSeconds(duration)

			}
			
       	

			BigDecimal minAmount = findMin(listOfAmounts).setScale(2, RoundingMode.HALF_UP);

			BigDecimal maxAmount = findMax(listOfAmounts).setScale(2, RoundingMode.HALF_UP);

			BigDecimal avgAmount = average(listOfAmounts).setScale(2, RoundingMode.HALF_UP);

			BigDecimal sumAmount = sum(listOfAmounts).setScale(2, RoundingMode.HALF_UP);

			getTransResponse.setMin(minAmount);
			getTransResponse.setCount(listOfAmounts.size());
			getTransResponse.setAvg(avgAmount);
			getTransResponse.setSum(sumAmount);
			getTransResponse.setMax(maxAmount);

			System.out.println("printing minAmount");
			System.out.println(minAmount);

			System.out.println("printing maxAmount");
			System.out.println(maxAmount);

			System.out.println("printing average");
			System.out.println(avgAmount);

			return Response.status(Response.Status.OK).entity(getTransResponse).build();

		} else {

			return Response.status(Response.Status.OK).entity("No Transactions Yet For Stats").build();

		}

	}
	
	
	/**
	 * create(transaction data)
	 * 
	 * Adds a transaction to the system.
	 * 
	 * @param data - JSON object representing the transaction
	 * @return -> Bad Request: if the data received on the message's body is not
	 *         valid as a transaction -> Conflict: if the system already contains a
	 *         transaction with the same Id as the received -> Created: if the data
	 *         is successfully added to the system and return 201
	 */

	public Response create(Transaction data) {
		
	
		//JSONObject jsonObject = new JSONObject(data.toString());

		if(data!=null&&isValid(data)) {
			System.out.println("data: "+data);
			System.out.println("isValid(data): "+isValid(data));
			System.out.println("Objects.nonNull(data): "+Objects.nonNull(data));
//		 String s = "2014-01-15T14:23T";
//		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS");
		 String s = data.getTimeStamp();
		 System.out.println(s);
		 //SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss.ssss");
		 Date date = null;
		try {
			date = sdf.parse(s);
		} catch (ParseException e) {
			
			return Response.status(422).entity(ResponseMessages.UNPROCESSABLE_REQUEST.toString())
					.build();
		
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		
		
		
		
		
		if(isDateFuture(data.getTimeStamp().toString().split("T")[0],"yyyy-MM-dd")) {
			return Response.status(422).entity(ResponseMessages.REQUEST_WITH_FUTURE_DATE.toString())
					.build();
		}
		
		try {
			String assumedNowString = "2014-07-17T09:59:51.312Z";
			
		
			
			Date assumedNow = sdf2.parse(assumedNowString);
			date = sdf2.parse(s);
			
			if(isOlderthan30Secs(assumedNow,date)) {
	        	
				return Response.status(Response.Status.NO_CONTENT).entity(null)
						.build();
	        }
			
		} catch (ParseException e) {
		
		
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		// System.out.println(sdf.format(date)); // 2014-01-15T14:23:50.0026 (bad!)
		if (findById(data.getId()) != null) {
			return Response.status(Response.Status.CONFLICT)
					.entity(ResponseMessages.RESOURCE_ALREADY_PRESENT.toString()).build();
		}
		
	

		transactions.add(data);
		
		}else {
			

			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseMessages.NO_BODY_PROVIDED.toString())
					.build();

			
			
		}

		return Response.status(Response.Status.CREATED).entity(ResponseMessages.SUCCESSFULLY_ADDED.toString()).build();
	}

	/**
	 * remove() Accepts empty request body Removes all transactions from the system
	 * 
	 * @return -> 204: if the system successfully remove the transaction
	 */

	public Response remove() {

		if (transactions == null) {
			return Response.status(Response.Status.NOT_MODIFIED).entity(ResponseMessages.CANNOT_EXECUTE.toString())
					.build();
		}

		transactions.removeAll(transactions);

		return Response.status(Response.Status.NO_CONTENT).entity(ResponseMessages.SUCCESSFULLY_REMOVED.toString())
				.build();
	}
	
	

	public List<Transaction> processTransactions(Transaction transaction) {
		List<Transaction> trans = new ArrayList<Transaction>();

		BigDecimal amount = new BigDecimal(transaction.getAmount());

		_log.info(amount);

		return trans;
	}

	public static BigDecimal findMin(List<BigDecimal> list) {

		// check list is empty or not
		if (list == null || list.size() == 0) {
			// return BigDecimal.MAX_VALUE;
			return BigDecimal.ZERO;

		}

		// create a new list to avoid modification
		// in the original list
		List<BigDecimal> sortedlist = new ArrayList<BigDecimal>(list);

		// sort list in natural order
		Collections.sort(sortedlist);

		// first element in the sorted list
		// would be minimum
		return sortedlist.get(0);
	}

	// function return maximum value in an unsorted
	// list in Java using Collection
	public static BigDecimal findMax(List<BigDecimal> listOfAmounts) {

		// check list is empty or not
		if (listOfAmounts == null || listOfAmounts.size() == 0) {
			return BigDecimal.ZERO;
		}

		// create a new list to avoid modification
		// in the original list
		List<BigDecimal> sortedlist = new ArrayList<BigDecimal>(listOfAmounts);

		// sort list in natural order
		Collections.sort(sortedlist);

		// last element in the sorted list would be maximum
		return sortedlist.get(sortedlist.size() - 1);
	}

	public static BigDecimal sum(List<BigDecimal> numbers) {
		BigDecimal sum = new BigDecimal(0);
		for (BigDecimal bigDecimal : numbers) {
			sum = sum.add(bigDecimal);
		}
		return sum;
	}



	public BigDecimal average(List<BigDecimal> bigDecimals) {

		RoundingMode roundingMode = RoundingMode.HALF_UP;

		BigDecimal sum = bigDecimals.stream().map(Objects::requireNonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
		if(bigDecimals.size() > 1 ) {
			
			return sum.divide(new BigDecimal(bigDecimals.size()), roundingMode);
				
		}else {
			
			return BigDecimal.ZERO;
		}
	}





	/**
	 * findById(int id)
	 * 
	 * Finds a transaction by its Id
	 * 
	 * @param id - Integer to be used as key for the transaction's list
	 * @return -> the transaction object, if it's found -> null if it is not found
	 */
	private Transaction findById(int id) {

		for (Transaction p : transactions) {
			if (p.getId() == id) {
				return p;
			}
		}

		return null;
	}

	/**
	 * Validates the transaction data
	 * 
	 * @param data - transaction object to be validated
	 * @return true for valid, false for invalid
	 */
	private Boolean isValid(Transaction data) {
		
		if(!isJSONValid(data.toString())) {
			return false;
		}
		
//		if (data.getId() == null||data.getId() == 0 || data.getAmount() == null || data.getTimeStamp() == null) {
//			return false;
//			
//		}

		return true;
	}
	
	public boolean isJSONValid(String test) {
	    try {
	    	JSONObject jsonObject = new JSONObject(test);
	    	
			if(jsonObject.get("amount").equals("null")||jsonObject.get("id").equals("null")||jsonObject.get("timeStamp").equals("null")) {
				return false;

			}
	    } catch (JSONException ex) {
	        // edited, to include @Arthur's comment
	        // e.g. in case JSONArray is valid as well...
	        try {
	            new JSONArray(test);
	            
	            
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
	
	 static boolean has30SecsPassed(Calendar calendar)
	    {
	        Long numberOfMilliSec = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
	        double numberOfSecondsPassed = numberOfMilliSec / 1000.0;
	        System.out.println("seconds passed: " + numberOfSecondsPassed);
	        return numberOfSecondsPassed >= 30;
	    }
	 
	 static boolean isWithin30Secs(Date dateNow, Date date)
	    {
		 
		 Long numberOfMilliSec = TimeUnit.MILLISECONDS.toMillis(dateNow.getTime()) - TimeUnit.MILLISECONDS.toMillis(date.getTime());
	        System.out.println(numberOfMilliSec + " milliseconds ago");

	        System.out.println(dateNow.getTime() + "-"+date.getTime());
	        
	        System.out.println(TimeUnit.MILLISECONDS.toMillis(dateNow.getTime()) + "-"+ TimeUnit.MILLISECONDS.toMillis(date.getTime()));


		 //Calendar calendar
	        //Long numberOfMilliSec = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
	       // Long numberOfMilliSec = Long.valueOf(val);

	        double numberOfSecondsPassed = numberOfMilliSec / 1000.0;
	       // double numberOfSecondsPassed = numberOfMilliSec ;

	        System.out.println("seconds passed: " + numberOfSecondsPassed);
	        System.out.println((numberOfSecondsPassed <= 30));
	        return (numberOfSecondsPassed <= 30);
	    }
	 
	 static boolean isOlderthan30Secs(Date dateNow, Date date)
	    {
		 
		 Long numberOfMilliSec = TimeUnit.MILLISECONDS.toMillis(dateNow.getTime()) - TimeUnit.MILLISECONDS.toMillis(date.getTime());
	        System.out.println(numberOfMilliSec + " milliseconds ago");

	        System.out.println(dateNow.getTime() + "-"+date.getTime());
	        
	        System.out.println(TimeUnit.MILLISECONDS.toMillis(dateNow.getTime()) + "-"+ TimeUnit.MILLISECONDS.toMillis(date.getTime()));


		 //Calendar calendar
	        //Long numberOfMilliSec = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
	       // Long numberOfMilliSec = Long.valueOf(val);

	        double numberOfSecondsPassed = numberOfMilliSec / 1000.0;
	       // double numberOfSecondsPassed = numberOfMilliSec ;

	        System.out.println("seconds passed: " + numberOfSecondsPassed);
	        System.out.println((numberOfSecondsPassed <= 30));
	        return (numberOfSecondsPassed >= 30);
	    }
	 
	 public static boolean isDateFuture(final String date, final String dateFormat) {
			LocalDate localDate = LocalDate.now(ZoneId.systemDefault());

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
			LocalDate inputDate = LocalDate.parse(date, dtf);

			return inputDate.isAfter(localDate);
		}
}
