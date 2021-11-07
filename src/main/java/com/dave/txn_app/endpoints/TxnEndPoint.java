package com.dave.txn_app.endpoints;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dave.txn_app.model.Transaction;
import com.dave.txn_app.response.GetTransResponse;
import com.dave.txn_app.server.TxnServer;
import com.dave.txn_app.service.TansactionServiceImpl;
import com.dave.txn_app.service.TransactionService;
import com.dave.txn_app.utils.ResponseMessages;

@Path("transactions")
public class TxnEndPoint {
	private static Logger _log = Logger.getLogger(TxnEndPoint.class.getName());


	 SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss.ssss");
	 
	 SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss");


	 //SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss");

	static private List<Transaction> transactions = new ArrayList<Transaction>();

	TransactionService transactionService = new TansactionServiceImpl();		

	
	
	/**
	 * getAll()
	 * 
	 * Gets the transaction`s ArrayList and builds into a JSON object
	 * 
	 * @return All transactions saved on the system.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTrxStats() {
		
		return transactionService.getTrxStats();
		 

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
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Transaction data) {

		return transactionService.create(data);
	}

	/**
	 * remove() Accepts empty request body Removes all transactions from the system
	 * 
	 * @return -> 204: if the system successfully remove the transaction
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove() {
		
		return transactionService.remove();

	}

	

}
