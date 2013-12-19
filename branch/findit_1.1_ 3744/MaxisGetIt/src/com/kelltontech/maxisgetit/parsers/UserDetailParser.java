package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import android.util.Log;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.response.UserDetailResponse;

public class UserDetailParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_USER = "User";
	public static final String TAG_USER_ID = "Id";
	public static final String TAG_USER_NAME = "Name";
	public static final String TAG_USER_MOBILE = "Mobile";
	public static final String TAG_USER_EMAIL = "Email";
	public static final String TAG_EMAIL_STATUS = "Email_Status";
	public static final String TAG_MOBILE_STATUS = "Status";
	public static final String TAG_Is_Company = "IsCompany";
	public static final String TAG_IS_OTP = "OTP";

	private UserDetailResponse response = new UserDetailResponse();

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		response = new UserDetailResponse();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), UserDetailParser.this);
		return response;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			response.setErrorCode(getInt(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			response.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_USER_ID)) {
			response.setUserId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_USER_NAME)) {
			response.setName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_USER_EMAIL)) {
			response.setEmail(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_USER_MOBILE)) {
			response.setMobile(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_MOBILE_STATUS)) {
			response.setMobileStatus(getInt(getNodeValue(), 0) == 1 ? true : false);
		} else if (localName.equalsIgnoreCase(TAG_EMAIL_STATUS)) {
			response.setEmailStatus(getInt(getNodeValue(), 0) == 1 ? true : false);
		}else if(localName.equalsIgnoreCase(TAG_Is_Company)){
			response.setCompany(getInt(getNodeValue(), 0) == 1 ? true : false);
		}else if(localName.equalsIgnoreCase(TAG_IS_OTP)){
			response.setOTP(getInt(getNodeValue(), 0) == 1 ? true : false);
		}
	}
}
