package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import android.util.Log;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.ControlDetails;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;

public class TempletParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";

	public static final String TAG_TEMPLATE_ID = "Template_Id";
	public static final String TAG_TEMPLATE_NAME = "Template_Name";
	public static final String TAG_TEMPLATE_DESCRIPTION = "Template_Description";
	public static final String TAG_SELECTOR = "Selector";
	public static final String TAG_SEARCH_KEY = "Search_Key";
	public static final String TAG_CONTROLTYPE = "ControlType";
	public static final String TAG_CONTROLTYPEID = "ControlTypeId";
	public static final String TAG_DISPLAY_NAME = "Display_Name";
	public static final String TAG_FIELD_ID = "Field_Id";
	public static final String TAG_COLUMN_ID = "Column_Id";
	public static final String TAG_DISPLAY_ORDER = "Display_Order";
	public static final String TAG_REQUIRED = "Required";
	public static final String TAG_SEARCHABLE = "Searchable";
	public static final String TAG_VALUE = "Value";

	private ControlDetailResponse response = new ControlDetailResponse();
	private ControlDetails controlDetail;

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		Log.d("maxis", "XML " + payload);
		response = new ControlDetailResponse();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), TempletParser.this);
		return response;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_SELECTOR)) {
			controlDetail = new ControlDetails();
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
		} else if (localName.equalsIgnoreCase(TAG_TEMPLATE_ID)) {
			response.setTempletId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_TEMPLATE_NAME)) {
			response.setTempletName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_TEMPLATE_DESCRIPTION)) {
			response.setTempletDescription(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_SEARCH_KEY)) {
			controlDetail.setSearchKey(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CONTROLTYPE)) {
			controlDetail.setControlType(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CONTROLTYPEID)) {
			controlDetail.setControlTypeId(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_DISPLAY_NAME)) {
			controlDetail.setDisplayName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_FIELD_ID)) {
			controlDetail.setFieldId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_COLUMN_ID)) {
			controlDetail.setColumnId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_DISPLAY_ORDER)) {
			controlDetail.setDisplayOrder(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_REQUIRED)) {
			controlDetail.setRequired(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_SEARCHABLE)) {
			controlDetail.setSearchable(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_VALUE)) {
			controlDetail.addValues(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_SELECTOR)) {
			response.addControlDetail(controlDetail);
		}
	}
}
