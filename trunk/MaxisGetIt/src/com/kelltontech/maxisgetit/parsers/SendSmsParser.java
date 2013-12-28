package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.response.SendSmsResponse;

public class SendSmsParser extends AbstractSAXParser {
	public static final String TAG_STATUS = "status";
	public static final String MSG_ROOT = "Results";
	
	private SendSmsResponse clResponse = new SendSmsResponse();

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		clResponse = new SendSmsResponse();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), SendSmsParser.this);
		return clResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_STATUS)) {
			Log.d("maxis", getNodeValue());
			clResponse.setStatusCode(getInt(getNodeValue(),0));
		} 
	}

}
