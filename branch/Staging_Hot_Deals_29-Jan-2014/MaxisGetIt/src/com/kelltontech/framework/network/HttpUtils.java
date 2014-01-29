package com.kelltontech.framework.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class HttpUtils {

	public static boolean hasConnectivity(Context context) {
		boolean rc = false;
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
				rc = true;
			}
		}
		return rc;
	}

}