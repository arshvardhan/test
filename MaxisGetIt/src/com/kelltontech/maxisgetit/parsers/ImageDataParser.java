package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.response.ImageDataResponse;

public class ImageDataParser extends AbstractSAXParser {
	public static final String TAG_IMAGENAME = "Image_name";
	
	private ImageDataResponse imResponse;

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		imResponse = new ImageDataResponse();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), ImageDataParser.this);
		Log.d("maxis", "XML " + payload);
		return imResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_IMAGENAME)) {
			Log.d("maxis", getNodeValue());
			imResponse.setImagename(getNodeValue());
		} 
	}

}
