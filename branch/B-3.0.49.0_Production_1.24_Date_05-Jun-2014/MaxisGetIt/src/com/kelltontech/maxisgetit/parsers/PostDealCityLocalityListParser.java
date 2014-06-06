package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.PostDealCityOrLoc;
import com.kelltontech.maxisgetit.response.PostDealCityLocListResponse;

public class PostDealCityLocalityListParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_CITY_ROOT = "Cities";
	public static final String TAG_LOCALITY_ROOT = "Localities";
	public static final String TAG_CITY_NAME = "CityName";
	public static final String TAG_LOCALITY_NAME = "Locality";
	private ArrayList<PostDealCityOrLoc> cityLocalityList;
	private PostDealCityOrLoc city;
	private PostDealCityOrLoc locality;
	private PostDealCityLocListResponse listResponse = new PostDealCityLocListResponse();

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		Log.d("maxis", "XML " + payload);
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), this);
		return listResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException,
			IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_CITY_ROOT)
				|| localName.equalsIgnoreCase(TAG_LOCALITY_ROOT)) {
			cityLocalityList = new ArrayList<PostDealCityOrLoc>();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			listResponse.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			listResponse.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CITY_NAME)) {
			Log.d("maxis", getNodeValue());
			city = new PostDealCityOrLoc();
			city.setName(getNodeValue());
			cityLocalityList.add(city);
		} else if (localName.equalsIgnoreCase(TAG_LOCALITY_NAME)) {
			Log.d("maxis", getNodeValue());
			locality = new PostDealCityOrLoc();
			locality.setName(getNodeValue());
			cityLocalityList.add(locality);
		} else if (localName.equalsIgnoreCase(TAG_CITY_ROOT)
				|| localName.equalsIgnoreCase(TAG_LOCALITY_ROOT)) {
			listResponse.setCityOrLocalityList(cityLocalityList);
		}

	}

}