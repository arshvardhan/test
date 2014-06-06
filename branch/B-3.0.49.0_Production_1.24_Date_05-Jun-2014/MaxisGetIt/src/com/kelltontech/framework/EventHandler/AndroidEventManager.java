package com.kelltontech.framework.EventHandler;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Hashtable;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;

import com.kelltontech.framework.model.Request;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.MyHttpClient;
import com.kelltontech.framework.network.utis.MySSLSocketFactory;
import com.kelltontech.maxisgetit.R;

public class AndroidEventManager extends EventManager {

	Context mActivity;

	public AndroidEventManager(Context activity) {
		super();
		mActivity = activity;
	}

	/**
	 * Called to handle the event. Default implementation does nothing.
	 * 
	 * @param int action
	 * @param int type
	 * @param AmcBuzzResponse
	 *            containing
	 *            <ul>
	 *            <li>byte[] for Binary data</li>
	 *            </ul>
	 */
	protected void handle(final int action, final int type, final Request payload) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				StringBuffer buffer=null;
				try {
					buffer = processRequest(payload);
					buffer = null == buffer ? new StringBuffer() : buffer;

					Response response = new Response();
					response.setResponseText(buffer.toString());
					if (payload.getParser() != null) {
						response.setPayload(payload.getParser().parse(buffer.toString()));
					}
					raise(action, type, response);
				} catch (Exception e) {
					Response res = new Response();
					res.setErrorCode(Response.EXCEPTION);
					res.setErrorText("Server not responding, Please try again later.");
//					if(buffer!=null){
//						res.setErrorText("Server not responding Plese try again later"+buffer.toString());
//					}
					res.setException(e);
					raise(action, type, res);
				}
			}
		});
		t.start();
	}

	/**
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	private StringBuffer processRequest(Request payload) throws Exception {
		int requestType = payload.getRequestType();
		MyHttpClient client = new MyHttpClient(createHttpClient());

		switch (requestType) {
		case Request.TYPE_GET:
			Hashtable<String, String> properties = payload.getProperties();
			Hashtable<String, String> header = payload.getHeader();
			return client.doGet(payload.getUri(), properties, header);
		case Request.TYPE_POST:
			Hashtable<String, String> propertiesPost = payload.getProperties();
			return client.doPost(payload.getUri(), payload.getPayload(), propertiesPost);
		case Request.TYPE_MULTI_PART:
			// need to handle
			break;
		case Request.TYPE_LOG:
			// need to handle
			// FileLogger.writeToFile(new String(payload.getPayload()));
			break;

		default:
			break;
		}
		return new StringBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kelltontech.framework.EventHandler.EventManager#handle(int, int,
	 * com.kelltontech.framework.model.Response)
	 */
	protected void handle(int action, int type, Response payload) {
	}

	/**
	 * 
	 * @return
	 */
	// private HttpClient createHttpClient()
	// {
	// HttpParams params = new BasicHttpParams();
	// HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	// HttpProtocolParams.setContentCharset(params,
	// HTTP.DEFAULT_CONTENT_CHARSET);
	// HttpProtocolParams.setUseExpectContinue(params, true);
	// SchemeRegistry schReg = new SchemeRegistry();
	// schReg.register(new Scheme("http",
	// PlainSocketFactory.getSocketFactory(), 80));
	// schReg.register(new Scheme("https",
	// SSLSocketFactory.getSocketFactory(), 443));
	// ClientConnectionManager conMgr = new
	// ThreadSafeClientConnManager(params,schReg);
	// return new DefaultHttpClient(conMgr, params);
	// }

	private HttpClient createHttpClient() {

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		KeyStore trustStore;
		SSLSocketFactory sf = null;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		schReg.register(new Scheme("https", sf, 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		return new DefaultHttpClient(conMgr, params);
	}
}
