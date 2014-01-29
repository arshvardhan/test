package com.kelltontech.maxisgetit.requests;

import java.util.Hashtable;

import android.content.Context;

public class DetailRequest extends MaxisBaseRequest {
//	public static final String METHOD_DEAL_DETAIL = "viewDeal.xml";
	
	public static final String METHOD_DEAL_DETAIL = "viewDealRecord.xml";
	public static final String METHOD_COMPANY_DETAIL = "viewCompany.xml";
	private static final String KEY_DEAL_ID = "deal_id";
	private static final String KEY_COMPANY_ID = "company_id";
	private static final String KEY_CATEGORY_ID = "category_id";
	private String id;
	public boolean isDeal;
	private String catId;

	public DetailRequest(Context context, String id, boolean isDeal, String catId) {
		super(context);
		this.id = id;
		this.isDeal = isDeal;
		this.catId = catId;
	}

	@Override
	public Hashtable<String, String> getRequestHeaders() {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		if (isDeal) {
			ht.put(KEY_DEAL_ID, id);
		} else {
			ht.put(KEY_COMPANY_ID, id);
			ht.put(KEY_CATEGORY_ID, catId);
		}
		return ht;
	}

	public String getMethodName() {
		if (isDeal)
			return METHOD_DEAL_DETAIL;
		else
			return METHOD_COMPANY_DETAIL;
	}

}
