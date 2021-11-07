package com.dave.txn_app.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;


public class Transaction {
	
	@NotNull
	@JsonProperty("id")
	private Integer id;
	
	@NotNull
	@JsonProperty("amount")
	private String amount;
	
	@NotNull
	@JsonProperty("timeStamp")
	private String timeStamp;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
//	public String getTimeStamp() {
//		return timeStamp;
//	}
//	public void setTimeStamp(String timeStamp) {
//		this.timeStamp = timeStamp;
//	}
	
	
	
	
//	public Transaction(Integer id, String amount, String timeStamp) {
//		super();
//		this.id = id;
//		this.amount = amount;
//		this.timeStamp = timeStamp;
//	}
	
	
	

	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Override
    public String toString() {
        String response = "{" +

				"\"id\": \""+ id +"\"," +

				"\"amount\": \""+ amount +"\"," +


                "\"timeStamp\" : \""+ timeStamp +"\"" +

                "}";


        return response;
    }
	

}
