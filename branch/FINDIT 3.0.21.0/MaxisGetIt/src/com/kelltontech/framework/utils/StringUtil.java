package com.kelltontech.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class useful when dealing with string objects. This class is a
 * collection of static functions it is not allowed to create instances of this
 * class
 */
public abstract class StringUtil {

	public static final Pattern MOBILE_NUMBER_PATTERN = Pattern.compile("^((\\+60?)|0|)?[0-9]{10}$");  //”^[0-9]{10}”
	
	/**
	 * @param pStr
	 *            String object to be tested.
	 * @returns true if the given string is null or empty or contains spaces
	 *          only.
	 */
	public static boolean isNullOrEmpty(final String pStr) {
		return pStr == null || pStr.trim().length() == 0;
	}

	/**
	 * Method to generate Rupee/Price text format of type Rs.300 x 3 = Rs.600
	 * 
	 * @param pStr
	 * @return
	 */
	public static String formatedPrice(int ticketCost, int seatCount) {
		double price = ticketCost * seatCount;
		price = Math.round(price * 100.0) / 100.0;
		return (ticketCost + " x " + seatCount + " = Rs." + price);
	}

	public static boolean containsIgnoreCase(String pText, String pPattern) {
		try {
			Pattern pattern = Pattern.compile(pPattern, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(pText);
			return matcher.find();
		} catch (Exception e) {
		}
		return false;
	}

	public static String decimalUpTo2Places(double originalValue) {
		return String.format("%.2f", originalValue);
	}
	
	/**
	 * 
	 * @param mobileNumber
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber) {
		return MOBILE_NUMBER_PATTERN.matcher(mobileNumber).matches();
	}
	
	
	/**
	 * This method checks and ensure http/https protocol in URL
	 * @param url
	 * @return formattedUrl
	 */
	public static String getFormattedURL(String url) {
		if( url.indexOf("http://") == 0 || url.indexOf("https://") == 0 ) {
			return url;
		} else if( url.indexOf("://") == 0 ) {
			return "http" + url;
		} else if( url.indexOf("//") == 0 ) {
			return "http:" + url;
		} else if( url.indexOf("/") == 0 ) {
			return "http:/" + url;
		} else {
			return "http://" + url;
		}
	}
}
