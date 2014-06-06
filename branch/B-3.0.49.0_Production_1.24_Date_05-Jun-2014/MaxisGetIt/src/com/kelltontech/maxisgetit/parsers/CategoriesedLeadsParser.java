package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.CategoriesedLeads;
import com.kelltontech.maxisgetit.dao.LeadDao;
import com.kelltontech.maxisgetit.response.CategoriesedLeadsResponse;

public class CategoriesedLeadsParser extends AbstractSAXParser {
	private CategoriesedLeadsResponse catLeadsResponse = new CategoriesedLeadsResponse();
	private CategoriesedLeads categoryLeads;
	private ArrayList<LeadDao> leadList;
	private LeadDao lead;

	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	private static final String TAG_NAME = "Name";
	private static final String TAG_CALLER_ID = "Caller_Id";
	private static final String TAG_START_TIME = "StartTime";
	private static final String TAG_CATEGORY = "Category";
	private static final String TAG_LEAD = "Lead";
	private static final String TAG_COMPANY_NAME = "Company_Name";

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), CategoriesedLeadsParser.this);
		return catLeadsResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_CATEGORY)) {
			categoryLeads = new CategoriesedLeads();
			leadList = new ArrayList<LeadDao>();
		} else if (localName.equalsIgnoreCase(TAG_LEAD)) {
			lead = new LeadDao();
		}

	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			catLeadsResponse.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			catLeadsResponse.setServerMessage(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_CALLER_ID)) {
			lead.setCallerId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_START_TIME)) {
			lead.setStartTime(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_LEAD)) {
			leadList.add(lead);
		} else if (localName.equalsIgnoreCase(TAG_NAME)) {
			categoryLeads.setCategoryName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_COMPANY_NAME)) {
			catLeadsResponse.setCompanyName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CATEGORY)) {
			categoryLeads.setLeadList(leadList);
			catLeadsResponse.addCatLeads(categoryLeads);
		}
	}

}
