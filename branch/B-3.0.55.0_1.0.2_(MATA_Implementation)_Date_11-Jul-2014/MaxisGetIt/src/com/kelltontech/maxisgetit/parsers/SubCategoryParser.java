package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.dao.Banner;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;

public class SubCategoryParser extends AbstractSAXParser {
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
	public static final String TAG_CAT_ROOT = "Category";
	public static final String TAG_CAT_ID = "Category_Id";
	public static final String TAG_CAT_NAME = "Category_Name";
	public static final String TAG_CAT_ICON_URL = "Icon_Url";
	public static final String TAG_CAT_THUMB_URL = "Thumb_Url";
	public static final String TAG_GROUP_ACTION_TYPE = "Action_Type";
	public static final String TAG_GROUP_TYPE = "Type";
	private SubCategoryResponse response = new SubCategoryResponse();
	private SubCategory category;
	private Banner banner;

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		Log.d("maxis", "XML " + payload);
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), this);
		return response;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_CAT_ROOT)) {
			category = new SubCategory();
		} else if (localName.equalsIgnoreCase(TAG_BANNER) && parent.equalsIgnoreCase("Results,"+TAG_BANNERS)) {
			banner = new Banner();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			response.setErrorCode(getInt(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			response.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_ID) && parent.equalsIgnoreCase("Results,"+TAG_BANNERS+","+TAG_BANNER)) {
			banner.setId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_IMAGE_URL) && parent.equalsIgnoreCase("Results,"+TAG_BANNERS+","+TAG_BANNER)) {
			banner.setImage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_LANDING_URL) && parent.equalsIgnoreCase("Results,"+TAG_BANNERS+","+TAG_BANNER)) {
			banner.setLandingUrl(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_SCREEN_NAME) && parent.equalsIgnoreCase("Results,"+TAG_BANNERS+","+TAG_BANNER)) {
			banner.setScreenName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_CATEGORY_ID) && parent.equalsIgnoreCase("Results,"+TAG_BANNERS+","+TAG_BANNER)) {
			banner.setCategoryId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER_ITEM_ID) && parent.equalsIgnoreCase("Results,"+TAG_BANNERS+","+TAG_BANNER)) {
			banner.setItemId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_BANNER) && parent.equalsIgnoreCase("Results,"+TAG_BANNERS)) {
			if (banner != null && !StringUtil.isNullOrEmpty(banner.getId()) && !StringUtil.isNullOrEmpty(banner.getImage()))
				response.addBanner(banner);
		} else if (localName.equalsIgnoreCase(TAG_CAT_ID)) {
			Log.d("maxis", getNodeValue());
			category.setCategoryId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CAT_NAME)) {
			Log.d("maxis", getNodeValue());
			category.setCategoryTitle(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CAT_ICON_URL)) {
			Log.d("maxis", getNodeValue());
			category.setIconUrl(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CAT_THUMB_URL)) {
			category.setThumbUrl(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_GROUP_ACTION_TYPE)) {
			category.setmGroupActionType(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_GROUP_TYPE)) {
			category.setMgroupType(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_CAT_ROOT)) {
			response.addSubCategory(category);
		}

	}

}
