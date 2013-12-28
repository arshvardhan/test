package com.kelltontech.maxisgetit.utils;

import java.security.MessageDigest;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.framework.utils.StringUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),	MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight	+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static String getMobileNoForWS(Context context, String mobile) {
		return context.getResources().getString(R.string.country_code_excluding_plus) + mobile;
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return model;
		} else {
			return manufacturer + " " + model;
		}
	}

	public static String getVersion(Context context) {
		String version = null;
		PackageInfo pInfo;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	
	public static String getPackageName(Context context) {
		String version = null;
		PackageInfo pInfo;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pInfo.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * @param context
	 * @return
	 */
	public static String getUniqueId(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String uuid = mTelephonyMgr.getDeviceId();
		if (!StringUtil.isNullOrEmpty(uuid)) {
			return "IMEI" + uuid;
		}
		uuid = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		if (!StringUtil.isNullOrEmpty(uuid)) {
			return "AND_ID" + uuid;
		}
		uuid = mTelephonyMgr.getSimSerialNumber();
		if (!StringUtil.isNullOrEmpty(uuid)) {
			return "SIM_SR" + uuid;
		} else {
			return "Unknown_device";
		}
	}

	/**
	 * @param inputStr
	 * @return
	 */
	public static String getMD5Hash(String inputStr) throws Exception {
		byte[] inputBytes = inputStr.getBytes("UTF-8");
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(inputBytes, 0, inputBytes.length);
		byte[] hashedBytes = new byte[16];
		int bytesNum = digest.digest(hashedBytes, 0, hashedBytes.length);
		StringBuffer hexStringBuffer = new StringBuffer();
		for (int i = 0; i < bytesNum; i++) {
			String hex = Integer.toHexString(0xFF & hashedBytes[i]);
			if (hex.length() == 1)
			    hexStringBuffer.append('0');
			hexStringBuffer.append(hex);
		}
		String hexString = hexStringBuffer.toString();
		return hexString;
	}
}