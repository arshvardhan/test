/**
 *
 */
package com.kelltontech.framework.utils.stringutils;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vishal.gupta
 * 
 */
public class Text {
	public final static char[] phoneFix = { '-', '(', ')', ' ' };
	public static final String EMAIL_REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	public static final String COUNTRY_CODE_REGEX = "^([0-9]{0,3})$";
	public static final String PHONE_REGEX = "^(\\()?([0-9]{3})(\\)|-)?([0-9]{3})(-)?([0-9]{4}|[0-9]{4})$";
	public static final String INTERNATIONAL_PHONE_REGEX = "^([+]{1})([0-9]{1,3})(\\s)(\\()?([0-9]{3})(\\)|-)?([0-9]{3})(-)?([0-9]{4}|[0-9]{4})$";

	public static String cleanString(final String number, final char[] removeChars) {
		final char[] origChars = number.toCharArray();
		final Vector<Character> chars = new Vector<Character>();

		for (int x = 0; x < origChars.length; x++) {
			boolean isRemove = false;
			for (int i = 0; i < removeChars.length; i++) {

				if (origChars[x] == removeChars[i]) {
					isRemove = true;
				}
				if (isRemove) {
					break;
				}

			}
			if (isRemove == false) {
				chars.addElement(new Character(origChars[x]));
			}
		}

		final char[] finalChars = new char[chars.size()];
		for (int z = 0; z < chars.size(); z++) {
			final Character temp = chars.elementAt(z);
			finalChars[z] = temp.charValue();
		}

		return new String(finalChars).trim();

	}

	public static boolean contains(final String value, final String pattern) {
		// final StringMatch pattern_ = new StringMatch(pattern);
		// final int index = pattern_.indexOf(value);

		// if (index == -1)
		// {
		// return false;
		// }
		// return true;

		// TODO Still not tested...........
		Pattern tPattern = Pattern.compile(pattern);
		Matcher matcher = tPattern.matcher(value);
		return matcher.matches();

	}

	public static String digitsOnly(final String in) {
		char c;
		final StringBuffer out = new StringBuffer(in.length());
		for (int i = 0; i < in.length(); i++) {
			c = in.charAt(i);
			if (Character.isDigit(c)) {
				out.append(c);
			}
		}

		return out.toString();
	}

	public static String formatAsPhone(final String phone) {

		final char[] digits = phone.toCharArray();
		final StringBuffer buff = new StringBuffer();
		if (digits.length < 6) {
			return phone;
		}
		for (int x = 0; x < digits.length; x++) {
			if (x == 0) {
				buff.append("(");
			}
			if (x == 3) {
				buff.append(") ");
			}
			if (x == 6) {
				buff.append("-");
			}
			final char[] c = { digits[x] };
			buff.append(new String(c));
		}
		return buff.toString();

	}

	public static String formatAsPhone(String phone, final String ext) {
		if (Text.isNull(phone)) {
			phone = "";
		}
		// // May do to format phone number if required

		if (!Text.isNull(ext)) {

			return phone.concat(" x" + ext);
		}

		return phone;
	}

	public static String getCountryCodeOnly(final String number) {
		if (Text.isNull(number)) {
			return number;
		}

		if (number.startsWith("+")) {
			return number.substring(1, number.length());
		}

		return "";
	}

	public static String[] getCountryCodeSeparated(final String number) {
		final String[] temp = new String[2];

		temp[1] = number;
		// // If has contryCode in starting
		if (number.startsWith("+")) {
			final int index = number.indexOf(' ');

			if (index >= 0) {
				// // Start country code from 1 position, skip '+'
				temp[0] = number.substring(1, index);
				temp[1] = number.substring(index + 1, number.length());

			} else {
				// / If we get '+' sign at starting than it is clear it is
				// International Number
				// / But we don;t get any space to separate country code and
				// number
				// / We will treat whole number as a local number

				temp[1] = number.substring(1, number.length());

			}

		}

		return temp;

	}

	public static String getInternationalNumber(String countryCode, String number) {
		countryCode = countryCode.trim();
		number = number.trim();

		if (!Text.isNull(countryCode)) {
			if (!countryCode.startsWith("+")) {
				countryCode = "+" + countryCode;
			}

			countryCode = countryCode.concat(" ");
			countryCode = countryCode.concat(number);
			return countryCode;
		}

		return number;
	}

	public static int getPasswordStrength(final String password) {
		int score = 0;

		// if password bigger than 6 give 1 point
		if (password.length() > 6) {
			score++;
		}

		// if password has both lower and uppercase characters give 1 point
		if (Text.matches("[a-z]", password) && Text.matches("[A-Z]", password)) {
			score++;
		}

		// if password has at least one number give 1 point
		if (Text.matches("\\d+", password)) {
			score++;
		}

		// if password has at least one special caracther give 1 point
		// if ( matches("[.!@#$%^&*?_-~()]",password)) score++;
		if (Text.matches("[.!@#$%^&*?_~()]", password)) {
			score++;
		}

		// if password bigger than 12 give another 1 point
		if (password.length() > 12) {
			score++;
		}

		return score;
	}

	public static String getPasswordStrengthString(final String password) {
		final String[] desc = new String[6];

		desc[0] = "Very Weak";

		desc[1] = "Weak";

		desc[2] = "Moderate";

		desc[3] = "Strong";

		desc[4] = "Very Strong";

		desc[5] = "Very Strong";

		return desc[Text.getPasswordStrength(password)];

	}

	public static String getStrippedNumber(final String number) {
		final StringBuffer getStrippedNumberStringBuffer = new StringBuffer(20);
		// Crazy code for speed optimization
		getStrippedNumberStringBuffer.setLength(0); // Clear
		if (number != null) {
			for (int i = number.length() - 1; i >= 0; i--) {
				final char oneChar = number.charAt(i);
				// Exactly two comparisons
				if ((oneChar >= '0') && (oneChar <= '9')) {
					getStrippedNumberStringBuffer.append(oneChar);
				}
			}
			getStrippedNumberStringBuffer.reverse();
		}

		return getStrippedNumberStringBuffer.toString();
	}

	public static boolean isNull(final String text) {

		return (text == null) || (text.trim().length() == 0) || text.equalsIgnoreCase("null");
	}

	public static boolean isValidCountryCode(String countryCode) {
		if (countryCode == null) {
			return false;
		}

		countryCode = countryCode.trim();
		try {
			if (countryCode.length() > 3) {
				return false;
			} else {
				final String pattern = COUNTRY_CODE_REGEX;
				return Text.matches(pattern, countryCode);
			}

		} catch (final Exception ex) {
			return false;
		}
	}

	public static boolean isValidEmailFormat(final String email) {
		try {

			if (email.trim().length() == 0) {
				return false;
			} else {
				// String pattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$";
				final String pattern = EMAIL_REGEX;
				if (!Text.matches(pattern, email.toLowerCase().trim())) {
					return false;
				}
			}
			return true;
		} catch (final Exception ex) {
			return false;
		}
	}

	public static boolean isValidPhoneFormat(String phone) {
		if (phone == null) {
			return false;
		}

		phone = phone.trim();

		try {
			if (phone.length() < 10) {
				return false;
			} else {

				// remove first leading "1"
				if (phone.startsWith("1") && (phone.length() == 11)) {
					phone = phone.substring(1);
				}

				if (phone.length() > 10) {
					return false;
				}

				// check area code
				final String areaCode = phone.substring(0, 3);
				if ((areaCode == "877") || (areaCode == "888") || (areaCode == "866") || (areaCode == "800")) {
					return false;
				}

				// check if xxx-555-xxxx
				final String exCode = phone.substring(3, 3);
				if (exCode == "555") {
					return false;
				}

				// String pattern = "[0-9]+";
				final String pattern = PHONE_REGEX;
				return Text.matches(pattern, phone);
			}
		} catch (final Exception e) {
			return false;
		}
	}

	public static boolean matches(final String regexp, final String s) {

		// final RE r = new RE(regexp);
		// return r.match(s);
		// TODO Still not tested...........
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}

	public static <T> void merge(final Vector<T> vct1, final Vector<T> vct2) {

		if ((vct1 != null) && (vct2 != null)) {
			final int size = vct2.size();

			for (int i = 0; i < size; i++) {
				vct1.addElement(vct2.elementAt(i));
			}
		}
	}

	public static String removeAllOccurances(final String s, final char c) {
		String r = "";

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != c) {
				r += s.charAt(i);
			}
		}

		return r;
	}

	public static String replaceAll(final String input, final String find, final String replacement) {

		String output = "";
		String temp = input;

		while (true) {

			final int indexOf = temp.indexOf(find);

			if (indexOf < 0) {

				break;
			} else if (indexOf > 0) {

				output += temp.substring(0, indexOf);
			}

			output += replacement;
			temp = temp.substring(indexOf + find.length());
		}

		return output + temp;
	}

	public static String truncateForDisplay(final String text, final int maxSize) {
		try {
			String s = text.substring(0, maxSize);
			s = s + "...";
			return s;
		} catch (final StringIndexOutOfBoundsException e) {
			return text;
		} catch (final NullPointerException e) {
			return text;
		}
	}

	public static String getFirstLetterUpperCaseStr(String text) {
		if (text == null)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append(text.substring(0, 1).toUpperCase());
		sb.append(text.substring(1).toLowerCase());
		return sb.toString();
	}

	public static boolean getBoolean(String value) throws IllegalArgumentException {
		value = value.trim();

		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"))
			return true;
		else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("0"))
			return false;
		else
			throw new IllegalArgumentException(value + " is not a valid boolean value");

	}

	public static boolean getBoolean(String value, boolean optValue) throws IllegalArgumentException {
		value = value.trim();
		if (value == null || value.length() < 0) {
			return optValue;
		}
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"))
			return true;
		else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("0"))
			return false;
		else
			return optValue;
		// throw new IllegalArgumentException(value +
		// " is not a valid boolean value");

	}

	public static long getLong(String value) throws IllegalArgumentException {
		value = value.trim();

		try {
			long lg = Long.parseLong(value);
			return lg;
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(value + " is not a valid long value");
		}

	}

	public static long getLong(String value, long optValue) throws IllegalArgumentException {
		value = value.trim();
		if (value == null || value.length() < 0) {
			return optValue;
		}
		try {
			long lg = Long.parseLong(value);
			return lg;
		} catch (NumberFormatException ex) {
			return optValue;
			// throw new IllegalArgumentException(value +
			// " is not a valid long value");
		}

	}

	public static float getFloat(String value) throws IllegalArgumentException {
		value = value.trim();

		try {
			float lg = Float.parseFloat(value);
			return lg;
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(value + " is not a valid float value");
		}

	}

	public static float getFloat(String value, float optValue) throws IllegalArgumentException {
		value = value.trim();
		if (value == null || value.length() < 0) {
			return optValue;
		}
		try {
			float lg = Float.parseFloat(value);
			return lg;
		} catch (NumberFormatException ex) {
			return optValue;
			// throw new IllegalArgumentException(value +
			// " is not a valid float value");
		}

	}

	public static double getDouble(String value) throws IllegalArgumentException {
		value = value.trim();

		try {
			double lg = Double.parseDouble(value);
			return lg;
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(value + " is not a valid double value");
		}

	}

	public static double getDouble(String value, double optValue) throws IllegalArgumentException {

		value = value.trim();
		if (value == null || value.length() < 0) {
			return optValue;
		}
		try {
			double lg = Double.parseDouble(value);
			return lg;
		} catch (NumberFormatException ex) {
			return optValue;
			// throw new IllegalArgumentException(value +
			// " is not a valid double value");
		}

	}

	public static int getInt(String value) throws IllegalArgumentException {
		value = value.trim();

		try {
			int it = Integer.parseInt(value);
			return it;
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(value + " is not a valid int value");
		}

	}

	public static int getInt(String value, int optValue) throws IllegalArgumentException {
		value = value.trim();
		if (value == null || value.length() < 0) {
			return optValue;
		}
		try {
			int it = Integer.parseInt(value);
			return it;
		} catch (NumberFormatException ex) {
			return optValue;
			// throw new IllegalArgumentException(value +
			// " is not a valid int value");
		}

	}

}
