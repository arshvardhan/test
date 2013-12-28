package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.response.RootCategoryResponse;

public class RootCategoryParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TAG_CAT_ROOT = "Category";
	public static final String TAG_CAT_ID = "Category_Id";
	public static final String TAG_CAT_NAME = "Category_Name";
	public static final String TAG_CAT_ICON_URL = "Icon_Url";
	public static final String TAG_CAT_THUMB_URL = "Thumb_Url";
	public static final String TAG_GROUP_ACTION_TYPE = "Action_Type";
	public static final String TAG_GROUP_TYPE = "Type";
	private RootCategoryResponse response = new RootCategoryResponse();
	private CategoryGroup category;

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
			category = new CategoryGroup();
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
		}else if (localName.equalsIgnoreCase(TAG_GROUP_ACTION_TYPE)) {
			category.setmGroupActionType(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_GROUP_TYPE)) {
			category.setMgroupType(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CAT_ROOT)) {
			response.addCategory(category);
		}

	}

}
