package com.kelltontech.maxisgetit.model.uploadImage;

import org.json.JSONException;
import org.json.JSONObject;


public class ResponseUploadPhoto {
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	private boolean isSuccess;
	private String errorMsg;
	public ResponseUploadPhoto fromJson(String jsonString)
	{
		try {
			System.out.println("**Tmd AppCheckPortalMembershi "+jsonString);
			JSONObject jsonObject = new JSONObject(jsonString);
			isSuccess=jsonObject.optInt("status_code")==1?true:false;
			errorMsg=jsonObject.optString("error");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

}
