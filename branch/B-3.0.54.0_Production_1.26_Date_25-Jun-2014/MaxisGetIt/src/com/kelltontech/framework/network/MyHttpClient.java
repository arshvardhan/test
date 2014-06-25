package com.kelltontech.framework.network;

import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.kelltontech.framework.logger.Logger;
import com.kelltontech.framework.utils.NativeHelper;

public class MyHttpClient {

	private HttpClient client;

	public MyHttpClient(HttpClient client) {
		super();
		this.client = client;
	}

	// Get.........................................................
	public StringBuffer doGet(String url) throws Exception {
		return doGet(url, null, client, null);
	}

	public StringBuffer doGet(String url, Hashtable<String, String> paramsHT, Hashtable<String, String> headerParam) throws Exception {
		return doGet(url, paramsHT, client, headerParam);
	}

	private StringBuffer doGet(String url, Hashtable<String, String> params, HttpClient httpClient, Hashtable<String, String> headerParam) throws Exception {
		String completeURL = HttpHelper.getURLWithPrams(url, params);
		System.out.println(completeURL);
		return doGet(completeURL, httpClient, headerParam);
	}

	private StringBuffer doGet(String url, HttpClient httpClient, Hashtable<String, String> paramsHT) throws Exception {
		trustEveryone();
		if (null == url)
			return null;

		url = NativeHelper.encodeURL(url);

		StringBuffer buffer = null;
		try {
			HttpGet httpget = new HttpGet(url);
			HttpHelper.setHeaderPram(httpget, paramsHT);
			HttpResponse response = httpClient.execute(httpget);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream in = entity.getContent();
					buffer = new StringBuffer(HttpHelper.readString(in));
				}
			} else {
				Logger.easyLog(Logger.LEVEL_DEBUG, this.getClass().getName(), String.valueOf(response.getStatusLine()));
			}
		} catch (Exception e) {

			throw e;
		}
		return buffer;
	}

	// Post.....................................................................
	public StringBuffer doPost(String url, Hashtable<String, String> paramsHT) throws Exception {
		return doPost(url, paramsHT, client);
	}

	private StringBuffer doPost(String url, Hashtable<String, String> paramsHT, HttpClient httpClient) throws Exception {
		/**
		 * need to implement.
		 */
		return doPost(url, null, httpClient, null);
	}

	public StringBuffer doPost(String url, byte[] data, Hashtable<String, String> paramsHT) throws Exception {
		return doPost(url, data, client, paramsHT);
	}

	private StringBuffer doPost(String url, byte[] data, HttpClient httpClient, Hashtable<String, String> paramsHT) throws Exception {
		trustEveryone();
		if (null == url)
			return null;
		StringBuffer buffer = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			// establishing the parameters
			// TODO:List<NameValuePair> params = HttpHelper.getParams(paramsHT);
			// set the parameters to send
			// setting the header...........
			HttpHelper.setHeaderPram(httpPost, paramsHT);
			System.out.println("post data URI"+httpPost.getURI());
			// set the parameters to send
			if (null != data && data.length > 0) {
				ByteArrayEntity req_entity = new ByteArrayEntity(data);
				if (req_entity != null)
					httpPost.setEntity(req_entity);
			}

			HttpResponse response = httpclient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream in = entity.getContent();
					buffer = new StringBuffer(HttpHelper.readString(in));
				}
			} else {
				Logger.easyLog(Logger.LEVEL_DEBUG, "HttpStatus is not Ok", String.valueOf(response.getStatusLine()));
			}
		} catch (Exception e) {

			throw e;
		}

		return buffer;
	}

	// other.........................................................................................

	private void trustEveryone() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) { // should never happen
			e.printStackTrace();
		}
	}

}
