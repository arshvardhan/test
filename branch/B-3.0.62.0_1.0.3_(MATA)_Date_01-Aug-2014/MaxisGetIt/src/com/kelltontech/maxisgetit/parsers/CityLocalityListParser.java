package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.response.GenralListResponse;

public class CityLocalityListParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_CITY_ROOT = "City";
	public static final String TAG_LOCALITY_ROOT = "Locality";
	public static final String TAG_ID = "Id";
	public static final String TAG_NAME = "Name";
	private ArrayList<CityOrLocality> cityLocalityList=new ArrayList<CityOrLocality>();
	private CityOrLocality cityLocality;
	private GenralListResponse listResponse=new GenralListResponse();
	@Override
	public IModel parse(String payload) throws Exception {
		init();
		Log.d("maxis", "XML " + payload);
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), this);
		listResponse.setCityOrLocalityList(cityLocalityList);
		return listResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_CITY_ROOT)||localName.equalsIgnoreCase(TAG_LOCALITY_ROOT)) {
			cityLocality = new CityOrLocality();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			listResponse.setErrorCode(getInt(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			listResponse.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ID)) {
			Log.d("maxis", getNodeValue());
			cityLocality.setId(getInt(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_NAME)) {
			Log.d("maxis", getNodeValue());
			cityLocality.setName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CITY_ROOT)||localName.equalsIgnoreCase(TAG_LOCALITY_ROOT)) {
			cityLocalityList.add(cityLocality);
		}

	}

}
