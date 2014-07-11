package com.kelltontech.maxisgetit.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;

/**
 * @author vipin.sahu JSONHandler offer a mechanism to reuse objects that are
 *         expensive to create frequently.
 */
public class JSONHandler extends JSONParser {

	private static JSONHandler jsonHandler;
	private static Gson gson;
	private CookieStore cookieStore = new BasicCookieStore();
	private String session_id = "";
	private static List<Cookie> cookie;

	public static JSONHandler getJsonHandler() {
		return jsonHandler;
	}

	public static void setJsonHandler(JSONHandler jsonHandler) {
		JSONHandler.jsonHandler = jsonHandler;
	}

	public static Gson getGson() {
		return gson;
	}

	public static void setGson(Gson gson) {
		JSONHandler.gson = gson;
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	private JSONHandler() {
		gson = new Gson();

	}

	/**
	 * @return return the Instance of JSONHandler and ensure that only one
	 *         instance of a class is created and can be accessed any where
	 */
	public static JSONParser getInstanse() {
		if (jsonHandler == null) {
			jsonHandler = new JSONHandler();

		}
		return jsonHandler;

	}

	@Override
	public void parse_JSON(JSONObject jsonObject) throws Exception {

	}

	@Override
	public JSONObject getJSONObject(String uri) {
		try {
			String result = getStringJSON(uri);
			if (result != null)
				return new org.json.JSONObject(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JSONObject getJSONObject(String url, List<NameValuePair> params) {

		return null;
	}

	@Override
	public JSONObject getJSONObjectLogin(String url, List<NameValuePair> params) {

		return null;
	}

	@Override
	public String getStringJSON(String url) throws Exception {
		synchronized (this) {

			InputStream is = null;
			try {

				// Log.println(Log.ASSERT, url, "");
				Log.i("requesturl", url);

				final DefaultHttpClient httpClient = new DefaultHttpClient();
				httpClient.getParams().setParameter(
						ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
				HttpPost httpPost = new HttpPost(url);
				/** maintain session if logged in */
				HttpContext localContext = new BasicHttpContext();
				localContext.setAttribute(ClientContext.COOKIE_STORE,
						cookieStore);
				httpClient.setCookieStore(cookieStore);
				HttpResponse httpResponse = httpClient.execute(httpPost,
						localContext);
				setCookies(httpClient.getCookieStore().getCookies());
				HttpEntity httpEntity = httpResponse.getEntity();
				String errorHandlling = EntityUtils.toString(httpEntity)
						.replace("@", "");
				int index = errorHandlling.indexOf("{\"attributes\":{},", 0);
				if (index == -1) {
					return errorHandlling;
				}
				String result = errorHandlling.substring(index,
						errorHandlling.length());
				// Log.i("response", result);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		}
		return url;
	}

	@Override
	public String getStringJSON(String url, List<NameValuePair> nameValuePairs)
			throws Exception {
		synchronized (this) {
			InputStream is = null;
			try {

				final DefaultHttpClient httpClient = new DefaultHttpClient();
				httpClient.getParams().setParameter(
						ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
				HttpPost httpPost = new HttpPost();
				httpPost.setURI(new URI(url));
				Log.i("manish", url);

				HttpParams params = httpClient.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 100000);
				HttpConnectionParams.setSoTimeout(params, 1000000);

				httpPost.setURI(new URI(url));
				// String header[] = ProjectUtils.getHeaderNames();
				// String value[] = ProjectUtils.getHeaderValues();
				// httpPost.setHeader(header[0], value[0]);

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpContext localContext = new BasicHttpContext();
				localContext.setAttribute(ClientContext.COOKIE_STORE,
						cookieStore);
				httpClient.setCookieStore(cookieStore);
				HttpResponse httpResponse = httpClient.execute(httpPost,
						localContext);
				HttpEntity httpEntity = httpResponse.getEntity();
				String errorHandlling = EntityUtils.toString(httpEntity);
				httpClient.getConnectionManager().shutdown();
				return errorHandlling;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return convertStreamToString(is);
		}
	}

	@Override
	public String getSessionStringJSON(String url) throws Exception {
		InputStream is = null;
		try {

			final DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			HttpPost httpPost = new HttpPost(url);
			/** maintain session if logged in */
			getNewSessionValue(url, httpPost);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			setCookies(httpClient.getCookieStore().getCookies());
			HttpEntity httpEntity = httpResponse.getEntity();
			return EntityUtils.toString(httpEntity).replace("@", "");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convertStreamToString(is);
	}

	@Override
	public String getStringWithoutHeaderJSON(String url) throws Exception {
		InputStream is = null;
		try {

			final DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			HttpPost httpPost = new HttpPost(url);
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			httpClient.setCookieStore(cookieStore);
			HttpResponse httpResponse = httpClient.execute(httpPost,
					localContext);
			HttpEntity httpEntity = httpResponse.getEntity();
			return EntityUtils.toString(httpEntity).replace("@", "");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convertStreamToString(is);
	}

	@Override
	public String getNewSessionValue(String url, HttpPost httppost)
			throws Exception {
		// relogin with some credentail
		return null;
	}

	/**
	 * use this code with android run out of memory error with json response is
	 * large;
	 * 
	 * @param inputStream
	 * @return json String;
	 * @throws IOException
	 */
	private static String convertStreamToString(InputStream inputStream)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = new BufferedInputStream(inputStream);
		byte[] buffer = new byte[4096];
		int n = 0;
		try {
			while (-1 != (n = in.read(buffer))) {
				out.write(buffer, 0, n);
			}
		} finally {
			out.close();
			in.close();
		}

		return out.toString();
	}

	/**
	 * @param is
	 * @return json String;
	 */

	@SuppressWarnings("unused")
	private String ConvertStreamToStringBF(InputStream is) {
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			return sb.toString().replace("@", "");
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		return null;

	}

	@Override
	public <T> T mapFromJSON(String jsonString, Class<T> clazz) {
		return gson.fromJson(jsonString, clazz);
	}

	@Override
	public <T> T mapFromJSON(String jsonString, Type typeof) {

		return gson.fromJson(jsonString, typeof);
	}

	@Override
	public void releaseObjects() {
		gson = null;

		jsonHandler = null;
		cookieStore.clear();
		/* Reinitialized */
		getInstanse();
	}

	@SuppressWarnings("unused")
	private boolean checkLogin() {
		return false;

	}

	@Override
	public String getSessionString() {

		return session_id;
	}

	@Override
	public void setSessionString(String session_id) {
		this.session_id = session_id;
	}

	@Override
	public List<Cookie> getCookies() {

		return cookie;
	}

	@Override
	public void setCookies(List<Cookie> cookie) {
		JSONHandler.cookie = cookie;

	}

	@Override
	public String getNewNounce(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeStringToFile(String name, String jsonString) {
		// TODO Auto-generated method stub

	}

	@Override
	public String readStringFromFile(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie getCookiesValue(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

}
