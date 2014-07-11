package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.AttributeGroup;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.response.LocalSearchResponse;

public class LocalSearchResponseParser extends AbstractSAXParser {
	private LocalSearchResponse localSearchResponse=new LocalSearchResponse();
	private CompanyDetail compDetail;
	private AttributeGroup attrGroup;
	private ArrayList<String> contactList;
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	private static final String TAG_COMPANY = "Company";
	private static final String TAG_ID = "ID";
	private static final String TAG_TITLE = "Title";
	private static final String TAG_CONTACTS = "Contacts";
	private static final String TAG_CONTACT_NUMBER = "Contact_Number";
	private static final String TAG_EMAIL_ID = "Email_Id";
	private static final String TAG_LOCALITY = "Locality";
	private static final String TAG_CITY = "City";
	private static final String TAG_PIN = "PinCode";
	private static final String TAG_WEBSITE = "Website";
	private static final String TAG_GEO_CORDINATE = "Geo-cordinates";
	private static final String TAG_LATITUDE = "Latitude";
	private static final String TAG_LONGITUDE = "Longitude";
	private static final String TAG_DESC = "Description";
	private static final String TAG_IMAGE_URL = "Image_Url";
	private static final String TAG_SHARE_TEXT = "Sharing_Text";
	private static final String TAG_ATTRIBUTE_GROUP = "Attribute_Group";
	private static final String TAG_GROUP_VAL = "Values";
	private static final String TAG_ATTR_NAME = "Label";
	private static final String TAG_ATTR_VAL = "Value";
	private static final String TAG_CALL_NUMBER = "Call_Number";
	private static final String TAG_SMS_NUMBER = "Sms_Number";
	private static final String TAG_BILLING_NUMBER = "Billing_Number";
	private static final String TAG_IS_PAID="Is_Paid";
	private static final String TAG_CONTACT_CHANNEL="Contact_Chanel";
	@Override
	public IModel parse(String payload) throws Exception {
		init();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), LocalSearchResponseParser.this);
		return localSearchResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_CONTACTS)) {
			contactList = new ArrayList<String>();
		}if(localName.equalsIgnoreCase(TAG_GROUP_VAL)){
			attrGroup=new AttributeGroup();
		}else if(localName.equalsIgnoreCase(TAG_COMPANY)){
			compDetail=new CompanyDetail();
		}else if(localName.equalsIgnoreCase(TAG_CONTACT_CHANNEL)){
			compDetail.setContactChannelExists(true);
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			localSearchResponse.setErrorCode(getInt(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			localSearchResponse.setServerMessage(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_ID)) {
			compDetail.setId(getNodeValue());
		}if (localName.equalsIgnoreCase(TAG_TITLE)) {
			compDetail.setTitle(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CONTACTS)) {
			compDetail.setContacts(contactList);
		} else if (localName.equalsIgnoreCase(TAG_CONTACT_NUMBER)) {
			String number=getNodeValue();
			if(number.trim().length()>0)
				contactList.add(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_EMAIL_ID)) {
			compDetail.setMailId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_WEBSITE)) {
			compDetail.setWebsite(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_LATITUDE)) {
			compDetail.setLatitude(getDouble(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_LONGITUDE)) {
			compDetail.setLongitude(getDouble(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_DESC)) {
			compDetail.setDescription(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_IMAGE_URL)) {
			compDetail.setImageUrl(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_SHARE_TEXT)) {
			compDetail.setSharingText(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_LOCALITY)) {
			compDetail.setLocality(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_CITY)) {
			compDetail.setCity(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_PIN)) {
			compDetail.setPincode(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_ATTR_NAME)) {
			attrGroup.setLable(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_ATTR_VAL)) {
			attrGroup.addValue(getNodeValue());
		}else if(localName.equalsIgnoreCase(TAG_GROUP_VAL)){
			compDetail.addAttrGroups(attrGroup);
		}else if(localName.equalsIgnoreCase(TAG_CALL_NUMBER)){
			compDetail.setCallNumber(getNodeValue());
		}else if(localName.equalsIgnoreCase(TAG_SMS_NUMBER)){
			compDetail.setSmsNumber(getNodeValue());
		}else if(localName.equalsIgnoreCase(TAG_BILLING_NUMBER)){
			compDetail.setBillingNumber(getNodeValue());
		}else if(localName.equalsIgnoreCase(TAG_IS_PAID)){
			compDetail.setPaid(getInt(getNodeValue(), 0));
		}else if(localName.equalsIgnoreCase(TAG_COMPANY)){
			localSearchResponse.addCompanyDetail(compDetail);
		}
	}

}
