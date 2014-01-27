package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.CategoryWithCharge;
import com.kelltontech.maxisgetit.dao.CompanyCategory;
import com.kelltontech.maxisgetit.dao.MyDeal;
import com.kelltontech.maxisgetit.dao.MyDealsList;
import com.kelltontech.maxisgetit.response.DealsListResponse;

public class DealsListParser extends AbstractSAXParser {
	private DealsListResponse myDealsResponse = new DealsListResponse();
	private MyDeal latestDeal;
	private MyDeal myDeal;
	private MyDealsList dealGroup;
	private ArrayList<MyDeal> dealArray;
	private CompanyCategory companyCategory;
	private CategoryWithCharge catCharge;
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	private static final String TAG_ID = "ID";
	private static final String TAG_TITLE = "Title";
	private static final String TAG_DESC = "Description";
	private static final String TAG_DEAL = "Deal";
	private static final String TAG_DEAL_TYPE = "DealType";
	private static final String TAG_CATEGORY_NAME = "Category_Name";
	private static final String TAG_VALIDITY = "Validity";
	private static final String TAG_DEAL_STATUS = "DealStatus";
	private static final String TAG_GROUP = "GROUP";
	private static final String TAG_NAME = "Name";
	private static final String TAG_DEAL_CHARGE = "DealCharge";
	private static final String TAG_COMPANY = "Company";
	private static final String TAG_CATEGORY = "Category";
	@Override
	public IModel parse(String payload) throws Exception {
		init();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), DealsListParser.this);
		myDealsResponse.setLatestDeal(latestDeal);
		Log.d("maxis", "XML " + payload);
		return myDealsResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_DEAL)) {
			myDeal = new MyDeal();
		} else if (localName.equalsIgnoreCase(TAG_GROUP)) {
			dealArray = new ArrayList<MyDeal>();
			dealGroup = new MyDealsList();
		}else if (localName.equalsIgnoreCase(TAG_COMPANY)) {
			companyCategory = new CompanyCategory();
		} else if (localName.equalsIgnoreCase(TAG_CATEGORY)) {
			catCharge = new CategoryWithCharge();
		} 
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			myDealsResponse.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			myDealsResponse.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ID) && parent.endsWith(TAG_DEAL)) {
			myDeal.setId(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_TITLE) && parent.endsWith(TAG_DEAL)) {
			myDeal.setTitle(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_DESC) && parent.endsWith(TAG_DEAL)) {
			myDeal.setDesc(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_DEAL_TYPE) && parent.endsWith(TAG_DEAL)) {
			myDeal.setDealType(getInt(getNodeValue(), 0) == 1 ? true : false);
		} else if (localName.equalsIgnoreCase(TAG_CATEGORY_NAME) && parent.endsWith(TAG_DEAL)) {
			myDeal.setCategory(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_DEAL_STATUS) && parent.endsWith(TAG_DEAL)) {
			myDeal.setDealStatus(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_VALIDITY) && parent.endsWith(TAG_DEAL)) {
			myDeal.setValidity(getLong(getNodeValue(),0)+"");
		} else if (localName.equalsIgnoreCase(TAG_DEAL)) {
//			dealGroup.addDeal(myDeal);
			dealArray.add(myDeal);
			if(latestDeal==null){
				latestDeal=myDeal;
			}else{
//				if(getLong(latestDeal.getValidity(),0)<getLong(myDeal.getValidity(),0)){
//					latestDeal=myDeal;
//				}
			}
		} else if (localName.equalsIgnoreCase(TAG_GROUP)) {
			dealGroup.addDealList(dealArray);
			myDealsResponse.addDealGroupList(dealGroup);
		}else if (localName.equalsIgnoreCase(TAG_ID) && parent.endsWith(TAG_COMPANY)) {
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
			myDealsResponse.addCompCategoryList(companyCategory);
		}
	}

}
