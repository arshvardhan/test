package com.kelltontech.maxisgetit.requests;

import java.util.Hashtable;

import android.content.Context;

public class RegistrationRequest extends MaxisBaseRequest {
	public static final String METHOD_REGISTER = "userRegistration.xml";
	private static final String KEY_NAME = "name";
	private static final String KEY_MOBILE = "mobile";
	private static final String KEY_EMAIL = "email";
	private String name;
	private String mobile;
	private String email;

	public RegistrationRequest(Context context, String name, String mobile) {
		super(context);
		this.name = name;
		this.mobile = mobile;
	}

	@Override
	public Hashtable<String, String> getRequestHeaders(String screenName) {
		Hashtable<String, String> ht = getDefaultHeaders(screenName);
		ht.put(KEY_NAME, name);
		ht.put(KEY_MOBILE, mobile);
		if (email != null)
			ht.put(KEY_EMAIL, email);
		return ht;
	}

	public String getMethodName() {
		return METHOD_REGISTER;
	}

	public String getMobileNumber() {
		return mobile;
	}

	public String getUserName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
