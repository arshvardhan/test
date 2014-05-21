package com.kelltontech.maxisgetit.model;

import java.util.ArrayList;

public class Data {

	private String cid;
	private String cname;
	private ArrayList<CatDetails> catdetails;
	private String ccDid;
    private String ccBillingNo;
    private String ccDate;
    private String ccPaymentStatus;
    private String ccLeadStatus;
    private String ccSubscriptionStatus;
    private String maxisDidNumber;
    private String subscription;
    private String message;
    private String price;
    private String leadsReceivedDetailsCout;
	
	
	

	public String getCcDid() {
		return ccDid;
	}

	public void setCcDid(String ccDid) {
		this.ccDid = ccDid;
	}

	public String getCcBillingNo() {
		return ccBillingNo;
	}

	public void setCcBillingNo(String ccBillingNo) {
		this.ccBillingNo = ccBillingNo;
	}

	public String getCcDate() {
		return ccDate;
	}

	public void setCcDate(String ccDate) {
		this.ccDate = ccDate;
	}

	public String getCcPaymentStatus() {
		return ccPaymentStatus;
	}

	public void setCcPaymentStatus(String ccPaymentStatus) {
		this.ccPaymentStatus = ccPaymentStatus;
	}

	public String getCcLeadStatus() {
		return ccLeadStatus;
	}

	public void setCcLeadStatus(String ccLeadStatus) {
		this.ccLeadStatus = ccLeadStatus;
	}

	public String getCcSubscriptionStatus() {
		return ccSubscriptionStatus;
	}

	public void setCcSubscriptionStatus(String ccSubscriptionStatus) {
		this.ccSubscriptionStatus = ccSubscriptionStatus;
	}

	public String getMaxisDidNumber() {
		return maxisDidNumber;
	}

	public void setMaxisDidNumber(String maxisDidNumber) {
		this.maxisDidNumber = maxisDidNumber;
	}

	public String getSubscription() {
		return subscription;
	}

	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLeadsReceivedDetailsCout() {
		return leadsReceivedDetailsCout;
	}

	public void setLeadsReceivedDetailsCout(String leadsReceivedDetailsCout) {
		this.leadsReceivedDetailsCout = leadsReceivedDetailsCout;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public ArrayList<CatDetails> getCatdetails() {
		return catdetails;
	}

	public void setCatdetails(ArrayList<CatDetails> catdetails) {
		this.catdetails = catdetails;
	}

}
