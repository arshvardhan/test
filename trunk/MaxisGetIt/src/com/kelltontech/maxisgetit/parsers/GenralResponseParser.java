package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.parser.AbstractSAXParser;

public class GenralResponseParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_MESSAGE = "Message";
	private MaxisResponse response;

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		response = new MaxisResponse();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), GenralResponseParser.this);
		Log.d("maxis", "XML " + payload);
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
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)||localName.equalsIgnoreCase(TAG_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			response.setServerMessage(getNodeValue());
		}
	}

}
