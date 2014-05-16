package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import android.util.Log;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.dao.AttributeGroup;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.response.CompanyListResponse;

public class CompanyListParser extends AbstractSAXParser {
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	public static final String TOT_RECORDS = "Total_Records_Found";
	public static final String RECORD_PER_PAGE = "Records_Per_Page";
	public static final String PAGE_NUMBER = "Page_Number";
	public static final String DEAL_ROOT = "Deal_Detail";
	public static final String DEAL_ROOT2 = "Deal";
	public static final String COMPANY_ROOT = "Company";
	public static final String ID = "Id";
	public static final String TITLE = "Title";
	public static final String CITY = "City";
	public static final String STATE = "State";
	public static final String DESCRIPTION = "Description";
	public static final String ADDITIONAL_INFO = "Additional_Info";
	public static final String ICON_URL = "Icon_Url";
	public static final String DISTANCE = "Distance";
	public static final String RATING = "rating";
	public static final String VALUE = "Value";
	public static final String NAME = "Name";
	public static final String CATEGORY = "Category";
	public static final String LATITUDE = "Latitude";
	public static final String LONGITUDE = "Longitude";
	private static final String TAG_PIN = "PinCode";
	private static final String TAG_LOCALITY = "Locality";
	private static final String TAG_CONTACT_NO = "ContactNumber";
	private static final String TAG_GROUP_VAL = "Values";
	private static final String TAG_ATTR_NAME = "Label";
	private static final String TAG_ATTR_VAL = "Value";
	
	private static final String TAG_CATEGORY_ID = "L3Catid";
	private static final String TAG_END_DATE = "End_Date";
	private static final String TAG_VALID_IN = "ValidIn";
	
	private static final String TAG_VIDEO = "CVideo";
	
	private static final String TAG_SEARCH_DISTANCE = "Search_Distance";
	
	private static final String TAG_SOURCE = "Source";
	
	private static String TAG_DEAL_DETAIL_URL = "DealDetailsUrl";
	
	
	private CompanyListResponse clResponse = new CompanyListResponse();
	private CategoryRefine category;
	private CompanyDesc compDesc;
	private AttributeGroup attrGroup;

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		Log.d("maxis", "Company List XML : " + payload);
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), CompanyListParser.this);
		return clResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(COMPANY_ROOT) || localName.equalsIgnoreCase(DEAL_ROOT) || localName.equalsIgnoreCase(DEAL_ROOT2)) {
			compDesc = new CompanyDesc();
		} else if (localName.equalsIgnoreCase(CATEGORY)) {
			category = new CategoryRefine();
		} else if(localName.equalsIgnoreCase(TAG_GROUP_VAL)){
			attrGroup=new AttributeGroup();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			Log.d("maxis", getNodeValue());
			clResponse.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			Log.d("maxis", getNodeValue());
			clResponse.setServerMessage(getNodeValue());
		}
		if (localName.equalsIgnoreCase(COMPANY_ROOT) || localName.equalsIgnoreCase(DEAL_ROOT) || localName.equalsIgnoreCase(DEAL_ROOT2)) {
			compDesc.setCompId_catId();
			clResponse.addCompanyDescription(compDesc);
		} else if (localName.equalsIgnoreCase(RECORD_PER_PAGE)) {
			clResponse.setRecordsPerPage(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TOT_RECORDS)) {
			clResponse.setTotalrecordFound(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(PAGE_NUMBER)) {
			clResponse.setPageNumber(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(ID) && (parent.equalsIgnoreCase("Results,Company")||parent.equalsIgnoreCase("Results,"+DEAL_ROOT))) {
			compDesc.setCompId(getNodeValue());
		} else if (localName.equalsIgnoreCase(ICON_URL)) {
			compDesc.setIconUrl(getNodeValue());
		} else if (localName.equalsIgnoreCase(TITLE)) {
			compDesc.setTitle(getNodeValue());
		} else if (localName.equalsIgnoreCase(DESCRIPTION)) {
			compDesc.setDesc(getNodeValue());
		} else if (localName.equalsIgnoreCase(ADDITIONAL_INFO)) {
			compDesc.setAdditionalInfo(getNodeValue());
		} else if (localName.equalsIgnoreCase(DISTANCE)) {
			compDesc.setDistance(getNodeValue());
		} else if (localName.equalsIgnoreCase(RATING)) {
			compDesc.setRating(getFloat(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(CITY)) {
			compDesc.setCity(getNodeValue());
		} else if (localName.equalsIgnoreCase(STATE)) {
			compDesc.setState(getNodeValue());
		} /*else if (localName.equalsIgnoreCase(VALUE)) {
			compDesc.appendAttributes(getNodeValue());
		}*/else if (localName.equalsIgnoreCase(LATITUDE) ) {
			compDesc.setLatitude(getDouble(getNodeValue(),0));
		} else if (localName.equalsIgnoreCase(LONGITUDE)) {
			compDesc.setLongitude(getDouble(getNodeValue(),0));
		}else if (localName.equalsIgnoreCase(TAG_LOCALITY)) {
			compDesc.setLocality(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_PIN)) {
			compDesc.setPincode(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_CONTACT_NO)) {
			compDesc.setContactNo(getNodeValue());
		}
		else if (localName.equalsIgnoreCase(CATEGORY)) {
			if (category != null && !StringUtil.isNullOrEmpty(category.getCategoryId()) && !StringUtil.isNullOrEmpty(category.getCategoryTitle()))
				clResponse.addCategory(category);
		} else if (localName.equalsIgnoreCase(ID) && parent.equalsIgnoreCase("Results,Categories,Category")) {
			category.setCategoryId(getNodeValue());
		} else if (localName.equalsIgnoreCase(NAME) && parent.equalsIgnoreCase("Results,Categories,Category")) {
			category.setCategoryTitle(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ATTR_NAME)) {
			attrGroup.setLable(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ATTR_VAL)) {
			if(attrGroup != null)
				attrGroup.addValue(getNodeValue());
			else
				compDesc.appendAttributes(getNodeValue());
		} else if(localName.equalsIgnoreCase(TAG_GROUP_VAL)){
			compDesc.addAttrGroups(attrGroup);
		} else if (localName.equalsIgnoreCase(TAG_CATEGORY_ID)) {
			compDesc.setCat_id(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_END_DATE)) {
			compDesc.setEnd_date(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_VALID_IN)) {
			compDesc.setValid_in(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_VIDEO)) {
			compDesc.setVideo_url(getNodeValue());
		}else if (localName.equalsIgnoreCase(TAG_SEARCH_DISTANCE)) {
			clResponse.setSearch_distance(getNodeValue());
		}else if(localName.equalsIgnoreCase(TAG_SOURCE)){
			compDesc.setSource(getNodeValue());
		}else if(localName.equalsIgnoreCase(TAG_DEAL_DETAIL_URL)){
			compDesc.setDealDetailUrl(getNodeValue());
		}

	}

}
