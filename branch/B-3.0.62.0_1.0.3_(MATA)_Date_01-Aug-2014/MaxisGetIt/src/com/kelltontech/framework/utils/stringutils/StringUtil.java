package com.kelltontech.framework.utils.stringutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.kelltontech.framework.C;

/**
 * General static utilities.
 * 
 * @since 1.0
 */
public class StringUtil {

	private static String B16[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
	private static String B64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	private static byte[] encodeData;
	static {
		encodeData = new byte[64];
		for (int i = 0; i < 64; i++) {
			byte c = (byte) B64.charAt(i);
			encodeData[i] = c;
		}
	}

	/**
	 *
	 */
	public static boolean isEmpty(String v) {
		return (null == v) || (v.length() == 0) || (v.trim().length() == 0);
	}

	/**
	 * yeah, won't work for secondly events
	 */
	public static boolean isGMT(String iCalDate) {
		if (iCalDate == null) {
			return false;
		}
		return iCalDate.indexOf("00Z") > 0;
	}

	/**
	 *
	 */
	public static long convertIcalDate(String iCalString) {
		long rc = 0;
		if (iCalString != null) {
			String value = iCalString.trim();
			int pos = value.lastIndexOf(':');
			if (pos > 0) {
				value = value.substring(pos + 1);
			}
			pos = value.indexOf('T');
			if (pos > 0) {
				String b = value.substring(0, pos);
				value = b.concat(value.substring(pos + 1));
			}
			pos = value.indexOf('Z');
			if (pos > 0) {
				value = value.substring(0, pos);
			}

			int year = StringUtil.getInt(value.substring(0, 4));
			int month = StringUtil.getInt(value.substring(4, 6));
			int day = StringUtil.getInt(value.substring(6, 8));
			int hour = 0;
			int min = 0;
			int sec = 0;
			if (value.length() > 8) {
				hour = StringUtil.getInt(value.substring(8, 10));
				min = StringUtil.getInt(value.substring(10, 12));
				sec = StringUtil.getInt(value.substring(12, 14));
			}

			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month - 1);
			c.set(Calendar.DAY_OF_MONTH, day);
			c.set(Calendar.HOUR_OF_DAY, hour);
			c.set(Calendar.MINUTE, min);
			c.set(Calendar.SECOND, sec);
			rc = c.getTime().getTime();
		}
		return rc;
	}

	/**
	 *
	 */
	public static long adjustTimeFromGMT(long date, String timeZone) {
		long rc = 0;
		if (date > 0 && timeZone != null) {
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));
			c.setTime(new Date(date));
			TimeZone tz = TimeZone.getDefault();
			if (timeZone != null) {
				tz = TimeZone.getTimeZone(timeZone);
			}
			int o = tz.getOffset(1, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK), (c.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000));
			rc = date + o;
		}
		return rc;
	}

	/**
	 * Handles unfolding data as described by RFC 2425.
	 * 
	 * Unfolding is accomplished by removing the CRLF character and the linear
	 * white space character that immediately follows.
	 * 
	 * @param StringBuffer
	 *            The folder data
	 * @return The unfolded data
	 */
	public static StringBuffer unfold(StringBuffer v) {
		StringBuffer rc = new StringBuffer(v.length());
		int len = v.length();
		for (int i = 0; i < len; i++) {
			if (v.charAt(i) == '\r' && i < (len - 2) && v.charAt(i + 1) == '\n' && v.charAt(i + 2) == ' ') {
				i += 3;
			}
			rc.append(v.charAt(i));
		}
		return rc;
	}

	/**
	 * Removes escape character "\" from string.  Also converts a "\""n"
	 * sequence to the new-line character.
	 * 
	 * @param String
	 * @return String
	 */
	public static String unescape(String in) {
		String rc = null;
		if (in != null) {
			if (in.indexOf('\\') < 0) {
				rc = in;
			} else {
				rc = "";
				int len = in.length();
				for (int i = 0; i < len; i++) {
					if (in.charAt(i) == '\\' && i < (len - 1) && in.charAt(i + 1) == 'n') {
						rc += '\n';
						i++;
					} else if (in.charAt(i) == '\\' && i < (len - 1) && in.charAt(i + 1) == ':') {
						continue;
					} else if (in.charAt(i) == '\\' && i < (len - 1) && in.charAt(i + 1) == '\\') {
						continue;
					} else if (in.charAt(i) == '\\' && i < (len - 1) && in.charAt(i + 1) == ',') {
						continue;
					} else if (in.charAt(i) == '\\' && i < (len - 1) && in.charAt(i + 1) == ';') {
						continue;
					} else {
						rc += in.charAt(i);
					}
				}
				in = null;
			}
		}
		return rc;
	}

	/**
	 * Helper for converting a String to an int when you don't care about the
	 * NumberFormatException.
	 * 
	 * @param String
	 * @return int
	 */
	public static int getInt(String str) {
		int rc = 0;
		try {
			rc = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
		}
		return rc;
	}

	/**
	 * Converts the string-month to an integer-month.
	 * 
	 * Note: Month starts at 0 (January).
	 * 
	 * @param String
	 * @return int
	 */
	public static int monthInt(String v) {
		int rc = 0;
		if (v != null) {
			if (v.length() > 2) {
				String m = v.toLowerCase();
				if (m.length() > 3) {
					m = m.substring(0, 3);
				}
				rc = "janfebmaraprmayjunjulaugsepoctnovdec".indexOf(m) / 3;
			}
		}
		return rc;
	}

	/**
	 * Returns the maximum number of days in the given month/year
	 * 
	 * @param year
	 * @param month
	 * @return int
	 */
	public static int daysInMonth(int month, int year) {
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			return 31;
		case 3:
		case 5:
		case 8:
		case 10:
			return 30;
		case 1:
			return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)) ? 29 : 28;
		default:
			return 31;
		}
	}

	/**
	 * Helper for splitting a String by the specified separator. If no separator
	 * is found then the original String is returned in String[0].
	 * 
	 * Does NOT support escaping in-line separator chars.
	 * 
	 * @param String
	 * @param separator
	 * @return String[]
	 */
	public static String[] split(String v, char sep) {
		String[] rc = null;
		if (v != null) {
			int count = countValues(v, sep);
			rc = new String[count];
			int b = 0;
			int e = v.indexOf(sep, b);
			int i = 0;
			if (e < 0) {
				rc[i] = v;
			} else {
				while (e != -1) {
					rc[i++] = v.substring(b, e);
					b = e + 1;
					e = v.indexOf(sep, b);
				}
				rc[i] = v.substring(b, v.length());
			}
		}
		return rc;
	}

	/**
	 * Helper for closing an InputStream when you don't care about the
	 * IOEException.
	 * 
	 * @param v
	 */
	public static void closeIn(InputStream v) {
		if (v != null) {
			try {
				v.close();
			} catch (IOException ioe) {
			}
			v = null;
		}
	}

	/**
	 * Helper for closing an OutputStream when you don't care about the
	 * IOEException.
	 * 
	 * @param v
	 */
	public static void closeOut(OutputStream v) {
		if (v != null) {
			try {
				v.close();
			} catch (IOException ioe) {
			}
			v = null;
		}
	}

	/**
	 * Helper for closing a Reader when you don't care about the IOEException.
	 * 
	 * @param v
	 */
	private static void closeReader(Reader v) {
		if (v != null) {
			try {
				v.close();
			} catch (IOException ioe) {
			}
			v = null;
		}
	}

	/**
	 * Simple Base64 encoder. Intended for small(ish) strings like HTTP basic
	 * auth parameters.
	 * 
	 * @param String
	 *            String to be Base64 encoded
	 * @return String Base64 encoded string
	 */
	public static String b64Encode(String v) {
		String rc = null;
		if (v != null) {
			byte[] src = null;
			try {
				src = v.getBytes(C.DEFAULT_ENCODING);
			} catch (Throwable t) {
			}
			if (src != null) {
				int length = src.length;
				byte[] dst = new byte[(length + 2) / 3 * 4 + length / 72];
				int x = 0;
				int dstIndex = 0;
				int state = 0;
				int old = 0;
				int len = 0;
				int max = length;
				for (int srcIndex = 0; srcIndex < max; srcIndex++) {
					x = src[srcIndex];
					switch (++state) {
					case 1:
						dst[dstIndex++] = encodeData[(x >> 2) & 0x3f];
						break;
					case 2:
						dst[dstIndex++] = encodeData[((old << 4) & 0x30) | ((x >> 4) & 0xf)];
						break;
					case 3:
						dst[dstIndex++] = encodeData[((old << 2) & 0x3C) | ((x >> 6) & 0x3)];
						dst[dstIndex++] = encodeData[x & 0x3F];
						state = 0;
						break;
					}
					old = x;
					if (++len >= 72) {
						dst[dstIndex++] = (byte) '\n';
						len = 0;
					}
				}

				switch (state) {
				case 1:
					dst[dstIndex++] = encodeData[(old << 4) & 0x30];
					dst[dstIndex++] = (byte) '=';
					dst[dstIndex++] = (byte) '=';
					break;
				case 2:
					dst[dstIndex++] = encodeData[(old << 2) & 0x3c];
					dst[dstIndex++] = (byte) '=';
					break;
				}
				rc = new String(dst);
			}
		}
		return rc;
	}

	public static String getContentEncoding(InputStream is) {
		String rc = C.DEFAULT_ENCODING;
		if (is != null) {
			if (is.markSupported()) {
				byte[] bom = new byte[4];
				try {
					is.read(bom);
					if (bom[0] == (byte) 0xff && bom[1] == (byte) 0xfe) {
						rc = "UTF-16LE";
					} else if (bom[0] == (byte) 0xfe && bom[1] == (byte) 0xff) {
						rc = "UTF-16BE";
					} else if (bom[0] == (byte) 0x3c && bom[1] == (byte) 0x00) {
						rc = "UTF-16LE";
					} else if (bom[0] == (byte) 0x00 && bom[1] == (byte) 0x3c) {
						rc = "UTF-16BE";
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} finally {
					try {
						is.reset();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}
		}
		return rc;
	}

	/**
     *
     */
	public static StringBuffer convertToString(InputStream is) {
		StringBuffer rc = null;
		if (is != null) {
			rc = new StringBuffer();
			InputStreamReader br = null;
			try {
				br = new InputStreamReader(is);
				char[] c = new char[8192];
				int e = 0;
				while ((e = br.read(c)) > 0) {
					rc.append(c, 0, e);
				}
				c = null;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				StringUtil.closeReader(br);
				StringUtil.closeIn(is);
			}
		}
		return rc;
	}

	/**
	 *
	 */
	public static String byteArrayToHexString(byte in[]) {
		String rc = null;
		StringBuffer out = null;
		byte ch = 0x00;
		int i = 0;
		if (in != null && in.length > 0) {
			out = new StringBuffer(in.length * 2);
			while (i < in.length) {
				ch = (byte) (in[i] & 0xF0);
				ch = (byte) (ch >>> 4);
				ch = (byte) (ch & 0x0F);
				out.append(B16[(int) ch]);
				ch = (byte) (in[i] & 0x0F);
				out.append(B16[(int) ch]);
				i++;
			}
			rc = new String(out);
		}
		out = null;
		return rc;
	}

	/**
	 *
	 */
	public static Calendar getZeroCalendar() {
		Calendar rc = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		rc.set(Calendar.YEAR, 0);
		rc.set(Calendar.MONTH, 0);
		rc.set(Calendar.DAY_OF_MONTH, 0);
		rc.set(Calendar.HOUR_OF_DAY, 0);
		rc.set(Calendar.MINUTE, 0);
		rc.set(Calendar.SECOND, 0);
		rc.set(Calendar.MILLISECOND, 0);
		return rc;
	}

	/**
     *
     */
	public static int indexOf(char k, StringBuffer v, int b) {
		int rc = -1;
		if (v != null && b >= 0) {
			for (int i = b; i < v.length(); i++) {
				if (k == v.charAt(i)) {
					rc = i;
					break;
				}
			}
		}
		return rc;
	}

	/**
     *
     */
	public static String substring(StringBuffer v, int b, int e, char[] buf) {
		String rc = null;
		if (v != null && e - b > 0) {
			v.getChars(b, e, buf, 0);
			rc = new String(buf, 0, e - b);
		}
		return rc;
	}

	/**
     *
     */
	public static void escapeString(StringBuffer out, String str) {
		if (out == null || str == null) {
			return;
		}
		int sz;
		sz = str.length();
		for (int i = 0; i < sz; i++) {
			char ch = str.charAt(i);
			if (ch > 0xfff) {
				out.append("\\u" + Integer.toHexString(ch).toUpperCase());
			} else if (ch > 0xff) {
				out.append("\\u0" + Integer.toHexString(ch).toUpperCase());
			} else if (ch > 0x7f) {
				out.append("\\u00" + Integer.toHexString(ch).toUpperCase());
			} else if (ch < 32) {
				switch (ch) {
				case '\b':
					out.append('\\');
					out.append('b');
					break;
				case '\n':
					out.append('\\');
					out.append('n');
					break;
				case '\t':
					out.append('\\');
					out.append('t');
					break;
				case '\f':
					out.append('\\');
					out.append('f');
					break;
				case '\r':
					out.append('\\');
					out.append('r');
					break;
				default:
					if (ch > 0xf) {
						out.append("\\u00" + Integer.toHexString(ch).toUpperCase());
					} else {
						out.append("\\u000" + Integer.toHexString(ch).toUpperCase());
					}
					break;
				}
			} else {
				switch (ch) {
				case '\'':
					out.append('\'');
					break;
				case '"':
					out.append('\\');
					out.append('"');
					break;
				case '\\':
					out.append('\\');
					out.append('\\');
					break;
				case '/':
					out.append('/');
					break;
				default:
					out.append(ch);
					break;
				}
			}
		}
	}

	public static String findAndReplace(final String aInput, final String aOldPattern, final String aNewPattern) {
		if (aOldPattern.equals("")) {
			throw new IllegalArgumentException("Old pattern must have content.");
		}
		StringBuffer result = new StringBuffer();
		int startIdx = 0;
		int idxOld = 0;
		while ((idxOld = aInput.indexOf(aOldPattern, startIdx)) >= 0) {
			// grab a part of aInput which does not include aOldPattern
			result.append(aInput.substring(startIdx, idxOld));
			// add aNewPattern to take place of aOldPattern
			result.append(aNewPattern);

			// reset the startIdx to just after the current match, to see
			// if there are any further matches
			startIdx = idxOld + aOldPattern.length();
		}
		// the final chunk will go to the end of aInput
		result.append(aInput.substring(startIdx));
		return result.toString();
	}

	/**
     *
     */

	public static String URLEncode(String s) {
		StringBuffer r = new StringBuffer(s.length());
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			switch (c) {
			case '$': // These are required
			case '&':
			case '+':
			case ',':
			case '/':
			case ':':
			case ';':
			case '=':
			case '?':
			case '@':

			case ' ': // These are optional but recommended
			case '"':
			case '<':
			case '>':
			case '#':
			case '%':
			case '{':
			case '}':
			case '|':
			case '\\':
			case '^':
			case '~':
			case '[':
			case ']':
			case '`':
				r.append("%");
				r.append(Integer.toHexString(c).toUpperCase());
				break;
			default:
				r.append(c);
			}
		}
		return r.toString();
	}

	// ! # $ & ' ( ) * + , / : ; = ? @ [ ]
	// %21 %23 %24 %26 %27 %28 %29 %2A %2B %2C %2F %3A %3B %3D %3F %40 %5B %5D
	//
	//
	// Common characters after percent-encoding (ASCII or UTF-8 based)
	// space " % - . < > \ ^ _ ` { | } ~
	// A %20 %22 %25 %2D %2E %3C %3E %5C %5E %5F %60 %7B %7C %7D %7E
	public static String encodeURL(String s) {
		StringBuffer r = new StringBuffer(s.length());
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			switch (c) {
			case '$':
				r.append("%24");
				break;
			case '&':
				r.append("%26");
				break;
			case '+':
				r.append("%2B");
				break;
			case ',':
				r.append("%2C");
				break;
			case '/':
				r.append("/");
				break;
			case ':':
				r.append("%3A");
				break;
			case ';':
				r.append("%3B");
				break;
			case '=':
				r.append("%3D");
				break;
			case '?':
				r.append("%3F");
				break;

			case '@':
				r.append("@");
				break;

			case ' ':
				r.append("%20");
				break;
			case '"':
				r.append("%22");
				break;
			case '<':
				r.append("%3C");
				break;
			case '>':
				r.append("%3E");
				break;
			case '#':
				r.append("%23");
				break;
			case '%':
				r.append("%25");
				break;
			case '{':
				r.append("%7B");
				break;
			case '}':
				r.append("%7D");
				break;
			case '|':
				r.append("%7C");
				break;
			case '\\':
				r.append("\\");
				break;
			case '^':
				r.append("%5E");
				break;
			case '~':
				r.append("%7E");
				break;
			case '[':
				r.append("%5B");
				break;
			case ']':
				r.append("%5D");
				break;
			case '`':
				r.append("%60");
				break;

			default:
				r.append(c);
			}
		}
		return r.toString();
	}

	/**
     *
     */
	private static int countValues(String v, char sep) {
		int rc = 0;
		if (v != null) {
			rc = 1;
			int b = 0;
			while ((b = v.indexOf(sep, b)) >= 0) {
				rc++;
				b++;
			}
		}
		return rc;
	}
}
