package com.kelltontech.maxisgetit.dao;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

public class MaxisStore {
	private static MaxisStore storeObj;
	private SharedPreferences sharedPreferences;
	private static Context appContext;
	private static final String MAXIS_STORE = "MAXIS_STORE";
	private static final String KEY_LOCATION_AWARE = "LOCATION_AWARE";
	private static final String KEY_EN_SELECTED = "KEY_EN_SELECTED";
	private static final String KEY_MOBILE_NUMBER = "MOBILE_USER";
	private static final String KEY_USER_NAME = "NAME_USER";
	private static final String KEY_USER_EMAIL = "EMAIL_USER";
	private static final String KEY_USER_ID = "USER_ID";
	private static final String KEY_IS_LOGGED_IN = "IS_LOGIN";
	private static final String KEY_IS_Registered_User = "IS_REGISTERED";
	private static final String KEY_IS_REG_VERIFIED = "IS_VERIFIED";
	private static final String KEY_IS_COMPANY = "Is_User_company";
	private static final String KEY_APP_ACTIVATED="APP_ACTIVATION_STATUS";
	private static final String KEY_TnC_STATUS = "TnC_STATUS";
	private static final String KEY_USER_DIALOG_DECISION = "USER_DIALOG_DECISION";
	// private static final String KEY_USER_FAV_COMPANYS = "USER_FAV_COMPANY_ID_CATEGORY_ID";
	
	public static MaxisStore getStore(Context context) {
		if (storeObj == null) {
			storeObj = new MaxisStore(context);
		}
		return storeObj;
	}

	private MaxisStore(Context context) {
		sharedPreferences = context.getSharedPreferences(MAXIS_STORE, context.MODE_PRIVATE);
		appContext=context;
	}
	public boolean isAppActivated(){
		boolean isActivated=sharedPreferences.getBoolean(KEY_APP_ACTIVATED, false);
		if(!isActivated){
//			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//				File directory=Environment.getExternalStorageDirectory();
//				File file=new File(directory + "/findit333.rss");
//				if(file.exists()){
//					Log.e("maxis", directory + "/findit333.rss  file exists");
//					return true;
//				}
//			}
			return false;
		}else{
			return true;
		}
	}
	
	public void setAppActivated(boolean status){
		if(status!=sharedPreferences.getBoolean(KEY_APP_ACTIVATED, false)){
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(KEY_APP_ACTIVATED, status);
			editor.commit();
		}
		// check external store if file not found recreate it
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File directory=Environment.getExternalStorageDirectory();
			File file=new File(directory + "/findit.rss");
			Log.e("maxis", directory + "/findit.rss");
			if(!file.exists()){
				try {
					file.createNewFile();
					Log.d("maxis","successful file write");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean isRegisteredUser() {
		return sharedPreferences.getBoolean(KEY_IS_Registered_User, false);
	}

	public void setUserRegistered(boolean isRegisted) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_IS_Registered_User, isRegisted);
		editor.commit();
	}
	
	public boolean isTnCAccepted() {
		return sharedPreferences.getBoolean(KEY_TnC_STATUS, false);
	}

	public void setTnCStatus(boolean tnCStatus) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_TnC_STATUS, tnCStatus);
		editor.commit();
	}
	
	public boolean isUserDecisionRemembered() {
		return sharedPreferences.getBoolean(KEY_USER_DIALOG_DECISION, false);
	}
	
	/*public long getUserDecisionTime() {
		return sharedPreferences.getLong(KEY_USER_DIALOG_TIME, 0);
	}*/
	
	public void setUserDecisionWithTime(boolean userDecision) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_USER_DIALOG_DECISION, userDecision);
		editor.commit();
	}
	
	public boolean isUserCompany() {
		return sharedPreferences.getBoolean(KEY_IS_COMPANY, false);
	}

	public void setUserCompany(boolean isCompany) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_IS_COMPANY, isCompany);
		editor.commit();
	}
	public boolean isVerifiedUser() {
		return sharedPreferences.getBoolean(KEY_IS_REG_VERIFIED, false);
	}

	public void setVerifiedUser(boolean isVerified) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_IS_REG_VERIFIED, isVerified);
		editor.commit();
	}

	public boolean isLoogedInUser() {
		return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
	}

	public void setLoggedInUser(boolean isLoggedIn) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
		editor.commit();
	}

	public String getUserID() {
		return sharedPreferences.getString(KEY_USER_ID, null);
	}

	public void setUserID(String userId) {
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_USER_ID, userId);
		editor.commit();
	}

	public String getUserName() {
		return sharedPreferences.getString(KEY_USER_NAME, null);
	}

	public void setUserName(String userName) {
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_USER_NAME, userName);
		editor.commit();
	}

	/*public String getFavCompanies() {
		return sharedPreferences.getString(KEY_USER_FAV_COMPANYS, null);
	}

	public void setFavCompanies(ArrayList<String> companyIdCategoryId) {
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_USER_FAV_COMPANYS, TextUtils.join(",", companyIdCategoryId));
		editor.commit();
	}*/
	
	public String getUserMobileNumber() {
		return sharedPreferences.getString(KEY_MOBILE_NUMBER, null);
	}
	
	public String getUserMobileNumberToDispaly() {
		 String phone = sharedPreferences.getString(KEY_MOBILE_NUMBER, null);
		 if(phone != null)
			 phone = phone.substring(2);
		 return phone;
	}

	public void setUserMobileNumber(String mobile) {
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_MOBILE_NUMBER, mobile);
		editor.commit();
	}

	public boolean isEnglishSelected() {
		return sharedPreferences.getBoolean(KEY_EN_SELECTED, true);
	}

	public void setEnglishSelected(boolean isEnglishSelected) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_EN_SELECTED, isEnglishSelected);
		editor.commit();
	}

	public boolean isLocalized() {
		return sharedPreferences.getBoolean(KEY_LOCATION_AWARE, true);
	}

	public String getLocaleCode() {
		if (isEnglishSelected())
			return "en";
		else
			return "ms";
	}

	public void setLocalized(boolean isLocalized) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_LOCATION_AWARE, isLocalized);
		editor.commit();
	}

	public void setUserEmailId(String userEmail) {
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_USER_EMAIL, userEmail);
		editor.commit();
	}
	public String getUserEmail() {
		return sharedPreferences.getString(KEY_USER_EMAIL, null);
	}
}
