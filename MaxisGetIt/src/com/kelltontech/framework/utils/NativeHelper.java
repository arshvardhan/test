package com.kelltontech.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.kelltontech.framework.logger.Logger;
import com.kelltontech.framework.network.NetworkInformation;
import com.kelltontech.framework.ui.BaseMainActivity;
import com.kelltontech.framework.utils.stringutils.StringUtil;
import com.kelltontech.framework.utils.stringutils.Text;

public class NativeHelper {

	/**
	 * Function to be called to initiate call.
	 * 
	 * @param context
	 * @param number
	 */

	public static void makeCall(Context context, String number) {
		// Creating intent to make call.....
		Intent call = new Intent(Intent.ACTION_CALL);
		call.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		call.setData(Uri.parse("tel:" + number));
		context.startActivity(call);
	}

	public static boolean isTelephonyAvailable(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
			// No calling functionality
			return false;
		} else {
			// calling functionality
			return true;
		}
	}

	/**
	 * Function to be used to send email.
	 * 
	 * @param context
	 * @param subject
	 * @param body
	 * @param address
	 */
	public static void sendMail(Context context, String address) {
		// Creating intent for sending mail....
		String[] recipients = new String[] { address, };
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, ""); // Subject of message
		emailIntent.putExtra(Intent.EXTRA_TEXT, ""); // Body of message
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
		try {
			context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "No Email Client Available.", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Function to be used to send sms.
	 * 
	 * @param context
	 * @param number
	 * @param body
	 */

	public static void sendSms(Context context, String number) {
		Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
		context.startActivity(sms);
	}

	/**
	 * Function used to detect if Network is available or not.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isDataConnectionAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info == null)
			return false;

		return connectivityManager.getActiveNetworkInfo().isConnected();
	}

	/**
	 * Function should be used to detect if GPS is enabled or not.
	 * 
	 * @param manager
	 * @return
	 */
	public static boolean isGpsEnabled(LocationManager manager) {
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return false;
		}
		return true;
	}

	public static boolean isLocationByNetworkEnabled(LocationManager manager) {
		if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			return false;
		}
		return true;
	}

	/**
	 * to use this you have to add permissions
	 * android.permission.READ_PHONE_STATE
	 * 
	 * As FROM_GOOGLE_NETWORK_REQUEST requires server request from the server so
	 * linked with the Framework so you also need the Controller and the model
	 * and response will come on the lister (here activity setcreendata function
	 * 
	 * @return
	 */
	public static final short FROM_BEST = 0;
	public static final short FROM_NETWORK = 1;// it may be off from setting
	public static final short FROM_GPS = 2;
	public static final short FROM_GOOGLE_NETWORK_REQUEST = 3;

	public static double[] getLatNLong(short provider, BaseMainActivity acivity) {
		double[] latNLong = null;
		switch (provider) {
		case FROM_BEST:
			latNLong = getLatnLongFromBestProvider(acivity.getApplicationContext());
			break;
		case FROM_NETWORK:
			latNLong = getLatnLongFromNetowrkProvider(acivity.getApplicationContext());
			break;
		case FROM_GPS:
			latNLong = getLatnLongFromGPSProvider(acivity.getApplicationContext());
			break;
		case FROM_GOOGLE_NETWORK_REQUEST:

			break;

		default:
			break;
		}
		return latNLong;
	}

	public static LocationManager getLocationManager(Context context) {//
		return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	public static double[] getLatnLongFromBestProvider(Context context) {
		double[] latNLong = null;
		LocationManager locationManager = getLocationManager(context);
		List<String> providerList = getListofEnableProviders(locationManager);// getListofAllProvider(locationManager);
		if (null != providerList && providerList.size() > 0) {
			String provider = locationManager.getBestProvider(new Criteria(), true);
			if (null != provider) {
				Location locations = locationManager.getLastKnownLocation(provider);
				if (null != locations)
					latNLong = new double[] { locations.getLatitude(), locations.getLongitude() };
			}
		}
		return latNLong;

	}
	public static String getMyPhoneNumber(Context context){
	    TelephonyManager mTelephonyMgr;
	    mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
	    return mTelephonyMgr.getLine1Number();
	}

	/**
	 * @param context
	 * @return 10 digit number or null
	 */
	public static String getMy10DigitPhoneNumber(Context context){
	    String s = getMyPhoneNumber(context);
	    if(s==null)
	    	return null;
	    if(s.length()>=10)
	    	return s.substring(s.length()-10);
	    return null;
	}
	public static double[] getLatnLongFromGPSProvider(Context context) {
		double[] latNLong = null;
		LocationManager locationManager = getLocationManager(context);
		if (isGpsEnabled(locationManager)) {
			Location locations = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (null != locations)
				latNLong = new double[] { locations.getLatitude(), locations.getLongitude() };
		} else {
			Intent i = new Intent();
			ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.SecuritySettings");
			i.setComponent(comp);
			context.startActivity(i);
		}
		return latNLong;

	}

	public static double[] getLatnLongFromNetowrkProvider(Context context) {
		double[] latNLong = null;
		LocationManager locationManager = getLocationManager(context);
		if (isLocationByNetworkEnabled(locationManager)) {
			Location locations = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (null != locations)
				latNLong = new double[] { locations.getLatitude(), locations.getLongitude() };
		}
		return latNLong;

	}

	/**
	 * As this requires server request from the server so linked with the
	 * Framework so you also need the Controller and the model and response will
	 * come on the lister (here activity setcreendata function)
	 * 
	 * @param listner
	 */
	public static void getLocationFromNetworkRequest(BaseMainActivity listner) {

	}

	public static List<String> getListofEnableProviders(LocationManager locationManager) {
		return locationManager.getProviders(true);
	}

	public static List<String> getListofAllProvider(Context context) {
		return getLocationManager(context).getAllProviders();
	}

	/**
	 * to use this you have to add permissions
	 * android.permission.READ_PHONE_STATE
	 * 
	 * @return
	 */
	public static boolean isSimulator(Context context) {
		boolean isSimulator = false;
		TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String uuid = tManager.getDeviceId();
		if (null == uuid || uuid.equals("000000000000000")) {
			isSimulator = true;
		}
		return isSimulator;
	}

	/**
	 * CellID: network.serving.getCellidStr() Lac: network.serving.getLacStr()
	 * Mcc: network.serving.getMccStr() Mnc: network.serving.getMncStr() Rssi:
	 * network.serving.getRssiStr()if (network.neighbour != null &&
	 * network.neighbour.size() > 0) Neigbour cellid 1:
	 * network.neighbour.get(0).getCellidStr()); if (network.neighbour != null
	 * && network.neighbour.size() > 1) Neigbour cellid 2:
	 * network.neighbour.get(1).getCellidStr()); if (network.neighbour != null
	 * && network.neighbour.size() > 2)"Neigbour cellid 3:
	 * network.neighbour.get(2).getCellidStr()); if (network.wifi != null &&
	 * network.wifi.size() > 0)"Wifi network 1: network.wifi.get(0).ssid); if
	 * (network.wifi != null && network.wifi.size() > 1)"Wifi network 2:
	 * network.wifi.get(1).ssid); if (network.wifi != null &&
	 * network.wifi.size() > 2)"Wifi network 3: network.wifi.get(2).ssid);
	 * 
	 * @param context
	 * @return
	 */
	public static NetworkInformation getNetworkOperatorInfo(Context context) {
		// model having all data
		NetworkInformation network = new NetworkInformation();
		/*
		 * Mcc and mnc is concatenated in the networkOperatorString. The first 3
		 * chars is the mcc and the last 2 is the mnc.
		 */
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
		try {
			network.serving.cellid = Integer.parseInt(location.getCid() + "", 16);
			network.serving.lac = Integer.parseInt(location.getLac() + "", 16);
		} catch (NumberFormatException e) {
			Logger.easyLog(Logger.LEVEL_ERROR, "Native Helper", "NumberFormatException in cellid or location id");
		}
		String networkOperator = tm.getNetworkOperator();
		if (networkOperator != null && networkOperator.length() > 0) {
			try {
				network.serving.mcc = Integer.parseInt(networkOperator.substring(0, 3));
				network.serving.mnc = Integer.parseInt(networkOperator.substring(3));
			} catch (NumberFormatException e) {
				Logger.easyLog(Logger.LEVEL_ERROR, "Native Helper", "NumberFormatException in cellid or location id");
			}
		}
		/*
		 * Check if the current cell is a UMTS (3G) cell. If a 3G cell the cell
		 * id padding will be 8 numbers, if not 4 numbers.
		 */
		if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
			network.serving.padding = 8;
		} else {
			network.serving.padding = 4;
		}

		/*
		 * Update with info about neighboring cells (max 3)
		 */
		network.neighbour.clear();
		List<NeighboringCellInfo> networkNeighbours = tm.getNeighboringCellInfo();
		if (networkNeighbours != null && networkNeighbours.size() > 0)
			for (int i = 0; i < networkNeighbours.size() && i < 3; i++)
				network.addNeighboringCell(networkNeighbours.get(i).getCid(), networkNeighbours.get(i).getRssi());
		else
			Log.i("TEST", "No neighboring cells found");

		/*
		 * Update with info about wifi networks (max 3)
		 */
		network.wifi.clear();
		List<ScanResult> wifiNetworks = wifi.getScanResults();
		if (wifiNetworks != null && wifiNetworks.size() > 0)
			for (int i = 0; i < wifiNetworks.size() && i < 3; i++)
				network.addWifiCell(wifiNetworks.get(i).BSSID, wifiNetworks.get(i).SSID, wifiNetworks.get(i).level);
		else
			Log.i("TEST", "No wifi networks found");
		return network;
	}

	// Function to validate email address........
	public static boolean emailAddressValidator(String mail) {
		Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = pattern.matcher(mail);
		if (matcher.matches())
			return true;
		else
			return false;
	}

	/**
	 * Converts InputStream to array of bytes
	 * 
	 * @param is
	 * @return byte array from given stream
	 */
	public static byte[] convertStreamtoBytes(InputStream is) {
		/** read count */
		int read = 0;
		/** data will be read in chunks */

		byte[] data = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			while ((read = is.read(data)) != -1) {
				baos.write(data, 0, read);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public static byte[] convertStreamtoBytes2(InputStream is) {
	 * ByteArrayOutputStream bytes = new ByteArrayOutputStream(); byte[] buffer
	 * = new byte[128]; int read; int totalRead = 0; while ((read =
	 * is.read(buffer)) > 0) { totalRead += read; if (totalRead > TOO_BIG) { //
	 * abort download. close connection return null; } bytes.write(buffer, 0
	 * read); }
	 */

	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();

		if (imei.equals("")) {
			telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();

		}

		return imei;
	}

	public static String getIMSI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		String imsi = telephonyManager.getSubscriberId();
		if (imsi.equals("")) {
			telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			imsi = telephonyManager.getSubscriberId();
		}

		return imsi;
	}

	public static double roundOff2place(double n) {
		// DecimalFormat df = new DecimalFormat();
		// df.setMaximumFractionDigits(2);
		// df.setMinimumFractionDigits(2);
		// df.format(n);
		return (double) Math.round(n * 100.0) / 100.0;
	}

	public static String roundOff2placeStr(double n) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		return df.format(n);
	}

	/**
	 * 
	 * @param context
	 *            : to get TelephonyManager
	 * @return imei of the phone..
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		if (imei.equals("")) {
			telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		}
		// Used hardcoded value for testing of coupons
		return imei;// "123456789";//
	}

	public static String getDeviceInformation(Context context) {
		String deviceInfo = "";
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		deviceInfo += "DeviceID : " + telephonyManager.getDeviceId() + "\n\r";
		deviceInfo += "PhoneType : " + telephonyManager.getPhoneType() + "\n\r";
		deviceInfo += "NetworkOperatorName : " + telephonyManager.getNetworkOperatorName() + "\n\r";
		deviceInfo += "NetworkType : " + telephonyManager.getNetworkType() + "\n\r";
		deviceInfo += "SimOperatorName : " + telephonyManager.getSimOperatorName() + "\n\r";
		deviceInfo += "MANUFACTURER : " + Build.MANUFACTURER + "\n\r";
		deviceInfo += "MODEL : " + Build.MODEL + "\n\r";
		deviceInfo += "VERSION.SDK_INT : " + Build.VERSION.SDK_INT + "\n\r";

		return deviceInfo;
	}

	public static HashMap getDevLoggingInfo(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		HashMap dinfo = new HashMap<String, String>();
		dinfo.put("DeviceID", "" + telephonyManager.getDeviceId());
		switch (telephonyManager.getPhoneType()) {
		case TelephonyManager.PHONE_TYPE_GSM:
			dinfo.put("PhoneType", "GSM");
			break;
		case TelephonyManager.PHONE_TYPE_CDMA:
			dinfo.put("PhoneType", "CDMA");
			break;
		default:
			dinfo.put("PhoneType", "None");
			break;
		}
		dinfo.put("MODEL", "" + Build.MODEL);
		dinfo.put("VERSION.SDK_INT", Build.VERSION.SDK_INT + "");
		if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			dinfo.put("Screen Size", "Large screen");
		} else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			dinfo.put("Screen Size", "Normal sized screen");

		} else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			dinfo.put("Screen Size", "Small sized screen");
		} else {
			dinfo.put("Screen Size", "Screen size Undefined");
		}
		return dinfo;
	}

	public static String getDeviceRealse() {
		return Build.VERSION.RELEASE;
	}

	public static int getDeviceSDK() {
		return Build.VERSION.SDK_INT;
	}

	public static String getDeviceCodeName() {
		return Build.VERSION.CODENAME;
	}

	// method for getting formatted time
	public static String getFomattedTime(int minutes) {
		String time = (minutes / 60) + " hr. " + (minutes % 60) + " min.";
		return time;
	}

	// method for getting formatted date
	public static String getFormattedDate(String date) {
		if (!Text.isNull(date)) {
			String newDateStr;
			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date dateObj = null;
			try {
				dateObj = curFormater.parse(date);
				SimpleDateFormat postFormater = new SimpleDateFormat("MMMM d, yyyy");
				newDateStr = postFormater.format(dateObj);
			} catch (ParseException e) {
				newDateStr = date;
			}

			return newDateStr;
		}
		return "";
	}

	public static Date getDateObj(String date) {
		if (!Text.isNull(date)) {

			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
			Date dateObj = null;
			try {
				dateObj = curFormater.parse(date);

			} catch (ParseException e) {

			}

			return dateObj;
		}
		return null;
	}

	public static String getFormattedString(ArrayList<String> strArr) {
		String result = new String();
		if (strArr != null) {
			for (int index = 0; index < strArr.size(); index++) {
				result = result + (strArr.get(index));
				if (index != strArr.size() - 1)
					result = result + (", ");
			}
		}
		return result;
	}

	public static long dateToMilliSeconds(int day, int month, int year, int hour, int minute, int sec) {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getDefault());
		// ;//TODO:Time zone of system
		c.set(year, month, day, hour, minute, sec);
		return c.getTimeInMillis();

	}

	public static String encodeURL(String url) {
		return url.replaceAll(" ", "%20");

	}

	public static boolean isValidEmail(String email) {
		String emailEx = "^[A-Z0-9a-z._%+-]{2,24}+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
		Pattern emailPattern;
		emailPattern = Pattern.compile(emailEx);
		if (!emailPattern.matcher(email).matches())
			return false;

		return true;
	}

	public static boolean isValidPassword(String password) {
		String passwordEx = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).[a-zA-Z0-9]{7,15})";
		Pattern passwordPattern;
		passwordPattern = Pattern.compile(passwordEx);
		if (!passwordPattern.matcher(password).matches())
			return false;

		return true;
	}

	public static boolean isValidZipcode(String zipcode) {
		String zipEx = "^\\d{5}$";
		Pattern zipPattern;
		zipPattern = Pattern.compile(zipEx);
		if (!zipPattern.matcher(zipcode).matches())
			return false;

		return true;

	}

	public static boolean isValidMobile(String mobile) {
		String mobileRegEx = "^\\d{10}$";
		Pattern mobilePattern;
		mobilePattern = Pattern.compile(mobileRegEx);
		if (!mobilePattern.matcher(mobile).matches())
			return false;

		return true;

	}

	/*
	 * @ param Today, Mar 14
	 */

	public static String getDateString(long time) {
		String dateStr = "";
		SimpleDateFormat sdf;
		Calendar selectedCal = Calendar.getInstance();
		Calendar currentCal = Calendar.getInstance();
		selectedCal.setTimeInMillis(time);
		sdf = new SimpleDateFormat("MMMM");
		String month = sdf.format(selectedCal.getTime());
		sdf = new SimpleDateFormat("EEEE");
		String day = sdf.format(selectedCal.getTime());
		boolean isSpecialMonth = month.equalsIgnoreCase("May") || month.equalsIgnoreCase("April") || month.equalsIgnoreCase("June") || month.equalsIgnoreCase("July");
		boolean isSameMonthYear = selectedCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH) && selectedCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR);
		int dateDiff = selectedCal.get(Calendar.DATE) - currentCal.get(Calendar.DATE);

		if (dateDiff == 0 && isSameMonthYear)
			day = "Today";
		else if (dateDiff == 1 && isSameMonthYear)
			day = "Tom";
		else if (day.equalsIgnoreCase("Monday"))
			day = "Mon";
		else if (day.equalsIgnoreCase("Tuesday"))
			day = "Tues";
		else if (day.equalsIgnoreCase("Wednesday"))
			day = "Wed";
		else if (day.equalsIgnoreCase("Thursday"))
			day = "Thurs";
		else if (day.equalsIgnoreCase("Friday "))
			day = "Fri";
		else if (day.equalsIgnoreCase("Saturday"))
			day = "Sat";
		else if (day.equalsIgnoreCase("Sunday"))
			day = "Sun";

		if (isSpecialMonth)
			sdf = new SimpleDateFormat(", MMMM dd");
		else
			sdf = new SimpleDateFormat(", MMM dd");

		dateStr = day + sdf.format(selectedCal.getTime());
		return dateStr;
	}

	/*
	 * @ param String eg 2011-10-30 4:54 pm\am 2012-01-12 10:39 PM GMT-03:00
	 */
	public static long getTime(String date) {
		try {
			String[] splitArry = StringUtil.split(date, ' ');
			String showtimeDate = splitArry[0];
			String timings = splitArry[1];

			int year = Integer.parseInt(showtimeDate.substring(0, 4));
			int month = Integer.parseInt(showtimeDate.substring(5, 7)) - 1;
			int day = Integer.parseInt(showtimeDate.substring(8, 10));

			String[] time = StringUtil.split(timings, ':');
			int hour = Integer.parseInt(time[0]);
			int min = Integer.parseInt(time[1]);

			Calendar calendar = Calendar.getInstance();
			if (splitArry.length > 3 && !Text.isNull(splitArry[3]))
				calendar.setTimeZone(TimeZone.getTimeZone(splitArry[3]));

			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			if (splitArry[2].equalsIgnoreCase("am")) {
				// calendar.set(Calendar.AM_PM, Calendar.AM);
				hour = hour == 12 ? 0 : hour;
			} else {
				// calendar.set(Calendar.AM_PM, Calendar.PM);

				hour = hour == 12 ? hour : hour + 12;

			}
			calendar.set(Calendar.MINUTE, min);
			calendar.set(Calendar.HOUR_OF_DAY, hour);

			return calendar.getTimeInMillis();

		} catch (Exception e) {
			return 0;
		}
	}

	/*
	 * @ param String eg 2011-10-30
	 */

	public static long getTimeFromShortDate(String date) {

		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7)) - 1;
		int day = Integer.parseInt(date.substring(8, 10));

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);

		System.out.println(getDateString(calendar.getTimeInMillis()));

		return calendar.getTimeInMillis();
	}

	public static double getDistanceInMeter(double lat1, double lon1, double lat2, double lon2) {
		return getDistanceInMile(lat1, lon1, lat2, lon2) * 1609.344;
	}

	public static double getDistanceInMile(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public static String replaceSpecialCharacters(String str) {
		// http://www.utexas.edu/learn/html/spchar.html
		// above url is used for html special characters.

		if (str == null)
			return null;
		if (str.trim().equals(""))
			return "";

		String[] htmlSpecialcharArray = { "&ndash;", "&mdash;", "&iexcl;", "&iquest;", "&quot;", "&ldquo;", "&rdquo;", "&lsquo;", "&rsquo;", "&laquo;", "&raquo;", "&nbsp;", "&amp;", "&cent;", "&copy;", "&divide;", "&gt;", "&lt;",
				"&micro;", "&middot;", "&para;", "&plusmn;", "&euro;", "&pound;", "&reg;", "&sect;", "&trade;", "&yen;", "&aacute;", "&Aacute;", "&agrave;", "&Agrave;", "&acirc;", "&Acirc;", "&aring;", "&Aring;", "&atilde;",
				"&Atilde;", "&auml;", "&Auml;", "&aelig;", "&AElig;", "&ccedil;", "&Ccedil;", "&eacute;", "&Eacute;", "&egrave;", "&Egrave;", "&ecirc;", "&Ecirc;", "&euml;", "&Euml;", "&iacute;", "&Iacute;", "&igrave;",
				"&Igrave;", "&icirc;", "&Icirc;", "&iuml;", "&Iuml;", "&ntilde;", "&Ntilde;", "&oacute;", "&Oacute;", "&ograve;", "&Ograve;", "&ocirc;", "&Ocirc;", "&oslash;", "&Oslash;", "&otilde;", "&Otilde;", "&ouml;",
				"&Ouml;", "&szlig;", "&uacute;", "&Uacute;", "&ugrave;", "&Ugrave;", "&ucirc;", "&Ucirc;", "&uuml;", "&Uuml;", "&yuml;", "&sbquo;&not;", "&Atilde;", "&uml;", "&brvbar;", "&bdquo;", "&sup1;", "&ordf;", "&#039;" };

		String[] stringSpecialcharArray = { "ñ", "ó", "°", "ø", "\"", "ì", "ì", "ë", "í", "´", "ª", " ", "And", "¢", "©", "˜", ">", "<", "µ", "∑", "∂", "±", "Ä", "£", "Æ", "ß", "ô", "•", "·", "¡",
				"‡", "¿", "‚", "¬", "Â", "≈", "„", "√", "‰", "ƒ", "Ê", "∆", "�?", "«", "È", "…", "Ë", "»", "�?", " ", "Î", "À", "Ì", "Õ", "�?", "Ã", "Ó", "Œ", "Ô", "œ", "Ò", "—", "Û", "�?",
				"Ú", "“", "Ù", "‘", "¯", "ÿ", "ı", "’", "ˆ", "÷", "ﬂ", "˙", "�?�", "˘", "Ÿ", "˚", "€", "¸", "‹", "ˇ", "¨", "~", "U", "b", "q", "s", "o", "'" };

		// replce special charaters one by one
		int len = htmlSpecialcharArray.length;
		for (int index = 0; index < len; index++) {
			str = replaceOld(str, htmlSpecialcharArray[index], stringSpecialcharArray[index]);
		}
		return str;
	}

	public static String replaceOld(final String aInput, final String aOldPattern, final String aNewPattern) {
		if (aOldPattern == null || aOldPattern.equals("")) {
			// throw new
			// IllegalArgumentException("Old pattern must have content.");
			return "";
		}
		final StringBuffer result = new StringBuffer();
		// startIdx and idxOld delimit various chunks of aInput; these
		// chunks always end where aOldPattern begins
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

	public static String getLocale(Context context) {
		String country = context.getResources().getConfiguration().locale.getLanguage();
		Log.d("country code", country);
		if (country == null || country.trim().equals(""))
			return "en";
		return country;
	}

	public static String getDisplayDensity(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int density = metrics.densityDpi;
		if (density > DisplayMetrics.DENSITY_HIGH) {
			return "xhdpi";
		} else if (density == DisplayMetrics.DENSITY_HIGH) {
			return "hdpi";
		} else if (density == DisplayMetrics.DENSITY_MEDIUM) {
			return "mdpi";
		} else if (density == DisplayMetrics.DENSITY_LOW) {
			return "mdpi";
		} else {
			return "mdpi";
		}
	}

}