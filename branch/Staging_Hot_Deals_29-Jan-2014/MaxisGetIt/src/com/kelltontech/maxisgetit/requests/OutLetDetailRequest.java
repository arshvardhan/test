package com.kelltontech.maxisgetit.requests;

public class OutLetDetailRequest {

	private String deal_id;
	private String comp_id;
	private String l3cat_id;

	public String getL3cat_id() {
		return l3cat_id;
	}

	public void setL3cat_id(String l3cat_id) {
		this.l3cat_id = l3cat_id;
	}

	public String getDeal_id() {
		return deal_id;
	}

	public void setDeal_id(String deal_id) {
		this.deal_id = deal_id;
	}

	public String getComp_id() {
		return comp_id;
	}

	public void setComp_id(String comp_id) {
		this.comp_id = comp_id;
	}
	
}
