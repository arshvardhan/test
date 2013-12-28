package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.KeyValuePair;
import com.kelltontech.maxisgetit.dao.ReportErrorMandatoryFields;
import com.kelltontech.maxisgetit.response.PostRatingResponse;

public class PostRatingParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	private static final String TAG_RATING = "rating";
	private static final String TAG_RATED_USER_COUNT = "ratedUserCount";
	
	PostRatingResponse response;
	@Override
	public IModel parse(String payload) throws Exception {
		init();
		response = new PostRatingResponse();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), PostRatingParser.this);
		return response;
	}

	
	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			response.setErrorCode(getInt(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			response.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_RATING)) {
			Log.d("maxis", getNodeValue());
			response.setRating(getFloat(getNodeValue()));
		} else if (localName.equalsIgnoreCase(TAG_RATED_USER_COUNT)) {
			Log.d("maxis", getNodeValue());
			response.setRatedUserCount(getInt(getNodeValue()));
		}
	}


	@Override
	public void onStartElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException,
			IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}
}
