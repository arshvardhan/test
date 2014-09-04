package com.kelltontech.maxisgetit.parsers.matta;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.BuildConfig;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.dao.matta.MattaBanner;
import com.kelltontech.maxisgetit.dao.matta.MattaHallList;
import com.kelltontech.maxisgetit.response.matta.MattaHallListResponse;

/**
 * @author arsh.vardhan
 * @modified 19-Aug-2014
 */
public class MattaHallListParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_BANNERS = "Banners";
	public static final String TAG_BANNER = "Banner";
	public static final String TAG_BANNER_ID = "id";
	public static final String TAG_BANNER_IMAGE_URL = "image";
	public static final String TAG_BANNER_LANDING_URL = "landingUrl";
	public static final String TAG_BANNER_SCREEN_NAME = "screenName";
	public static final String TAG_BANNER_CATEGORY_ID = "categoryId";
	public static final String TAG_BANNER_ITEM_ID = "itemId";
	public static final String TAG_HALL_ROOT = "Hall";
	public static final String TAG_HALL_ID = "Id";
	public static final String TAG_HALL_NAME = "Name";
	public static final String TAG_HALL_IMAGE = "Image";
	public static final String TAG_HALL_SOURCE = "Source";
	public static final String TAG_HALL_THEME = "Theme";
	public static final String TAG_HALL_LIST_TYPE = "ListType";
	public static final String TAG_HALL_BUILDING_NAME = "Building_Name";

	private MattaHallListResponse response = new MattaHallListResponse();
	private MattaHallList hallList;
	private MattaBanner banner;

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		if (BuildConfig.DEBUG)
			Log.d(AppConstants.FINDIT_DEBUG_TAG, "XML " + payload);
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), this);
		return response;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_HALL_ROOT)) {
			hallList = new MattaHallList();
		} else if (localName.equalsIgnoreCase(TAG_BANNER) && parent.equalsIgnoreCase("Results," + TAG_BANNERS)) {
			banner = new MattaBanner();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			response.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			response.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_ID) && parent.equalsIgnoreCase("Results," + TAG_BANNERS + "," + TAG_BANNER)) {
			banner.setId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_IMAGE_URL) && parent.equalsIgnoreCase("Results," + TAG_BANNERS + "," + TAG_BANNER)) {
			banner.setImage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_LANDING_URL) && parent.equalsIgnoreCase("Results," + TAG_BANNERS + "," + TAG_BANNER)) {
			banner.setLandingUrl(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_SCREEN_NAME) && parent.equalsIgnoreCase("Results," + TAG_BANNERS + "," + TAG_BANNER)) {
			banner.setScreenName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_CATEGORY_ID) && parent.equalsIgnoreCase("Results," + TAG_BANNERS + "," + TAG_BANNER)) {
			banner.setCategoryId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_ITEM_ID) && parent.equalsIgnoreCase("Results," + TAG_BANNERS + "," + TAG_BANNER)) {
			banner.setItemId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER) && parent.equalsIgnoreCase("Results," + TAG_BANNERS)) {
			if (banner != null && !StringUtil.isNullOrEmpty(banner.getId()) && !StringUtil.isNullOrEmpty(banner.getImage()))
				response.addBanner(banner);
		} else if (localName.equalsIgnoreCase(TAG_HALL_ID) && parent.equalsIgnoreCase("Results," + TAG_HALL_ROOT)) {
			hallList.setmHallId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_HALL_NAME) && parent.equalsIgnoreCase("Results," + TAG_HALL_ROOT)) {
			hallList.setmHallName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_HALL_BUILDING_NAME) && parent.equalsIgnoreCase("Results," + TAG_HALL_ROOT)) {
			hallList.setmHallBuildingName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_HALL_THEME) && parent.equalsIgnoreCase("Results," + TAG_HALL_ROOT)) {
			hallList.setmHallTheme(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_HALL_IMAGE) && parent.equalsIgnoreCase("Results," + TAG_HALL_ROOT)) {
			hallList.setmHallImage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_HALL_LIST_TYPE) && parent.equalsIgnoreCase("Results," + TAG_HALL_ROOT)) {
			hallList.setmHallListType(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_HALL_SOURCE) && parent.equalsIgnoreCase("Results," + TAG_HALL_ROOT)) {
			hallList.setmHallSource(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_HALL_ROOT)) {
			if (!StringUtil.isNullOrEmpty(hallList.getmHallId()))
				response.addMattaHallList(hallList);
		}
	}
}