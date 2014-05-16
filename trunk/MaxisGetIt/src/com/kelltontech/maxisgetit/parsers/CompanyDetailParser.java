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
import com.kelltontech.maxisgetit.dao.CompanyReview;
import com.kelltontech.maxisgetit.dao.IconUrl;
import com.kelltontech.maxisgetit.dao.NearOutLets;
import com.kelltontech.maxisgetit.dao.OutLetsinDetail;

public class CompanyDetailParser extends AbstractSAXParser {
	private CompanyDetail compdDetail = new CompanyDetail();
	private NearOutLets nearLets;
	CompanyReview compReview;
	private AttributeGroup attrGroup;
	private ArrayList<String> contactList;
	private ArrayList<NearOutLets> nearOutLets;
	private ArrayList<IconUrl> urls;
	private IconUrl iconUrl;

	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	private static final String TAG_CID = "Company_ID";
	private static final String TAG_ID = "ID";
	private static final String TAG_CTITLE = "Company_Title";
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
	private static final String TAG_IS_PAID = "Is_Paid";
	private static final String TAG_CONTACT_CHANNEL = "Contact_Chanel";
	private static final String TAG_DISTANCE = "Distance";
	private static final String TAG_RATING = "rating";
	private static final String TAG_RATED_USER_COUNT = "ratedUserCount";
	private static final String TAG_STATE = "State";
	private static final String TAG_BUILD = "Building";
	private static final String TAG_SUB_LOCALITY = "SubLocality";
	private static final String TAG_LANDMARK = "Landmark";
	private static final String TAG_STREET = "Street";
	private static final String TAG_CAT_ID = "L3Catid";
	private static final String TAG_REVIEW = "Review";
	private static final String TAG_TOTAL_RECORD = "Total_Record";
	private static final String TAG_USER_NAME = "User_Name";
	private static final String TAG_USER_RATING = "Rating";
	private static final String TAG_REVIEW_DESC = "Review_Desc";
	private static final String TAG_CREATED = "Created";
	private static final String TAG_TOTAL_REVIEW_COUNT = "Total_Reviews";
	private static final String TAG_RECORD_TYPE = "Record_Type";

	private static final String TAG_VALID_IN = "ValidIn";
	private static final String TAG_TILL_DATE = "Till_Date";
	private static final String TAG_ICON_URL = "Icon_Url";
	private static final String TAG_DEAL_ICON_URL = "Deal_Icon_Url";
	private static final String TAG_OUTLETS = "Outlets";
	private static final String TAG_NEAR_OUTLET = "NearOutlet";
	private static final String TAG_NEAR_OUTLET_LAT = "Outlet_Latitude";
	private static final String TAG_NEAR_OUTLET_LONG = "Outlet_Longitude";
	private static final String TAG_COMP_ID = "Cid";
	private static final String TAG_TERMS_ND_CON = "TermsCondition";

	private boolean isReviewRating = false;

	private static final String TAG_MAP_ICON = "Map_Icon";

	private static final String TAG_VIDEO = "CVideo";

	private static final String TAG_SOURCE = "Source";

	private static String TAG_DEAL_DETAIL_URL = "DealDetailsUrl";

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		Log.d("maxis", "XML " + payload);
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()),
				CompanyDetailParser.this);
		return compdDetail;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException,
			IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_CONTACTS)) {
			contactList = new ArrayList<String>();
		}
		if (localName.equalsIgnoreCase(TAG_GROUP_VAL)) {
			attrGroup = new AttributeGroup();
		} else if (localName.equalsIgnoreCase(TAG_CONTACT_CHANNEL)) {
			compdDetail.setContactChannelExists(true);
		} else if (localName.equalsIgnoreCase(TAG_REVIEW)) {
			Log.d("maxis", getNodeValue());
			compReview = new CompanyReview();
			isReviewRating = true;
		} else if (localName.equalsIgnoreCase(TAG_OUTLETS)) {
			nearOutLets = new ArrayList<NearOutLets>();
		} else if (localName.equalsIgnoreCase(TAG_NEAR_OUTLET)) {
			nearLets = new NearOutLets();
		} else if (localName.equalsIgnoreCase(TAG_ICON_URL)) {
			urls = new ArrayList<IconUrl>();
		} else if (localName.equalsIgnoreCase(TAG_DEAL_ICON_URL)) {
			iconUrl = new IconUrl();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CID)
				|| localName.equalsIgnoreCase(TAG_ID)) {
			compdDetail.setId(getNodeValue());
		}
		if (localName.equalsIgnoreCase(TAG_TITLE)
				|| localName.equalsIgnoreCase(TAG_CTITLE)) {
			compdDetail.setTitle(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CONTACTS)) {
			compdDetail.setContacts(contactList);
		} else if (localName.equalsIgnoreCase(TAG_CONTACT_NUMBER)) {
			String number = getNodeValue();
			if (number.trim().length() > 0)
				contactList.add(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_EMAIL_ID)) {
			compdDetail.setMailId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_WEBSITE)) {
			compdDetail.setWebsite(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_LATITUDE)) {
			compdDetail.setLatitude(getDouble(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_LONGITUDE)) {
			compdDetail.setLongitude(getDouble(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_DESC)) {
			compdDetail.setDescription(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_IMAGE_URL)) {
			compdDetail.setImageUrl(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_SHARE_TEXT)) {
			compdDetail.setSharingText(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_LOCALITY)) {
			compdDetail.setLocality(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CITY)) {
			compdDetail.setCity(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_PIN)) {
			compdDetail.setPincode(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ATTR_NAME)) {
			attrGroup.setLable(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ATTR_VAL)) {
			attrGroup.addValue(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_GROUP_VAL)) {
			compdDetail.addAttrGroups(attrGroup);
		} else if (localName.equalsIgnoreCase(TAG_CALL_NUMBER)) {
			compdDetail.setCallNumber(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_SMS_NUMBER)) {
			compdDetail.setSmsNumber(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BILLING_NUMBER)) {
			compdDetail.setBillingNumber(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_IS_PAID)) {
			compdDetail.setPaid(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_DISTANCE)) {
			compdDetail.setDistance(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_RATING)) {
			if (isReviewRating) {
				compReview.setRating(getFloat(getNodeValue(), 0));
			} else {
				compdDetail.setRating(getFloat(getNodeValue(), 0));
			}
		} else if (localName.equalsIgnoreCase(TAG_RATED_USER_COUNT)) {
			compdDetail.setRatedUserCount(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_STATE)) {
			compdDetail.setState(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BUILD)) {
			compdDetail.setBuilding(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_LANDMARK)) {
			compdDetail.setLandmark(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_SUB_LOCALITY)) {
			compdDetail.setSubLocality(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_STREET)) {
			compdDetail.setStreet(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CAT_ID)) {
			compdDetail.setCatId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_RECORD_TYPE)) {
			compdDetail.setRecordType(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_USER_NAME)) {
			Log.d("maxis", getNodeValue());
			compReview.setUserName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_REVIEW_DESC)) {
			Log.d("maxis", getNodeValue());
			compReview.setReviewDesc(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CREATED)) {
			Log.d("maxis", getNodeValue());
			compReview.setReportedOn(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_REVIEW)) {
			Log.d("maxis", getNodeValue());
			compdDetail.getCompanyReviewList().add(compReview);
			isReviewRating = false;
		} else if (localName.equalsIgnoreCase(TAG_TOTAL_REVIEW_COUNT)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setTotalReviewCount(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_VALID_IN)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setValidIn(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_TILL_DATE)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setValidDate(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_NEAR_OUTLET_LAT)) {
			Log.d("maxis", getNodeValue());
			nearLets.setOutLetLat(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_NEAR_OUTLET_LONG)) {
			Log.d("maxis", getNodeValue());
			nearLets.setOutLetLong(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_NEAR_OUTLET)) {
			Log.d("maxis", getNodeValue());
			nearOutLets.add(nearLets);
		} else if (localName.equalsIgnoreCase(TAG_OUTLETS)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setNearoutlets(nearOutLets);
		} else if (localName.equalsIgnoreCase(TAG_DEAL_ICON_URL)) {
			Log.d("maxis", getNodeValue());
			iconUrl.setDealIconUrl(getNodeValue());
			urls.add(iconUrl);
			compdDetail.setNearoutlets(nearOutLets);
		} else if (localName.equalsIgnoreCase(TAG_ICON_URL)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setIconUrl(urls);
		} else if (localName.equalsIgnoreCase(TAG_COMP_ID)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setCid(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_TERMS_ND_CON)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setTermsNdCondition(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_MAP_ICON)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setMapIcon(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_VIDEO)) {
			Log.d("maxis", getNodeValue());
			compdDetail.setVideoUrl(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_SOURCE)) {
			compdDetail.setSource(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_DEAL_DETAIL_URL)) {
			compdDetail.setDealDetailUrl(getNodeValue());
		}

	}

}
