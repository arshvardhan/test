package com.kelltontech.framework.network.addtionals;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;

import com.kelltontech.framework.C;
import com.kelltontech.framework.network.HttpHelper;

public class HttpAdvanced {
	private static final int HTTP_GET = 1;
	private static final int HTTP_POST = 2;

	protected HttpResponse executeHttpWithRetry(URI uri, int retry, int requestType, List<NameValuePair> postParameters) throws Exception {
		int count = 0;
		while (count < retry) {
			count += 1;
			try {
				switch (requestType) {
				case HTTP_GET:
					// return executeHttpGet(uri);
				case HTTP_POST:
					// return executeHttpPost(uri,postParameters);
				}
			} catch (Exception e) {
				if (count < retry) {
					Log.d("HttpAdvanced", "executeHttpGetWithRetry()" + e.getMessage());
				} else {
					throw e;
				}
			}
		}
		return null;
	}

	protected SingleClientConnManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		try {
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", new TrustingSocketFactory(), 443));
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpConnectionParams.setConnectionTimeout(HttpHelper.getHttpParams(null), (int) C.CONNECTION_TIMEOUT);
		return new SingleClientConnManager(HttpHelper.getHttpParams(null), registry);
	}

	/**
	 *
	 */
	private static class TrustingSocketFactory extends SSLSocketFactory {
		private SSLContext sslContext = null;

		public TrustingSocketFactory() throws Exception {
			super(null);
			setHostnameVerifier(ALLOW_ALL_HOSTNAME_VERIFIER);
		}

		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
			Socket s = getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
			return s;
		}

		@Override
		public Socket createSocket() throws IOException {
			Socket s = getSSLContext().getSocketFactory().createSocket();
			return s;
		}

		private SSLContext getSSLContext() {
			if (this.sslContext == null) {
				this.sslContext = createTrustingSSLContext();
			}
			return this.sslContext;
		}

		private SSLContext createTrustingSSLContext() {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}

				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

			} };

			try {
				sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return sslContext;
		}
	}

	/*
	 * public void executeMultipartPost() throws Exception { try { InputStream
	 * is = this.getAssets().open("data.xml"); HttpClient httpClient = new
	 * DefaultHttpClient(); HttpPost postRequest = new HttpPost(
	 * "http://192.178.10.131/WS2/Upload.aspx"); byte[] data =
	 * IOUtils.toByteArray(is); InputStreamBody isb = new InputStreamBody(new
	 * ByteArrayInputStream( data), "uploadedFile"); StringBody sb1 = new
	 * StringBody("someTextGoesHere"); StringBody sb2 = new
	 * StringBody("someTextGoesHere too"); MultipartEntity multipartContent =
	 * new MultipartEntity(); multipartContent.addPart("uploadedFile", isb);
	 * multipartContent.addPart("one", sb1); multipartContent.addPart("two",
	 * sb2); postRequest.setEntity(multipartContent); HttpResponse res =
	 * httpClient.execute(postRequest); res.getEntity().getContent().close(); }
	 * catch (Throwable e) { throw e; } }
	 */
}