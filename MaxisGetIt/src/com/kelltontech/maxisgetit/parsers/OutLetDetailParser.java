package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.OutLet;
import com.kelltontech.maxisgetit.dao.OutLetDetails;

public class OutLetDetailParser extends AbstractSAXParser {

	private ArrayList<OutLet> outletDetails;
	private OutLet outLet;
	OutLetDetails details = new OutLetDetails();
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_OUTLET_DETAILS = "OutLetDetails";
	public static final String TAG_OUTLET = "OutLet";
	private static final String TAG_ID = "Id";
	private static final String TAG_TITLE = "Title";
	private static final String TAG_CONTACT_NO = "Contact_No";
	private static final String TAG_PHONE_NO = "Phone_No";
	public static final String TAG_ADDRESS = "Address";
	public static final String OUTLET_ICON_URL = "Outlet_Icon_Url";

	private static final String TAG_LATITUDE = "Latitude";
	private static final String TAG_LONGITUDE = "Longitude";
	public static final String TAG_OUTLET_CAT_ID = "OutletCatId";

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()),
				OutLetDetailParser.this);
		return details;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException,
			IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_OUTLET_DETAILS)) {
			outletDetails = new ArrayList<OutLet>();
		} else if (localName.equalsIgnoreCase(TAG_OUTLET)) {
			outLet = new OutLet();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			details.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			details.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ID)) {
			outLet.setId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_TITLE)) {
			outLet.setTitle(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_PHONE_NO)) {
			outLet.setPhone_no(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ADDRESS)) {

			outLet.setAddress(getNodeValue());
		} else if (localName.equalsIgnoreCase(OUTLET_ICON_URL)) {
			outLet.setIcon_url(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_LATITUDE)) {
			outLet.setLat(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_LONGITUDE)) {
			outLet.setLongt(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_OUTLET_CAT_ID)) {
			outLet.setCatid(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_OUTLET)) {
			outletDetails.add(outLet);
		} else if (localName.equalsIgnoreCase(TAG_OUTLET_DETAILS)) {
			details.setOutlet(outletDetails);
		}
	}

}
