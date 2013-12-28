package com.kelltontech.maxisgetit.parsers;

import java.io.ByteArrayInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.parser.AbstractSAXParser;
import com.kelltontech.maxisgetit.dao.Classified_Base;
import com.kelltontech.maxisgetit.response.ClassifiedListResponse;

public class ClassifiedListParser extends AbstractSAXParser {
	private ClassifiedListResponse classifiedListResponse = new ClassifiedListResponse();
	private Classified_Base classified;
	private Classified_Base latestClassified;
	public static final String TAG_ERROR_CODE = "Error_Code";
	public static final String TAG_ERROR_MESSAGE = "Error_Message";
	private static final String TAG_ID = "ID";
	private static final String TAG_TITLE = "Title";
	private static final String TAG_DESC = "Description";
	private static final String TAG_CLASSIFIED = "Classified";
	private static final String TAG_AD_STATUS = "AdStatus";
	private static final String TAG_AD_STATUS1 = "IsActive";
	private static final String TAG_IS_PAID = "IsPaid";
	private static final String TAG_CATEGORY = "Category_Name";
	private static final String TAG_VALIDITY = "Validity";

	@Override
	public IModel parse(String payload) throws Exception {
		init();
		saxParser.parse(new ByteArrayInputStream(payload.getBytes()), ClassifiedListParser.this);

//		String str = "<?xml version=\"1.0\"?><Results><Error_Code>0</Error_Code><Total_Records_Found>7</Total_Records_Found><Group><Classified><Id><![CDATA[2]]></Id> <Title><![CDATA[arvind]]></Title><Description><![CDATA[Testing ji]]></Description><Category_Name>Kopitiam</Category_Name> <IsActive>1</IsActive><Validity></Validity> </Classified><Total_Classified>1</Total_Classified></Group><Group><Classified> <Id><![CDATA[3]]></Id> <Title><![CDATA[arvind]]></Title> <Description><![CDATA[Testing ji]]></Description> <Category_Name>Mamak</Category_Name> <IsActive>1</IsActive> <Validity></Validity></Classified><Classified> <Id><![CDATA[4]]></Id> <Title><![CDATA[Art]]></Title> <Description><![CDATA[Testinf njksb diojdsabjkbnc ghcbn ghc ghcd gcd gh]]></Description> <Category_Name>Mamak</Category_Name> <IsActive>1</IsActive><Validity></Validity> </Classified><Classified> <Id><![CDATA[5]]></Id><Title><![CDATA[Tet]]></Title> <Description><![CDATA[Tet]]></Description><Category_Name>Mamak</Category_Name><IsActive>1</IsActive><Validity></Validity> </Classified><Classified> <Id><![CDATA[6]]></Id>	 <Title><![CDATA[arvind]]></Title> <Description><![CDATA[lojjjkldkkndfsbjk]]></Description> <Category_Name>Mamak</Category_Name> <IsActive>1</IsActive><Validity></Validity> </Classified><Classified> <Id><![CDATA[7]]></Id> <Title><![CDATA[arvind]]></Title> <Description><![CDATA[lojjjkldkkndfsbjk]]></Description>	 <Category_Name>Mamak</Category_Name>	 <IsActive>1</IsActive>	<Validity></Validity> </Classified><Classified>	 <Id><![CDATA[8]]></Id>	 <Title><![CDATA[tetst]]></Title>	 <Description><![CDATA[test description]]></Description> <Category_Name>Mamak</Category_Name> <IsActive>1</IsActive> </Classified><Total_Classified>6</Total_Classified></Group>	 </Results>";
//		saxParser.parse(new ByteArrayInputStream(str.getBytes()), ClassifiedListParser.this);
//		
		classifiedListResponse.setLatestAd(latestClassified);
		return classifiedListResponse;
	}

	@Override
	public void onStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, IllegalArgumentException {
		if (localName.equalsIgnoreCase(TAG_CLASSIFIED)) {
			classified = new Classified_Base();
		}
	}

	@Override
	public void onEndElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_ERROR_CODE)) {
			classifiedListResponse.setErrorCode(getInt(getNodeValue(), 0));
		} else if (localName.equalsIgnoreCase(TAG_ERROR_MESSAGE)) {
			classifiedListResponse.setServerMessage(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_ID)) {
			classified.setId(getNodeValue());
		}
		if (localName.equalsIgnoreCase(TAG_TITLE)) {
			classified.setTitle(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_DESC)) {
			classified.setDesc(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_AD_STATUS) || localName.equalsIgnoreCase(TAG_AD_STATUS1)) {
			classified.setAdStatus(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_CLASSIFIED)) {
			classifiedListResponse.addClassified(classified);
			if (latestClassified == null) {
				latestClassified = classified;
			} else {
				if (getLong(latestClassified.getValidity(), 0) < getLong(classified.getValidity(), 0)) {
					latestClassified = classified;
				}
			}
		} else if (localName.equalsIgnoreCase(TAG_CATEGORY)) {
			classified.setCategory(getNodeValue());
		} else if (localName.equalsIgnoreCase(TAG_VALIDITY)) {
			classified.setValidity(getLong(getNodeValue(), 0) + "");
		}else if (localName.equalsIgnoreCase(TAG_IS_PAID)) {
			classified.setPaid((getInt(getNodeValue(), 0))==1?true:false);
		}
	}

}
