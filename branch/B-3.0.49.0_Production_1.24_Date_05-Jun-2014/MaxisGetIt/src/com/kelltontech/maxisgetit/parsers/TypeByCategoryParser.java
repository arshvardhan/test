package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.TypeByCategory;
import com.kelltontech.maxisgetit.response.TypeByCategoryResponse;

/**
 * @author Arsh Vardhan Atreya
 * @email arshvardhan.atreya@kelltontech.com
 */

public class TypeByCategoryParser extends AbstractSAXParser {

	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_TYPES = "Types";
	public static final String TAG_TYPE = "Type";
	public static final String TAG_LABEL = "Label";
	
	private ArrayList<TypeByCategory> typeByCategoryList = new ArrayList<TypeByCategory>();
	private TypeByCategory typeByCategory;
	private TypeByCategoryResponse response;

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		Log.d("maxis", "XML " + payload);
		response = new TypeByCategoryResponse();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), TypeByCategoryParser.this);
		response.setTypeByCategoryList(typeByCategoryList);
		return response;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_TYPES)) {
			typeByCategory = new TypeByCategory();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis: " + TAG_ERROR_CODE, getNodeValue());
			response.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis: " + TAG_ERROR_MESSAGE, getNodeValue());
			response.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_TYPE)) {
			Log.d("maxis: " + TAG_TYPE, getNodeValue());
			typeByCategory.setType(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_LABEL)) {
			Log.d("maxis: " + TAG_LABEL, getNodeValue());
			typeByCategory.setLabel(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_TYPES)) {
			typeByCategoryList.add(typeByCategory);
		}
	}

}
