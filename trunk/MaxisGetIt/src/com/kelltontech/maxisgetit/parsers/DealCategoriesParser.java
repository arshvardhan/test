package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.CategoryWithCharge;
import com.kelltontech.maxisgetit.dao.CompanyCategory;
import com.kelltontech.maxisgetit.response.DealCategoriesResponse;

public class DealCategoriesParser extends AbstractSAXParser {
	private DealCategoriesResponse dealCatResponse = new DealCategoriesResponse();
	private CompanyCategory companyCategory;
	private CategoryWithCharge catCharge;
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	private static final String TAG_ID = "ID";
	private static final String TAG_NAME = "Name";
	private static final String TAG_DEAL_CHARGE = "DealCharge";
	private static final String TAG_COMPANY = "Company";
	private static final String TAG_CATEGORY = "Category";
	

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), DealCategoriesParser.this);
		return dealCatResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_COMPANY)) {
			companyCategory = new CompanyCategory();
		} else if (localName.equalsIgnoreCase(TAG_CATEGORY)) {
			catCharge = new CategoryWithCharge();
		} 
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			dealCatResponse.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			dealCatResponse.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ID) && parent.endsWith(TAG_COMPANY)) {
			System.out.println(parent);
			companyCategory.setCompanyId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_NAME) && parent.endsWith(TAG_COMPANY)) {
			companyCategory.setCompanyName(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ID) && parent.endsWith(TAG_CATEGORY)) {
			catCharge.setCategoryId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_NAME) && parent.endsWith(TAG_CATEGORY)) {
			catCharge.setCategoryTitle(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_DEAL_CHARGE) && parent.endsWith(TAG_CATEGORY)) {
			catCharge.setCharge(getFloat(getNodeValue(), 0.0f));
		} else if (localName.equalsIgnoreCase(TAG_CATEGORY)) {
			companyCategory.addCategoryCharge(catCharge);
		} else if (localName.equalsIgnoreCase(TAG_COMPANY)) {
			dealCatResponse.addCompCategoryList(companyCategory);
		}
		
	}

}
