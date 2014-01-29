package com.kelltontech.framework.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import com.kelltontech.framework.C;

public class HttpHelper {

	static void setHeaderPram(HttpPost httpPost, Hashtable<String, String> paramsHT) {
		if (null == httpPost || paramsHT == null || paramsHT.size() == 0)
			return;
		Enumeration<String> keyEnumeration = paramsHT.keys();
		while (keyEnumeration.hasMoreElements()) {
			String name = keyEnumeration.nextElement();
			String value = paramsHT.get(name);
			httpPost.setHeader(name, value);
		}

	}

	static List<NameValuePair> getParams(final Hashtable<String, ?> hashtable) {
		if (hashtable == null || hashtable.size() == 0) {
			return null;
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Enumeration<String> keyEnumeration = hashtable.keys();
		while (keyEnumeration.hasMoreElements()) {
			String name = keyEnumeration.nextElement();
			Object value = hashtable.get(name);
			params.add(new BasicNameValuePair(name, String.valueOf(value)));
		}
		return params;
	}

	public static String readString(InputStream in) throws IOException {
		byte[] bytes = readBytes(in);
		in.close();
		String texto = new String(bytes);
		return texto;
		// BufferedReader r = new BufferedReader(new InputStreamReader(in));
		// StringBuilder total = new StringBuilder();
		// String line;
		// while ((line = r.readLine()) != null) {
		// total.append(line);
		// }
		// return total.toString();
	}

	private static byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}

			byte[] bytes = bos.toByteArray();
			return bytes;
		} finally {
			bos.close();
		}
	}

	public static String getURLWithPrams(String url, Hashtable<String, String> params) {
		StringBuffer urlBuffer = new StringBuffer(url);

		if ((params != null) && (params.size() > 0)) {
			urlBuffer.append('?');
			Enumeration<String> keysEnum = params.keys();

			while (keysEnum.hasMoreElements()) {
				String key = keysEnum.nextElement();
				String val = params.get(key);

				urlBuffer.append(key).append('=').append(URLEncoder.encode(val));

				if (keysEnum.hasMoreElements()) {
					urlBuffer.append('&');
				}
			}
		}
		return urlBuffer.toString();
	}

	public static BasicHttpParams getHttpParams(BasicHttpParams mHttpParams) {
		if (mHttpParams == null) {
			mHttpParams = new BasicHttpParams();
			mHttpParams.setParameter(HttpProtocolParams.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			mHttpParams.setBooleanParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
			mHttpParams.setBooleanParameter(HttpConnectionParams.STALE_CONNECTION_CHECK, false);
			mHttpParams.setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 8 * 1024);
			mHttpParams.setIntParameter(HttpConnectionParams.SO_TIMEOUT, (int) C.CONNECTION_TIMEOUT);
			// Default connection and socket timeout of 20 seconds. Tweak to
			// taste.
			// mHttpParams.setIntParameter(HttpConnectionParams.
			// CONNECTION_TIMEOUT, 20 * 1000);
		}
		return mHttpParams;
	}

	public static void setHeaderPram(HttpGet httpGet, Hashtable<String, String> paramsHT) {
		if (null == httpGet || paramsHT == null || paramsHT.size() == 0)
			return;
		Enumeration<String> keyEnumeration = paramsHT.keys();
		while (keyEnumeration.hasMoreElements()) {
			String name = keyEnumeration.nextElement();// Param must be of
														// string type
			String value = paramsHT.get(name);
			httpGet.setHeader(name, value);
		}

	}

}
