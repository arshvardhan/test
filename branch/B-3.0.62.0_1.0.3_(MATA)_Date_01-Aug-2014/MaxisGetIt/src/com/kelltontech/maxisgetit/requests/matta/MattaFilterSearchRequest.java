package com.kelltontech.maxisgetit.requests.matta;

import org.json.JSONObject;

import android.content.Context;
/**
 * @author arsh.vardhan
 * @modified 19-Aug-2014
 */
public class MattaFilterSearchRequest extends MattaRequest {
	
	private JSONObject postData;
	private String searchType;
	private String id;
	private String source;
	private String hallId;
	
	public MattaFilterSearchRequest(Context context) {
		super(context);
	}
	public JSONObject getPostData() {
		return postData;
	}
	public void setPostData(JSONObject postData) {
		this.postData = postData;
	}
	public String getsearchType() {
		return searchType;
	}
	public void setsearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getHallId() {
		return hallId;
	}
	public void setHallId(String hallId) {
		this.hallId = hallId;
	}	
}