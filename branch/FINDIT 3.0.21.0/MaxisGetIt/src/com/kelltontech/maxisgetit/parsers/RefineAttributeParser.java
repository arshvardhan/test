package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;

public class RefineAttributeParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_SELCTOR_ROOT = "Selector";
	public static final String TAG_SEARCH_KEY = "Search_Key";
	public static final String TAG_DISPLAY_NAME = "Display_Name";
	public static final String TAG_VLAUE = "Value";
	private SelectorDAO selector;
	private RefineSelectorResponse response = new RefineSelectorResponse();

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		Log.d("maxis", "XML " + payload);
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), this);
		return response;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_SELCTOR_ROOT)) {
			selector = new SelectorDAO();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			response.setErrorCode(getInt(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			response.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_SEARCH_KEY)) {
			Log.d("maxis", getNodeValue());
			selector.setSearchKey(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_DISPLAY_NAME)) {
			Log.d("maxis", getNodeValue());
			selector.setDisplayName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_VLAUE)) {
			selector.addSelectorValues(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_SELCTOR_ROOT)) {
			response.addSelector(selector);
		}

	}

}
