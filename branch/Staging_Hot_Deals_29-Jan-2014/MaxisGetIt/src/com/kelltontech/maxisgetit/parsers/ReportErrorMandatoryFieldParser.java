package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.ReportErrorMandatoryFields;
import com.kelltontech.maxisgetit.dao.KeyValuePair;
import com.kelltontech.maxisgetit.response.UserDetailResponse;

public class ReportErrorMandatoryFieldParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_ERROR_STATUS = "Error_Status";
	public static final String TAG_STATUS = "Status";
	public static final String TAG_ERROR_KEY = "Key";
	public static final String TAG_ERROR_VALUE = "Value";
	
	ReportErrorMandatoryFields response;
	KeyValuePair errorStatus;
	@Override
	public IModel parse(String payload) throws Exception {
		init();
		response = new ReportErrorMandatoryFields();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), ReportErrorMandatoryFieldParser.this);
		return response;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		
		if (localName.equalsIgnoreCase(TAG_ERROR_STATUS)) {
			ArrayList<KeyValuePair> errorStatusList = new ArrayList<KeyValuePair>();
			response.setErrorStatusList(errorStatusList);
		}else if (localName.equalsIgnoreCase(TAG_STATUS)) {
			errorStatus = new KeyValuePair();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			response.setErrorCode(getInt(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			response.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ERROR_KEY)) {
			errorStatus.setKey(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ERROR_VALUE)) {
			errorStatus.setValue(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_STATUS)) {
			response.getErrorStatusList().add(errorStatus);
		}
	}
}
