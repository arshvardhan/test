//package com.kelltontech.maxisgetit.parsers;
//
//import java.io.ByteArrayInputStream;
//
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//
//import android.util.Log;
//
//import com.kelltontech.framework.model.IModel;
//import com.kelltontech.framework.parser.AbstractSAXParser;
//import com.kelltontech.maxisgetit.dao.CategoryRefine;
//import com.kelltontech.maxisgetit.response.RefineCategoryResponse;
//
//public class RefineCategoryParser extends AbstractSAXParser {
//	public static final String TAG_ERROR_CODE = "Error_Code";
//	public static final String TAG_ERROR_MESSAGE = "Error_Message";
//	public static final String TAG_CAT_ROOT = "Category";
//	public static final String TAG_CAT_ID = "Id";
//	public static final String TAG_CAT_NAME = "Name";
//	private CategoryRefine category;
//	private RefineCategoryResponse response = new RefineCategoryResponse();
//
//	@Override
//	public IModel parse(String payload) throws Exception {
//		init();
//		Log.d("maxis", "XML " + payload);
//		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), this);
//		return response;
//	}
//
//	@Override
//	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
//		if (localName.equalsIgnoreCase(TAG_CAT_ROOT)) {
//			category = new CategoryRefine();
//		}
//	}
//
//	@Override
//	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
//		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
//			Log.d("maxis", getNodeValue());
//			response.setErrorCode(getInt(getNodeValue(),0));
//		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
//			Log.d("maxis", getNodeValue());
//			response.setServerMessage(getNodeValue());
//		} else if (localName.equalsIgnoreCase(TAG_CAT_ID)) {
//			Log.d("maxis", getNodeValue());
//			category.setCategoryId(getNodeValue());
//		} else if (localName.equalsIgnoreCase(TAG_CAT_NAME)) {
//			Log.d("maxis", getNodeValue());
//			category.setCategoryTitle(getNodeValue());
//		} else if (localName.equalsIgnoreCase(TAG_CAT_ROOT)) {
//			response.addCategory(category);
//		}
//
//	}
//
//}
