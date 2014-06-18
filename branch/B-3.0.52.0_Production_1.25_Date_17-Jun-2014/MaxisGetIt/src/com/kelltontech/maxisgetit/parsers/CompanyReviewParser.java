package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.CompanyReview;
import com.kelltontech.maxisgetit.response.CompanyReviewsListResponse;

public class CompanyReviewParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	private static final String TAG_REVIEW = "Review";
	private static final String TAG_TOTAL_RECORD = "Total_Record";
	private static final String TAG_USER_NAME = "User_Name";
	private static final String TAG_USER_RATING = "Rating";
	private static final String TAG_REVIEW_DESC = "Review_Desc";
	private static final String TAG_CREATED = "Created";
	
	CompanyReviewsListResponse response;
	CompanyReview compReview;
	
	@Override
	public IModel parse(String payload) throws Exception {
		init();
		response = new CompanyReviewsListResponse();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), CompanyReviewParser.this);
		return response;
	}

	
	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			response.setErrorCode(getInt(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			response.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_USER_RATING)) {
			Log.d("maxis", getNodeValue());
			compReview.setRating(getFloat(getNodeValue()));
		} else if (localName.equalsIgnoreCase(TAG_USER_NAME)) {
			Log.d("maxis", getNodeValue());
			compReview.setUserName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_TOTAL_RECORD)) {
			Log.d("maxis", getNodeValue());
			response.setTotalRecords(getInt(getNodeValue()));
		} else if (localName.equalsIgnoreCase(TAG_REVIEW_DESC)) {
			Log.d("maxis", getNodeValue());
			compReview.setReviewDesc(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CREATED)) {
			Log.d("maxis", getNodeValue());
			compReview.setReportedOn(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_REVIEW)) {
			Log.d("maxis", getNodeValue());
			response.getCompanyReviewList().add(compReview);
		}
	}


	@Override
	public void onStartElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException,
			IllegalArgumentException {
		 if (localName.equalsIgnoreCase(TAG_REVIEW)) {
				Log.d("maxis", getNodeValue());
				compReview = new CompanyReview();
			}
		
	}
}
