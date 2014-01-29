/**
 * 
 */
package com.kelltontech.maxisgetit.utils;

import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.model.listModel.Address;

/**
 * Utility class for project specific methods.
 */
public class ProjectUtils {

	private static final String	SEPARATOR_COMMA_SPACE	= ", ";
	private static final String	DELIMITER_FULL_STOP		= ".";

	/**
	 * @param pAddress
	 * @return
	 */
	public static String getAddressText(Address pAddress) {
		StringBuffer strBfr = new StringBuffer();
		if (!StringUtil.isNullOrEmpty(pAddress.getBuilding())) {
			strBfr.append(pAddress.getBuilding().trim());
		}
		if (!StringUtil.isNullOrEmpty(pAddress.getLandmark())) {
			if (strBfr.length() > 0) {
				strBfr.append(SEPARATOR_COMMA_SPACE);
			}
			strBfr.append(pAddress.getLandmark().trim());
		}
		if (!StringUtil.isNullOrEmpty(pAddress.getStreet())) {
			if (strBfr.length() > 0) {
				strBfr.append(SEPARATOR_COMMA_SPACE);
			}
			strBfr.append(pAddress.getStreet().trim());
		}
		if (!StringUtil.isNullOrEmpty(pAddress.getSubLocality())) {
			if (strBfr.length() > 0) {
				strBfr.append(SEPARATOR_COMMA_SPACE);
			}
			strBfr.append(pAddress.getSubLocality().trim());
		}
		if (!StringUtil.isNullOrEmpty(pAddress.getLocality())) {
			if (strBfr.length() > 0) {
				strBfr.append(SEPARATOR_COMMA_SPACE);
			}
			strBfr.append(pAddress.getLocality().trim());
		}
		if (strBfr.length() > 0) {
			strBfr.append(DELIMITER_FULL_STOP);
		}
		return strBfr.toString();
	}
}
