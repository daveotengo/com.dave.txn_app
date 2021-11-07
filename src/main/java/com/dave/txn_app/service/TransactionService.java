package com.dave.txn_app.service;

import javax.ws.rs.core.Response;

import com.dave.txn_app.model.Transaction;

public interface TransactionService {
	public Response getTrxStats();
	
	public Response create(Transaction data);
	
	public Response remove();
}
